package com.onecall.network.socket

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.telecom.TelecomManager
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onecall.data.CodeLease
import com.onecall.data.ConnectedDevice
import com.onecall.data.SecondaryConnectionRecord
import com.onecall.ui.calls.CallTransferConstants
import com.onecall.ui.calls.CallTransferNotifications
import com.onecall.utils.getCurrentWifiName
import com.onecall.utils.getDeviceDisplayName
import com.onecall.utils.getWifiBroadcastAddresses
import com.onecall.utils.isOnWifi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

object OneCallConnectionManager {

    private const val PREFS_NAME = "onecall_connection_state"
    private const val KEY_MAIN_DEVICE_ID = "main_device_id"
    private const val KEY_MAIN_DEVICE_NAME = "main_device_name"
    private const val KEY_MAIN_CODE = "main_code"
    private const val KEY_MAIN_CODE_GENERATED_AT = "main_code_generated_at"
    private const val KEY_MAIN_CODE_VALIDITY = "main_code_validity"
    private const val KEY_MAIN_TCP_PORT = "main_tcp_port"
    private const val KEY_SECONDARY_DEVICE_ID = "secondary_device_id"
    private const val KEY_SECONDARY_DEVICE_NAME = "secondary_device_name"
    private const val KEY_SECONDARY_AUTO_RECONNECT = "secondary_auto_reconnect"
    private const val KEY_SECONDARY_LAST_HOST = "secondary_last_host"
    private const val KEY_SECONDARY_LAST_PORT = "secondary_last_port"
    private const val KEY_SECONDARY_LAST_WIFI = "secondary_last_wifi"
    private const val KEY_SECONDARY_LAST_CONNECTED_AT = "secondary_last_connected_at"
    private const val KEY_SECONDARY_LAST_MAIN_DEVICE_ID = "secondary_last_main_device_id"
    private const val KEY_SECONDARY_LAST_MAIN_NAME = "secondary_last_main_name"
    private const val KEY_SECONDARY_LAST_CODE = "secondary_last_code"
    private const val DISCOVERY_PORT = 5061
    private const val PREFERRED_TCP_PORT = 6070
    private const val MAX_CONNECTED_DEVICES = 5
    private const val DISCOVERY_TIMEOUT_MILLIS = 2500L
    private const val AUTO_RECONNECT_DELAY_MILLIS = 1500L

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectedDevicesInternal = MutableLiveData<List<ConnectedDevice>>(emptyList())
    val connectedDevices: LiveData<List<ConnectedDevice>> = connectedDevicesInternal
    private val secondaryDeviceCountInternal = MutableLiveData(0)
    val secondaryDeviceCount: LiveData<Int> = secondaryDeviceCountInternal

    private val mainServerReady = AtomicBoolean(false)
    private val reconnectMonitorRegistered = AtomicBoolean(false)
    private val reconnectInProgress = AtomicBoolean(false)
    private val manualDisconnectRequested = AtomicBoolean(false)
    private val connectionLock = Any()
    private val activeConnections = ConcurrentHashMap<String, ActiveConnection>()
    private val ringingPausedDeviceIds = ConcurrentHashMap.newKeySet<String>()

    private var appContext: Context? = null
    private var preferences: SharedPreferences? = null
    private var mainServerSocket: ServerSocket? = null
    private var discoverySocket: DatagramSocket? = null
    private var acceptJob: Job? = null
    private var discoveryJob: Job? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var currentCodeLease: CodeLease? = null
    private var currentMainDeviceId: String? = null
    private var currentMainDeviceName: String? = null
    private var currentMainTcpPort: Int = PREFERRED_TCP_PORT
    private var currentSecondaryDeviceId: String? = null
    private var currentSecondaryDeviceName: String? = null
    private var activeSecondarySocket: Socket? = null
    private var activeSecondaryWriter: BufferedWriter? = null

    data class DiscoveryMatch(
        val host: String,
        val tcpPort: Int,
        val mainDeviceId: String,
        val mainDeviceName: String,
        val codeGeneratedAtMillis: Long,
        val codeValidityMillis: Long,
    )

