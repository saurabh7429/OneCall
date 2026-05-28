package com.onecall.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.onecall.R
import com.onecall.core.BluetoothCallBridge
import com.onecall.core.BluetoothDevicePreference
import com.onecall.core.BluetoothHfpController
import com.onecall.core.BluetoothManager
import com.onecall.core.RfcommSignalingService
import com.onecall.data.db.OneCallDatabase
import com.onecall.data.db.entities.CallHistoryEntity
import com.onecall.data.repository.CallHistoryRepository
import com.onecall.model.ConnectionState
import com.onecall.model.DeviceMode
import com.onecall.model.MessageType
import com.onecall.model.RfcommMessage
import com.onecall.ui.call.ActiveCallActivity
import com.onecall.ui.call.IncomingCallActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class OneCallService : Service() {

    companion object {
        private const val TAG = "OneCallService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "onecall_service"

        // Actions
        const val ACTION_ACCEPT_CALL = "com.onecall.ACCEPT_CALL"
        const val ACTION_DECLINE_CALL = "com.onecall.DECLINE_CALL"
        const val ACTION_END_CALL = "com.onecall.END_CALL"
        const val ACTION_TOGGLE_STOP_CALLS = "com.onecall.TOGGLE_STOP_CALLS"

        // Broadcast to UI
        const val BROADCAST_INCOMING_CALL = "com.onecall.INCOMING_CALL"
        const val BROADCAST_CALL_ENDED = "com.onecall.CALL_ENDED"
        const val BROADCAST_CONNECTION_CHANGED = "com.onecall.CONNECTION_CHANGED"
        const val BROADCAST_OUTGOING_REQUEST = "com.onecall.OUTGOING_REQUEST"
        const val BROADCAST_TRANSFER_RESPONSE = "com.onecall.TRANSFER_RESPONSE"

        const val EXTRA_CALLER_NAME = "caller_name"
        const val EXTRA_CALLER_NUMBER = "caller_number"
        const val EXTRA_CONNECTION_STATE = "connection_state"
        const val EXTRA_DEVICE_NAME = "device_name"
        const val EXTRA_APPROVED = "approved"

        @Volatile var instance: OneCallService? = null
    }

    inner class LocalBinder : Binder() {
        fun getService(): OneCallService = this@OneCallService
    }

    private val binder = LocalBinder()

    private lateinit var devicePrefs: BluetoothDevicePreference
    private lateinit var btManager: BluetoothManager
    private var hfpController: BluetoothHfpController? = null
    private var rfcommService: RfcommSignalingService? = null
    private var callBridge: BluetoothCallBridge? = null
    private var repository: CallHistoryRepository? = null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var connectionState: ConnectionState = ConnectionState.DISCONNECTED
        private set
    var connectedDeviceName: String? = null
        private set
    var currentMode: DeviceMode? = null
        private set

    // Current call state
    var currentCallerName: String? = null
    var currentCallerNumber: String? = null
    private var callStartTime: Long = 0L
    private var isCallActive: Boolean = false

    private val btStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                if (state == BluetoothAdapter.STATE_ON) {
                    Log.d(TAG, "Bluetooth turned on, reinitializing...")
                    reinitialize()
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    updateConnectionState(ConnectionState.DISCONNECTED, null)
                    updateNotification("Bluetooth OFF")
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        devicePrefs = BluetoothDevicePreference(this)
        btManager = BluetoothManager(this)
        repository = CallHistoryRepository(OneCallDatabase.getDatabase(this).callHistoryDao())
        createNotificationChannel()
        registerReceiver(btStateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        Log.d(TAG, "OneCallService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_ACCEPT_CALL -> handleAcceptCall(false)
            ACTION_DECLINE_CALL -> handleDeclineCall()
            ACTION_END_CALL -> handleEndCall()
            else -> initializeService()
        }
        return START_STICKY
    }

    private fun initializeService() {
        startForeground(NOTIFICATION_ID, buildNotification(getString(R.string.notification_active)))
        val mode = devicePrefs.getDeviceMode() ?: return
        currentMode = mode

        rfcommService = RfcommSignalingService(
            mode = mode,
            onMessage = { message -> handleRfcommMessage(message) },
            onConnected = {
                val name = rfcommService?.getConnectedDeviceName()
                updateConnectionState(ConnectionState.CONNECTED, name)
                updateNotification(getString(R.string.notification_connected, name ?: ""))
            },
            onDisconnected = {
                updateConnectionState(ConnectionState.DISCONNECTED, null)
                updateNotification(getString(R.string.notification_disconnected))
            }
        )

        hfpController = BluetoothHfpController(
            context = this,
            mode = mode,
            onConnected = { device ->
                val name = try { device.name } catch (e: SecurityException) { device.address }
                connectedDeviceName = name
                Log.d(TAG, "HFP connected to: $name")
            },
            onDisconnected = { Log.d(TAG, "HFP disconnected") },
            onAudioConnected = { Log.d(TAG, "HFP audio connected") },
            onAudioDisconnected = { Log.d(TAG, "HFP audio disconnected") }
        )

        if (mode == DeviceMode.MAIN) {
            callBridge = BluetoothCallBridge(this, rfcommService!!)
            callBridge?.startListening()
            rfcommService?.startServer()
        } else {
            val pairedAddress = devicePrefs.getPairedDeviceAddress()
            if (pairedAddress != null) {
                val device = try {
                    BluetoothAdapter.getDefaultAdapter()?.getRemoteDevice(pairedAddress)
                } catch (e: Exception) { null }
                device?.let { rfcommService?.connectToServer(it) }
            }
        }

        hfpController?.initialize()
        Log.d(TAG, "Service initialized as $mode")
    }

    private fun reinitialize() {
        rfcommService?.stop()
        hfpController?.release()
        callBridge?.stopListening()
        initializeService()
    }

    private fun handleRfcommMessage(message: RfcommMessage) {
        Log.d(TAG, "RFCOMM message: ${message.type}")
        when (message.type) {
            MessageType.RING_START -> {
                if (devicePrefs.isRingEnabled() && !devicePrefs.isCallsStopped()) {
                    currentCallerName = message.name ?: message.callerName
                    currentCallerNumber = message.number ?: message.callerNumber
                    launchIncomingCall(currentCallerName, currentCallerNumber)
                }
            }
            MessageType.RING_STOP, MessageType.CALL_ACCEPTED_MAIN -> {
                dismissIncomingCall()
            }
            MessageType.CALL_ENDED -> {
                endActiveCall()
            }
            MessageType.TRANSFER_REQUEST -> {
                // Secondary received transfer request — accept or reject
                val broadcastIntent = Intent(BROADCAST_TRANSFER_RESPONSE).apply {
                    putExtra(EXTRA_CALLER_NAME, message.callerName)
                    putExtra(EXTRA_CALLER_NUMBER, message.callerNumber)
                }
                sendBroadcast(broadcastIntent)
            }
            MessageType.TRANSFER_ACCEPTED -> {
                // Main device: transfer was accepted — launch active call on secondary
                launchActiveCall(currentCallerName, currentCallerNumber)
            }
            MessageType.TRANSFER_REJECTED -> {
                val intent = Intent(BROADCAST_TRANSFER_RESPONSE).apply {
                    putExtra(EXTRA_APPROVED, false)
                }
                sendBroadcast(intent)
            }
            MessageType.OUTGOING_REQUEST -> {
                if (currentMode == DeviceMode.MAIN) {
                    if (devicePrefs.getAutoApproveOutgoing()) {
                        rfcommService?.sendMessage(RfcommMessage(
                            type = MessageType.OUTGOING_ALLOWED,
                            targetNumber = message.targetNumber
                        ))
                        makeCall(message.targetNumber)
                    } else {
                        val broadcastIntent = Intent(BROADCAST_OUTGOING_REQUEST).apply {
                            putExtra("target_number", message.targetNumber)
                            putExtra(EXTRA_CALLER_NAME, message.callerName)
                        }
                        sendBroadcast(broadcastIntent)
                    }
                }
            }
            MessageType.OUTGOING_ALLOWED -> {
                // Secondary device: main approved our outgoing call
                val intent = Intent(BROADCAST_OUTGOING_REQUEST).apply {
                    putExtra(EXTRA_APPROVED, true)
                }
                sendBroadcast(intent)
            }
            MessageType.OUTGOING_BLOCKED -> {
                val intent = Intent(BROADCAST_OUTGOING_REQUEST).apply {
                    putExtra(EXTRA_APPROVED, false)
                }
                sendBroadcast(intent)
            }
            MessageType.HISTORY_SYNC -> {
                if (currentMode == DeviceMode.SECONDARY) {
                    saveHistoryEntry(message, false)
                }
            }
        }
    }

    fun handleAcceptCall(isSecondary: Boolean) {
        isCallActive = true
        callStartTime = System.currentTimeMillis()
        if (isSecondary && currentMode == DeviceMode.SECONDARY) {
            rfcommService?.sendMessage(RfcommMessage(
                type = MessageType.CALL_ACCEPTED_SECONDARY,
                callerName = currentCallerName,
                callerNumber = currentCallerNumber
            ))
        }
        dismissIncomingCallSilently()
        launchActiveCall(currentCallerName, currentCallerNumber)
    }

    fun handleDeclineCall() {
        rfcommService?.sendMessage(RfcommMessage(type = MessageType.RING_STOP))
        if (currentMode == DeviceMode.MAIN) {
            endCallViaTelecom()
        }
        dismissIncomingCall()
    }

    fun handleEndCall() {
        isCallActive = false
        val duration = if (callStartTime > 0) (System.currentTimeMillis() - callStartTime) / 1000 else 0
        rfcommService?.sendMessage(RfcommMessage(
            type = MessageType.CALL_ENDED,
            callerName = currentCallerName,
            callerNumber = currentCallerNumber
        ))
        // Sync history to secondary
        rfcommService?.sendMessage(RfcommMessage(
            type = MessageType.HISTORY_SYNC,
            callerName = currentCallerName,
            callerNumber = currentCallerNumber
        ))
        if (currentMode == DeviceMode.MAIN) {
            endCallViaTelecom()
            saveHistoryEntry(RfcommMessage(
                type = MessageType.HISTORY_SYNC,
                callerName = currentCallerName,
                callerNumber = currentCallerNumber
            ), true)
        }
        callStartTime = 0
        currentCallerName = null
        currentCallerNumber = null
        sendBroadcast(Intent(BROADCAST_CALL_ENDED))
    }

    private fun launchIncomingCall(callerName: String?, callerNumber: String?) {
        val intent = Intent(this, IncomingCallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra(EXTRA_CALLER_NUMBER, callerNumber)
            putExtra("is_secondary_connected", currentMode == DeviceMode.MAIN && connectionState == ConnectionState.CONNECTED)
        }
        startActivity(intent)
        updateNotification(getString(R.string.notification_incoming_call, callerName ?: "Unknown"))
    }

    private fun launchActiveCall(callerName: String?, callerNumber: String?) {
        val intent = Intent(this, ActiveCallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra(EXTRA_CALLER_NUMBER, callerNumber)
        }
        startActivity(intent)
    }

    private fun dismissIncomingCall() {
        sendBroadcast(Intent(IncomingCallActivity.ACTION_DISMISS))
    }

    private fun dismissIncomingCallSilently() {
        sendBroadcast(Intent(IncomingCallActivity.ACTION_DISMISS))
    }

    private fun endActiveCall() {
        isCallActive = false
        sendBroadcast(Intent(BROADCAST_CALL_ENDED))
    }

    fun sendTransferRequest(targetDeviceName: String) {
        rfcommService?.sendMessage(RfcommMessage(
            type = MessageType.TRANSFER_REQUEST,
            callerName = currentCallerName,
            callerNumber = currentCallerNumber
        ))
    }

    fun respondToTransfer(accepted: Boolean) {
        if (accepted) {
            rfcommService?.sendMessage(RfcommMessage(type = MessageType.TRANSFER_ACCEPTED))
        } else {
            rfcommService?.sendMessage(RfcommMessage(type = MessageType.TRANSFER_REJECTED))
        }
    }

    fun requestOutgoingCall(number: String) {
        if (currentMode == DeviceMode.SECONDARY) {
            rfcommService?.sendMessage(RfcommMessage(
                type = MessageType.OUTGOING_REQUEST,
                targetNumber = number
            ))
        } else {
            makeCall(number)
        }
    }

    fun approveOutgoingCall(number: String) {
        rfcommService?.sendMessage(RfcommMessage(type = MessageType.OUTGOING_ALLOWED))
        makeCall(number)
    }

    fun blockOutgoingCall() {
        rfcommService?.sendMessage(RfcommMessage(type = MessageType.OUTGOING_BLOCKED))
    }

    private fun makeCall(number: String?) {
        if (number == null) return
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = android.net.Uri.parse("tel:$number")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: SecurityException) {
            Log.e(TAG, "No permission to make call", e)
        }
    }

    private fun endCallViaTelecom() {
        try {
            val telecom = getSystemService(TELECOM_SERVICE) as? android.telecom.TelecomManager
            telecom?.endCall()
        } catch (e: SecurityException) {
            Log.e(TAG, "No permission to end call", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error ending call", e)
        }
    }

    private fun saveHistoryEntry(message: RfcommMessage, isPermanent: Boolean) {
        scope.launch {
            try {
                repository?.insert(
                    CallHistoryEntity(
                        callerNumber = message.callerNumber ?: "",
                        callerName = message.callerName,
                        callType = "INCOMING",
                        durationSeconds = 0,
                        timestamp = System.currentTimeMillis(),
                        deviceSource = currentMode?.name ?: "UNKNOWN",
                        isPermanent = isPermanent
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error saving history", e)
            }
        }
    }

    private fun updateConnectionState(state: ConnectionState, deviceName: String?) {
        connectionState = state
        connectedDeviceName = deviceName
        val intent = Intent(BROADCAST_CONNECTION_CHANGED).apply {
            putExtra(EXTRA_CONNECTION_STATE, state.name)
            putExtra(EXTRA_DEVICE_NAME, deviceName)
        }
        sendBroadcast(intent)
    }

    // Notification
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
                setShowBadge(false)
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    fun buildNotification(contentText: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_bluetooth)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun updateNotification(text: String) {
        val nm = getSystemService(NotificationManager::class.java)
        nm?.notify(NOTIFICATION_ID, buildNotification(text))
    }

    fun isConnected(): Boolean = connectionState == ConnectionState.CONNECTED

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        try { unregisterReceiver(btStateReceiver) } catch (e: Exception) {}
        callBridge?.stopListening()
        hfpController?.release()
        rfcommService?.stop()
        Log.d(TAG, "OneCallService destroyed")
    }
}
