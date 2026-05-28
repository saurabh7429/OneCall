package com.onecall.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.onecall.core.BluetoothDevicePreference
import com.onecall.service.OneCallService

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {

            Log.d(TAG, "Boot completed, checking if should auto-start service")

            // Only start if device mode was configured
            val prefs = BluetoothDevicePreference(context)
            if (prefs.getDeviceMode() != null) {
                Log.d(TAG, "Device mode configured, starting OneCallService")
                try {
                    val serviceIntent = Intent(context, OneCallService::class.java)
                    context.startForegroundService(serviceIntent)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start service on boot", e)
                }
            }
        }
    }
}