    sealed class SecondaryConnectResult {
        data class Success(val record: SecondaryConnectionRecord) : SecondaryConnectResult()
        data object WrongCode : SecondaryConnectResult()
        data object SameWifiRequired : SecondaryConnectResult()
        data class Failed(val message: String) : SecondaryConnectResult()
    }

    fun connectedDeviceCount(): Int = connectedDevicesInternal.value.orEmpty().size

    fun lastKnownDeviceCount(): Int = secondaryDeviceCountInternal.value ?: 0

    fun getMainDeviceId(context: Context): String {
        initialize(context)
        return currentMainDeviceId ?: loadOrCreateMainDeviceId()
    }

    fun getSecondaryDeviceId(context: Context): String {
        initialize(context)
        return currentSecondaryDeviceId ?: loadOrCreateSecondaryDeviceId()
    }

    fun getMainDeviceName(context: Context): String {
        initialize(context)
        return currentMainDeviceName ?: loadOrCreateMainDeviceName(context)
    }

    fun getSecondaryDeviceName(context: Context): String {
        initialize(context)
        return currentSecondaryDeviceName ?: loadSecondaryDeviceName(context)
    }

    fun isRingingPaused(deviceId: String): Boolean = ringingPausedDeviceIds.contains(deviceId)

    fun sendTransferRequest(
        context: Context,
        targetDeviceId: String,
        requestId: String,
        callerName: String?,
        callerNumber: String?,
        fromDeviceName: String,
    ): Boolean {
        initialize(context)
        val connection = activeConnections[targetDeviceId] ?: return false
        val fromDeviceId = currentMainDeviceId ?: loadOrCreateMainDeviceId()
        val message = buildString {
            append("TRANSFER_REQUEST|")
            append(requestId)
            append('|')
            append(fromDeviceId)
            append('|')
            append(encodeField(fromDeviceName))
            append('|')
            append(encodeField(callerName))
            append('|')
            append(encodeField(callerNumber))
            append('\n')
        }
        return runCatching {
            connection.writer.write(message)
            connection.writer.flush()
        }.isSuccess
    }

    fun sendTransferResponse(context: Context, requestId: String, accepted: Boolean) {
        initialize(context)
        val writer = activeSecondaryWriter ?: return
        val deviceId = currentSecondaryDeviceId ?: loadOrCreateSecondaryDeviceId()
        val deviceName = currentSecondaryDeviceName ?: loadSecondaryDeviceName(context)
        val status = if (accepted) CallTransferConstants.STATUS_ACCEPTED else CallTransferConstants.STATUS_REJECTED
        val message = buildString {
            append("TRANSFER_RESPONSE|")
            append(requestId)
            append('|')
            append(deviceId)
            append('|')
            append(encodeField(deviceName))
            append('|')
            append(status)
            append('\n')
        }
        runCatching {
            writer.write(message)
            writer.flush()
        }
    }

    fun sendEndCallToMain(context: Context) {
        initialize(context)
        val writer = activeSecondaryWriter ?: return
        val deviceId = currentSecondaryDeviceId ?: loadOrCreateSecondaryDeviceId()
        val deviceName = currentSecondaryDeviceName ?: loadSecondaryDeviceName(context)
        val message = buildString {
            append("END_CALL|")
            append(deviceId)
            append('|')
            append(encodeField(deviceName))
            append('\n')
        }
        runCatching {
            writer.write(message)
            writer.flush()
        }
    }

    fun isAutoReconnectEnabled(context: Context): Boolean {
        initialize(context)
        return preferences?.getBoolean(KEY_SECONDARY_AUTO_RECONNECT, true) ?: true
    }

    fun setAutoReconnectEnabled(context: Context, enabled: Boolean) {
        initialize(context)
        preferences?.edit { putBoolean(KEY_SECONDARY_AUTO_RECONNECT, enabled) }
    }

