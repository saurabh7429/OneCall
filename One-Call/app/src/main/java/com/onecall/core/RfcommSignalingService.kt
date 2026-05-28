package com.onecall.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.google.gson.Gson
import com.onecall.model.DeviceMode
import com.onecall.model.MessageType
import com.onecall.model.RfcommMessage
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.UUID

class RfcommSignalingService(
    private val mode: DeviceMode,
    private val onMessage: (RfcommMessage) -> Unit,
    private val onConnected: () -> Unit,
    private val onDisconnected: () -> Unit
) {
    companion object {
        private const val TAG = "RfcommSignalingService"
        val SERVICE_UUID: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
        private const val SERVICE_NAME = "OneCall"
    }

    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isRunning = false
    private var connectedDevice: BluetoothDevice? = null

    fun startServer() {
        isRunning = true
        scope.launch {
            while (isRunning) {
                try {
                    Log.d(TAG, "Starting RFCOMM server...")
                    serverSocket = BluetoothAdapter.getDefaultAdapter()
                        ?.listenUsingRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID)
                    val socket = serverSocket?.accept()
                    if (socket != null && isRunning) {
                        Log.d(TAG, "RFCOMM client connected: ${socket.remoteDevice.address}")
                        handleConnection(socket)
                    }
                } catch (e: IOException) {
                    if (isRunning) {
                        Log.w(TAG, "Server accept failed, retrying in 3s", e)
                        delay(3000)
                    }
                } catch (e: SecurityException) {
                    Log.e(TAG, "SecurityException in RFCOMM server", e)
                    delay(5000)
                }
            }
        }
    }

    fun connectToServer(device: BluetoothDevice) {
        isRunning = true
        scope.launch {
            var delay = 2000L
            var attempt = 0
            while (isRunning) {
                try {
                    Log.d(TAG, "Connecting RFCOMM to ${device.address}, attempt ${attempt + 1}")
                    BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
                    val socket = device.createRfcommSocketToServiceRecord(SERVICE_UUID)
                    socket.connect()
                    Log.d(TAG, "RFCOMM connected to main device")
                    handleConnection(socket)
                    // Reset backoff on success
                    delay = 2000L
                    attempt = 0
                } catch (e: IOException) {
                    if (!isRunning) break
                    Log.w(TAG, "RFCOMM connect failed attempt ${attempt + 1}, retry in ${delay}ms", e)
                    delay(delay)
                    delay = (delay * 2).coerceAtMost(30_000L)
                    attempt++
                } catch (e: SecurityException) {
                    Log.e(TAG, "SecurityException connecting RFCOMM", e)
                    delay(5000)
                }
            }
        }
    }

    private fun handleConnection(socket: BluetoothSocket) {
        clientSocket = socket
        outputStream = socket.outputStream
        connectedDevice = socket.remoteDevice
        withMainThread { onConnected() }
        listenForMessages(socket.inputStream)
    }

    fun sendMessage(message: RfcommMessage) {
        scope.launch {
            try {
                val json = gson.toJson(message) + "\n"
                outputStream?.write(json.toByteArray())
                outputStream?.flush()
            } catch (e: IOException) {
                Log.w(TAG, "Failed to send RFCOMM message", e)
            }
        }
    }

    private fun listenForMessages(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            while (isRunning) {
                val line = reader.readLine() ?: break
                if (line.isNotBlank()) {
                    try {
                        val message = gson.fromJson(line, RfcommMessage::class.java)
                        if (message.type != MessageType.KEEP_ALIVE) {
                            withMainThread { onMessage(message) }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to parse message: $line", e)
                    }
                }
            }
        } catch (e: IOException) {
            Log.w(TAG, "RFCOMM connection lost", e)
        } finally {
            closeSocket()
            withMainThread { onDisconnected() }
        }
    }

    private fun closeSocket() {
        try {
            clientSocket?.close()
            clientSocket = null
            outputStream = null
        } catch (e: IOException) {
            Log.w(TAG, "Error closing RFCOMM socket", e)
        }
    }

    private fun withMainThread(block: () -> Unit) {
        scope.launch(Dispatchers.Main) { block() }
    }

    fun stop() {
        isRunning = false
        closeSocket()
        try {
            serverSocket?.close()
            serverSocket = null
        } catch (e: IOException) {
            Log.w(TAG, "Error closing server socket", e)
        }
        scope.cancel()
    }

    fun isConnected(): Boolean = clientSocket?.isConnected == true

    fun getConnectedDeviceName(): String? {
        return try {
            connectedDevice?.name ?: connectedDevice?.address
        } catch (e: SecurityException) {
            connectedDevice?.address
        }
    }
}
