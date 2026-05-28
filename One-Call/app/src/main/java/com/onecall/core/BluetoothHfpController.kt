package com.onecall.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.onecall.model.DeviceMode

class BluetoothHfpController(
    private val context: Context,
    private val mode: DeviceMode,
    private val onConnected: (BluetoothDevice) -> Unit,
    private val onDisconnected: () -> Unit,
    private val onAudioConnected: () -> Unit,
    private val onAudioDisconnected: () -> Unit
) {
    companion object {
        private const val TAG = "BluetoothHfpController"
        // HFP Client profile constant (hidden API)
        private const val PROFILE_HFP_CLIENT = 16
    }

    private var headsetProxy: BluetoothHeadset? = null
    private var isReceiverRegistered = false

    private val serviceListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            Log.d(TAG, "HFP profile service connected, profile=$profile, mode=$mode")
            if (mode == DeviceMode.MAIN && profile == BluetoothProfile.HEADSET) {
                headsetProxy = proxy as BluetoothHeadset
            }
            // For secondary, the OS routes audio automatically via HFP client
        }

        override fun onServiceDisconnected(profile: Int) {
            Log.d(TAG, "HFP profile service disconnected")
            headsetProxy = null
        }
    }

    private val hfpReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                    val device = if (android.os.Build.VERSION.SDK_INT >= 33) {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    Log.d(TAG, "HFP connection state changed: $state, device: $device")
                    when (state) {
                        BluetoothProfile.STATE_CONNECTED -> device?.let { onConnected(it) }
                        BluetoothProfile.STATE_DISCONNECTED -> onDisconnected()
                    }
                }
                BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    Log.d(TAG, "HFP audio state changed: $state")
                    when (state) {
                        BluetoothHeadset.STATE_AUDIO_CONNECTED -> onAudioConnected()
                        BluetoothHeadset.STATE_AUDIO_DISCONNECTED -> onAudioDisconnected()
                    }
                }
            }
        }
    }

    fun initialize() {
        try {
            val profileType = if (mode == DeviceMode.MAIN) {
                BluetoothProfile.HEADSET
            } else {
                PROFILE_HFP_CLIENT
            }
            BluetoothAdapter.getDefaultAdapter()
                ?.getProfileProxy(context, serviceListener, profileType)
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException initializing HFP", e)
        }
        registerReceiver()
    }

    private fun registerReceiver() {
        if (isReceiverRegistered) return
        val filter = IntentFilter().apply {
            addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)
        }
        try {
            context.registerReceiver(hfpReceiver, filter)
            isReceiverRegistered = true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register HFP receiver", e)
        }
    }

    fun release() {
        try {
            if (isReceiverRegistered) {
                context.unregisterReceiver(hfpReceiver)
                isReceiverRegistered = false
            }
            headsetProxy?.let {
                BluetoothAdapter.getDefaultAdapter()?.closeProfileProxy(BluetoothProfile.HEADSET, it)
            }
            headsetProxy = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing HFP controller", e)
        }
    }

    fun getConnectedDevices(): List<BluetoothDevice> {
        return try {
            headsetProxy?.connectedDevices ?: emptyList()
        } catch (e: SecurityException) {
            emptyList()
        }
    }
}