    fun getLastSecondaryConnectionRecord(context: Context): SecondaryConnectionRecord? {
        initialize(context)
        val prefs = preferences ?: return null
        val code = prefs.getString(KEY_SECONDARY_LAST_CODE, null) ?: return null
        val generatedAt = prefs.getLong(KEY_MAIN_CODE_GENERATED_AT, -1L)
        val validity = prefs.getLong(KEY_MAIN_CODE_VALIDITY, CodeLease.DEFAULT_VALIDITY_MILLIS)
        val mainDeviceId = prefs.getString(KEY_SECONDARY_LAST_MAIN_DEVICE_ID, null) ?: return null
        val mainName = prefs.getString(KEY_SECONDARY_LAST_MAIN_NAME, null) ?: return null
        val host = prefs.getString(KEY_SECONDARY_LAST_HOST, null) ?: return null
        val port = prefs.getInt(KEY_SECONDARY_LAST_PORT, -1)
        val secondaryDeviceId = prefs.getString(KEY_SECONDARY_DEVICE_ID, null) ?: return null
        val secondaryName = prefs.getString(KEY_SECONDARY_DEVICE_NAME, null) ?: loadSecondaryDeviceName(context)
        val connectedAt = prefs.getLong(KEY_SECONDARY_LAST_CONNECTED_AT, -1L)
        val wifiName = prefs.getString(KEY_SECONDARY_LAST_WIFI, null)
        if (generatedAt <= 0L || port <= 0 || connectedAt <= 0L) return null

        return SecondaryConnectionRecord(
            codeLease = CodeLease(code = code, generatedAtMillis = generatedAt, validityMillis = validity),
            mainDeviceId = mainDeviceId,
            mainDeviceName = mainName,
            mainHost = host,
            mainPort = port,
            secondaryDeviceId = secondaryDeviceId,
            secondaryDeviceName = secondaryName,
            connectedAtMillis = connectedAt,
            wifiName = wifiName,
        )
    }

    fun ensureMainServerRunning(context: Context, codeLease: CodeLease) {
        initialize(context)
        currentCodeLease = codeLease
        preferences?.edit {
            putString(KEY_MAIN_CODE, codeLease.code)
            putLong(KEY_MAIN_CODE_GENERATED_AT, codeLease.generatedAtMillis)
            putLong(KEY_MAIN_CODE_VALIDITY, codeLease.validityMillis)
        }

        if (mainServerReady.get()) {
            return
        }

        synchronized(connectionLock) {
            if (mainServerReady.get()) {
                return
            }

            val mainId = loadOrCreateMainDeviceId()
            val mainName = loadOrCreateMainDeviceName(context)
            currentMainDeviceId = mainId
            currentMainDeviceName = mainName

            val serverSocket = createTcpServerSocket()
            mainServerSocket = serverSocket
            currentMainTcpPort = serverSocket.localPort
            preferences?.edit { putInt(KEY_MAIN_TCP_PORT, currentMainTcpPort) }

            val udpSocket = DatagramSocket(DISCOVERY_PORT).apply {
                broadcast = true
                reuseAddress = true
            }
            discoverySocket = udpSocket

            acceptJob = scope.launch { acceptConnectionsLoop(serverSocket) }
            discoveryJob = scope.launch { listenForDiscoveryLoop(udpSocket) }
            registerReconnectMonitor(context.applicationContext)
            mainServerReady.set(true)
        }
    }

    fun updateMainCodeLease(context: Context, codeLease: CodeLease) {
        initialize(context)
        currentCodeLease = codeLease
        preferences?.edit {
            putString(KEY_MAIN_CODE, codeLease.code)
            putLong(KEY_MAIN_CODE_GENERATED_AT, codeLease.generatedAtMillis)
            putLong(KEY_MAIN_CODE_VALIDITY, codeLease.validityMillis)
        }
    }

