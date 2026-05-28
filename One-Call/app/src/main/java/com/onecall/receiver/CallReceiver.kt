package com.onecall.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.onecall.service.OneCallService

/**
 * Fallback call receiver for devices where TelephonyManager listener may not work in background.
 * The primary call detection is done in OneCallService via BluetoothCallBridge.
 * This receiver ensures the service is awake when a call comes in.
 */
class CallReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "CallReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                Log.d(TAG, "Phone state: $state")
                // Ensure service is running
                if (OneCallService.instance == null) {
                    val serviceIntent = Intent(context, OneCallService::class.java)
                    try {
                        context.startForegroundService(serviceIntent)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to start service from receiver", e)
                    }
                }
            }
        }
    }
}