    suspend fun connectSecondary(context: Context, code: String): SecondaryConnectResult = withContext(Dispatchers.IO) {
        initialize(context)
        if (!context.isOnWifi()) {
            return@withContext SecondaryConnectResult.SameWifiRequired
        }

        val secondaryId = getSecondaryDeviceId(context)
        val secondaryName = loadSecondaryDeviceName(context)
        val requestId = UUID.randomUUID().toString()
        val discoveryMatch = discoverMainDevice(context, code, requestId, secondaryId, secondaryName)
            ?: return@withContext SecondaryConnectResult.SameWifiRequired

        when (discoveryMatch) {
            is DiscoveryOutcome.InvalidCode -> SecondaryConnectResult.WrongCode
            is DiscoveryOutcome.Match -> {
                val socket = Socket()
                try {
                    socket.connect(InetSocketAddress(discoveryMatch.host, discoveryMatch.tcpPort), 2500)
                    val reader = socket.getInputStream().bufferedReader()
                    val writer = socket.getOutputStream().bufferedWriter()
                    writer.write(
                        listOf(
                            "CONNECT",
                            requestId,
                            code,
                            secondaryId,
                            secondaryName,
                        ).joinToString("|") + "\n",
                    )
                    writer.flush()

                    val response = reader.readLine() ?: run {
                        socket.close()
                        return@withContext SecondaryConnectResult.Failed("No response from main device")
                    }

                    when {
                        response.startsWith("ERROR|INVALID_CODE") -> {
                            socket.close()
                            SecondaryConnectResult.WrongCode
                        }
                        response.startsWith("ERROR|DEVICE_LIMIT") -> {
                            socket.close()
                            SecondaryConnectResult.Failed("Maximum connected devices reached")
                        }
                        response.startsWith("WELCOME|") -> {
                            val record = SecondaryConnectionRecord(
                                codeLease = CodeLease(
                                    code = code,
                                    generatedAtMillis = discoveryMatch.codeGeneratedAtMillis,
                                    validityMillis = discoveryMatch.codeValidityMillis,
                                ),
                                mainDeviceId = discoveryMatch.mainDeviceId,
                                mainDeviceName = discoveryMatch.mainDeviceName,
                                mainHost = discoveryMatch.host,
                                mainPort = discoveryMatch.tcpPort,
                                secondaryDeviceId = secondaryId,
                                secondaryDeviceName = secondaryName,
                                connectedAtMillis = System.currentTimeMillis(),
                                wifiName = context.getCurrentWifiName(),
                            )
                            saveSecondaryConnectionRecord(context, record)
                            activeSecondarySocket = socket
                            activeSecondaryWriter = writer
                            manualDisconnectRequested.set(false)
                            currentSecondaryDeviceId = secondaryId
                            currentSecondaryDeviceName = secondaryName
                            scope.launch { monitorSecondarySocket(context.applicationContext, socket, reader) }
                            SecondaryConnectResult.Success(record)
                        }
                        else -> {
                            socket.close()
                            SecondaryConnectResult.Failed("Unexpected response from main device")
                        }
                    }
                } catch (exception: IOException) {
                    socket.close()
                    SecondaryConnectResult.Failed(exception.message ?: "Connection failed")
                }
            }
        }
    }

    fun disconnectSecondary(context: Context) {
        initialize(context)
        manualDisconnectRequested.set(true)
        activeSecondarySocket?.runCatching { close() }
        activeSecondarySocket = null
        activeSecondaryWriter = null
    }

    fun disconnectDevice(deviceId: String) {
        val activeConnection = activeConnections.remove(deviceId) ?: return
        activeConnection.socket.runCatching { close() }
        updateConnectedDevicesSnapshot()
    }

    private fun initialize(context: Context) {
        if (appContext != null) {
            return
        }

        appContext = context.applicationContext
        preferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        currentMainDeviceId = preferences?.getString(KEY_MAIN_DEVICE_ID, null)
        currentMainDeviceName = preferences?.getString(KEY_MAIN_DEVICE_NAME, null)
        currentMainTcpPort = preferences?.getInt(KEY_MAIN_TCP_PORT, PREFERRED_TCP_PORT) ?: PREFERRED_TCP_PORT
        currentSecondaryDeviceId = preferences?.getString(KEY_SECONDARY_DEVICE_ID, null)
        currentSecondaryDeviceName = preferences?.getString(KEY_SECONDARY_DEVICE_NAME, null)

        val code = preferences?.getString(KEY_MAIN_CODE, null)
        val generatedAt = preferences?.getLong(KEY_MAIN_CODE_GENERATED_AT, -1L) ?: -1L
        val validity = preferences?.getLong(KEY_MAIN_CODE_VALIDITY, CodeLease.DEFAULT_VALIDITY_MILLIS)
            ?: CodeLease.DEFAULT_VALIDITY_MILLIS
        if (code != null && generatedAt > 0L) {
            currentCodeLease = CodeLease(code = code, generatedAtMillis = generatedAt, validityMillis = validity)
        }

        registerReconnectMonitor(context.applicationContext)
    }

    private fun createTcpServerSocket(): ServerSocket {
        return runCatching {
            ServerSocket(PREFERRED_TCP_PORT).apply { reuseAddress = true }
        }.getOrElse {
            ServerSocket(0).apply { reuseAddress = true }
        }
    }

    private suspend fun acceptConnectionsLoop(serverSocket: ServerSocket) {
        while (scope.isActive && !serverSocket.isClosed) {
            val socket = try {
                serverSocket.accept()
            } catch (_: IOException) {
                break
            }
            scope.launch { handleMainTcpClient(socket) }
        }
    }

    private suspend fun listenForDiscoveryLoop(socket: DatagramSocket) {
        val buffer = ByteArray(2048)
        while (scope.isActive && !socket.isClosed) {
            val packet = DatagramPacket(buffer, buffer.size)
            try {
                socket.receive(packet)
                handleDiscoveryPacket(socket, packet)
            } catch (_: IOException) {
                break
            }
        }
    }

    private fun handleDiscoveryPacket(socket: DatagramSocket, packet: DatagramPacket) {
        val payload = String(packet.data, packet.offset, packet.length).trim()
        val parts = payload.split("|")
        if (parts.size < 5 || parts[0] != "DISCOVER") {
            return
        }

        val requestId = parts[1]
        val requestedCode = parts[2]
        val lease = currentCodeLease
        val response = when {
            lease == null || !lease.isValid() || lease.code != requestedCode -> {
                "INVALID_CODE|$requestId"
            }
            else -> {
                "MATCH|$requestId|${currentMainDeviceId.orEmpty()}|${currentMainDeviceName.orEmpty()}|$currentMainTcpPort|${lease.generatedAtMillis}|${lease.validityMillis}"
            }
        }

        val responseBytes = response.toByteArray(Charsets.UTF_8)
        val responsePacket = DatagramPacket(responseBytes, responseBytes.size, packet.address, packet.port)
        runCatching { socket.send(responsePacket) }
    }

    private suspend fun handleMainTcpClient(socket: Socket) {
        try {
            socket.tcpNoDelay = true
            val reader = socket.getInputStream().bufferedReader()
            val writer = socket.getOutputStream().bufferedWriter()
            val request = reader.readLine() ?: run {
                socket.close()
                return
            }
            val parts = request.split("|")
            if (parts.size < 5 || parts[0] != "CONNECT") {
                writer.write("ERROR|INVALID_REQUEST\n")
                writer.flush()
                socket.close()
                return
            }

            val code = parts[2]
            val deviceId = parts[3]
            val deviceName = parts[4]
            val lease = currentCodeLease
            if (lease == null || !lease.isValid() || lease.code != code) {
                writer.write("ERROR|INVALID_CODE\n")
                writer.flush()
                socket.close()
                return
            }

            val currentList = connectedDevicesInternal.value.orEmpty()
            val replacingExisting = activeConnections.containsKey(deviceId)
            if (!replacingExisting && currentList.size >= MAX_CONNECTED_DEVICES) {
                writer.write("ERROR|DEVICE_LIMIT\n")
                writer.flush()
                socket.close()
                return
            }

            val connectedDevice = ConnectedDevice(
                deviceId = deviceId,
                displayName = deviceName,
                ipAddress = socket.inetAddress.hostAddress ?: "",
                tcpPort = socket.port,
                connectedAtMillis = System.currentTimeMillis(),
            )

            // Update Device History
            scope.launch {
                appContext?.let { ctx ->
                    val repo = com.onecall.data.history.HistoryRepository.getInstance(ctx)
                    val existing = repo.getDeviceHistoryById(deviceId)
                    val newEntity = com.onecall.data.history.DeviceHistoryEntity(
                        deviceId = deviceId,
                        deviceName = deviceName,
                        firstConnectedAt = existing?.firstConnectedAt ?: System.currentTimeMillis(),
                        lastSeenAt = System.currentTimeMillis(),
                        callsAttended = existing?.callsAttended ?: 0,
                        callsMade = existing?.callsMade ?: 0
                    )
                    repo.addOrUpdateDeviceHistory(newEntity)
                }
            }

            activeConnections[deviceId]?.socket?.runCatching { close() }
            activeConnections[deviceId] = ActiveConnection(
                device = connectedDevice,
                socket = socket,
                writer = writer,
            )
            updateConnectedDevicesSnapshot()

            writer.write(
                listOf(
                    "WELCOME",
                    currentMainDeviceId.orEmpty(),
                    currentMainDeviceName.orEmpty(),
                    currentMainTcpPort.toString(),
                    lease.generatedAtMillis.toString(),
                    lease.validityMillis.toString(),
                ).joinToString("|") + "\n",
            )
            writer.flush()
            monitorMainSocket(socket, deviceId, reader)
        } catch (_: IOException) {
            socket.close()
        }
    }

    private suspend fun monitorMainSocket(socket: Socket, deviceId: String, reader: BufferedReader) {
        withContext(Dispatchers.IO) {
            try {
                while (socket.isConnected && !socket.isClosed) {
                    val line = reader.readLine() ?: break
                    val parts = line.split("|")
                    when (parts.firstOrNull()) {
                        "COUNT" -> {
                            val count = parts.getOrNull(1)?.toIntOrNull()
                            if (count != null) {
                                secondaryDeviceCountInternal.postValue(count)
                            }
                        }
                        "TRANSFER_RESPONSE" -> handleTransferResponse(parts)
                        "END_CALL" -> handleEndCallRequest()
                    }
                }
            } catch (_: IOException) {
                // fall through to cleanup
            } finally {
                val removed = activeConnections.remove(deviceId)
                updateConnectedDevicesSnapshot()
                // Update last seen
                scope.launch {
                    appContext?.let { ctx ->
                        val repo = com.onecall.data.history.HistoryRepository.getInstance(ctx)
                        val existing = repo.getDeviceHistoryById(deviceId)
                        if (existing != null) {
                            repo.addOrUpdateDeviceHistory(existing.copy(lastSeenAt = System.currentTimeMillis()))
                        }
                    }
                }
                runCatching { socket.close() }
            }
        }
    }

    private suspend fun monitorSecondarySocket(context: Context, socket: Socket, reader: BufferedReader) {
        withContext(Dispatchers.IO) {
            try {
                while (socket.isConnected && !socket.isClosed) {
                    val line = reader.readLine() ?: break
                    val parts = line.split("|")
                    when (parts.firstOrNull()) {
                        "COUNT" -> {
                            val count = parts.getOrNull(1)?.toIntOrNull()
                            if (count != null) {
                                secondaryDeviceCountInternal.postValue(count)
                            }
                        }
                        "TRANSFER_REQUEST" -> handleTransferRequest(context, parts)
                    }
                }
            } catch (_: IOException) {
                // fall through to cleanup
            } finally {
                if (activeSecondarySocket === socket) {
                    activeSecondarySocket = null
                }
                activeSecondaryWriter = null
                secondaryDeviceCountInternal.postValue(0)
                runCatching { socket.close() }
                val shouldReconnect = !manualDisconnectRequested.get() && context.isOnWifi() && isAutoReconnectEnabled(context)
                manualDisconnectRequested.set(false)
                if (shouldReconnect) {
                    launchAutoReconnect(context)
                }
            }
        }
    }

    private fun discoverMainDevice(
        context: Context,
        code: String,
        requestId: String,
        secondaryDeviceId: String,
        secondaryDeviceName: String,
    ): DiscoveryOutcome? {
        val discoveryMessage = listOf(
            "DISCOVER",
            requestId,
            code,
            secondaryDeviceId,
            secondaryDeviceName,
        ).joinToString("|")

        val broadcastAddresses = context.getWifiBroadcastAddresses()
        DatagramSocket().use { socket ->
            socket.broadcast = true
            socket.reuseAddress = true
            socket.soTimeout = DISCOVERY_TIMEOUT_MILLIS.toInt()
            val requestBytes = discoveryMessage.toByteArray(Charsets.UTF_8)

            for (address in broadcastAddresses) {
                runCatching {
                    socket.send(
                        DatagramPacket(
                            requestBytes,
                            requestBytes.size,
                            address,
                            DISCOVERY_PORT,
                        ),
                    )
                }
            }

            val responseBuffer = ByteArray(2048)
            while (true) {
                val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
                try {
                    socket.receive(responsePacket)
                    val payload = String(responsePacket.data, responsePacket.offset, responsePacket.length).trim()
                    val parts = payload.split("|")
                    when {
                        parts.isNotEmpty() && parts[0] == "INVALID_CODE" -> return DiscoveryOutcome.InvalidCode
                        parts.size >= 7 && parts[0] == "MATCH" && parts[1] == requestId -> {
                            return DiscoveryOutcome.Match(
                                host = responsePacket.address.hostAddress ?: return null,
                                tcpPort = parts[4].toIntOrNull() ?: return null,
                                mainDeviceId = parts[2],
                                mainDeviceName = parts[3],
                                codeGeneratedAtMillis = parts[5].toLongOrNull() ?: return null,
                                codeValidityMillis = parts[6].toLongOrNull() ?: return null,
                            )
                        }
                    }
                } catch (_: IOException) {
                    return null
                }
            }
        }
    }

    private fun updateConnectedDevicesSnapshot() {
        val devices = activeConnections.values.map { it.device }
            .sortedByDescending { it.connectedAtMillis }
        connectedDevicesInternal.postValue(devices)
        broadcastDeviceCount(devices.size)
    }

    private fun broadcastDeviceCount(count: Int) {
        activeConnections.values.forEach { connection ->
            runCatching {
                connection.writer.write("COUNT|$count\n")
                connection.writer.flush()
            }
        }
    }

    private fun handleTransferRequest(context: Context, parts: List<String>) {
        if (parts.size < 6) return
        val requestId = parts[1]
        val fromDeviceName = decodeField(parts[3])
        val callerName = decodeField(parts[4]).ifBlank { null }
        val callerNumber = decodeField(parts[5]).ifBlank { null }
        CallTransferNotifications.showTransferRequest(
            context = context,
            requestId = requestId,
            fromDeviceName = fromDeviceName,
            callerName = callerName,
            callerNumber = callerNumber,
        )
    }

    private fun handleTransferResponse(parts: List<String>) {
        if (parts.size < 5) return
        val requestId = parts[1]
        val deviceName = decodeField(parts[3])
        val status = parts[4]
        sendTransferResponseBroadcast(requestId, status, deviceName)
    }

    private fun handleEndCallRequest() {
        endRealCallOnMain()
        sendFinishActiveCallBroadcast()
    }

    private fun sendTransferResponseBroadcast(requestId: String, status: String, deviceName: String) {
        val context = appContext ?: return
        val intent = Intent(CallTransferConstants.ACTION_TRANSFER_RESPONSE).apply {
            setPackage(context.packageName)
            putExtra(CallTransferConstants.EXTRA_REQUEST_ID, requestId)
            putExtra(CallTransferConstants.EXTRA_STATUS, status)
            putExtra(CallTransferConstants.EXTRA_DEVICE_NAME, deviceName)
        }
        context.sendBroadcast(intent)
    }

    private fun sendFinishActiveCallBroadcast() {
        val context = appContext ?: return
        val intent = Intent(CallTransferConstants.ACTION_FINISH_ACTIVE_CALL).apply {
            setPackage(context.packageName)
        }
        context.sendBroadcast(intent)
    }

    private fun endRealCallOnMain() {
        val context = appContext ?: return
        val telecomManager = context.getSystemService<TelecomManager>() ?: return
        runCatching {
            val method = TelecomManager::class.java.getMethod("endCall")
            method.invoke(telecomManager)
        }
    }

    private fun encodeField(value: String?): String {
        return URLEncoder.encode(value.orEmpty(), Charsets.UTF_8.name())
    }

    private fun decodeField(value: String): String {
        return URLDecoder.decode(value, Charsets.UTF_8.name())
    }

    private fun saveSecondaryConnectionRecord(context: Context, record: SecondaryConnectionRecord) {
        initialize(context)
        preferences?.edit {
            putString(KEY_SECONDARY_DEVICE_ID, record.secondaryDeviceId)
            putString(KEY_SECONDARY_DEVICE_NAME, record.secondaryDeviceName)
            putString(KEY_SECONDARY_LAST_HOST, record.mainHost)
            putInt(KEY_SECONDARY_LAST_PORT, record.mainPort)
            putString(KEY_SECONDARY_LAST_WIFI, record.wifiName)
            putLong(KEY_SECONDARY_LAST_CONNECTED_AT, record.connectedAtMillis)
            putString(KEY_SECONDARY_LAST_MAIN_DEVICE_ID, record.mainDeviceId)
            putString(KEY_SECONDARY_LAST_MAIN_NAME, record.mainDeviceName)
            putString(KEY_SECONDARY_LAST_CODE, record.codeLease.code)
            putLong(KEY_MAIN_CODE_GENERATED_AT, record.codeLease.generatedAtMillis)
            putLong(KEY_MAIN_CODE_VALIDITY, record.codeLease.validityMillis)
        }
        currentSecondaryDeviceId = record.secondaryDeviceId
        currentSecondaryDeviceName = record.secondaryDeviceName
    }

    private fun registerReconnectMonitor(context: Context) {
        if (!reconnectMonitorRegistered.compareAndSet(false, true)) {
            return
        }

        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) ?: return
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (isAutoReconnectEnabled(context) && getLastSecondaryConnectionRecord(context) != null && activeSecondarySocket == null) {
                    launchAutoReconnect(context)
                }
            }
        }

        runCatching {
            connectivityManager.registerNetworkCallback(request, networkCallback as ConnectivityManager.NetworkCallback)
        }
    }

    private fun launchAutoReconnect(context: Context) {
        val record = getLastSecondaryConnectionRecord(context) ?: return
        if (!record.wifiName.isNullOrBlank()) {
            val currentWifiName = context.getCurrentWifiName()
            if (currentWifiName.isNullOrBlank() || !currentWifiName.equals(record.wifiName, ignoreCase = true)) {
                return
            }
        }

        if (!reconnectInProgress.compareAndSet(false, true)) {
            return
        }

        scope.launch {
            try {
                delay(AUTO_RECONNECT_DELAY_MILLIS)
                connectSecondary(context, record.codeLease.code)
            } finally {
                reconnectInProgress.set(false)
            }
        }
    }

    private fun loadOrCreateMainDeviceId(): String {
        val prefs = preferences ?: return UUID.randomUUID().toString().also { currentMainDeviceId = it }
        return prefs.getString(KEY_MAIN_DEVICE_ID, null) ?: UUID.randomUUID().toString().also { generatedId ->
            prefs.edit { putString(KEY_MAIN_DEVICE_ID, generatedId) }
            currentMainDeviceId = generatedId
        }
    }

    private fun loadOrCreateMainDeviceName(context: Context): String {
        val prefs = preferences ?: return context.getDeviceDisplayName().also { currentMainDeviceName = it }
        return prefs.getString(KEY_MAIN_DEVICE_NAME, null) ?: context.getDeviceDisplayName().also { generatedName ->
            prefs.edit { putString(KEY_MAIN_DEVICE_NAME, generatedName) }
            currentMainDeviceName = generatedName
        }
    }

    private fun loadOrCreateSecondaryDeviceId(): String {
        val prefs = preferences ?: return UUID.randomUUID().toString().also { currentSecondaryDeviceId = it }
        return prefs.getString(KEY_SECONDARY_DEVICE_ID, null) ?: UUID.randomUUID().toString().also { generatedId ->
            prefs.edit { putString(KEY_SECONDARY_DEVICE_ID, generatedId) }
            currentSecondaryDeviceId = generatedId
        }
    }

    private fun loadSecondaryDeviceName(context: Context): String {
        val prefs = preferences ?: return context.getDeviceDisplayName().also { currentSecondaryDeviceName = it }
        return prefs.getString(KEY_SECONDARY_DEVICE_NAME, null) ?: context.getDeviceDisplayName().also { generatedName ->
            prefs.edit { putString(KEY_SECONDARY_DEVICE_NAME, generatedName) }
            currentSecondaryDeviceName = generatedName
        }
    }

    private sealed class DiscoveryOutcome {
        data class Match(
            val host: String,
            val tcpPort: Int,
            val mainDeviceId: String,
            val mainDeviceName: String,
            val codeGeneratedAtMillis: Long,
            val codeValidityMillis: Long,
        ) : DiscoveryOutcome()

        data object InvalidCode : DiscoveryOutcome()
    }

    private data class ActiveConnection(
        val device: ConnectedDevice,
        val socket: Socket,
        val writer: BufferedWriter,
    )
}
