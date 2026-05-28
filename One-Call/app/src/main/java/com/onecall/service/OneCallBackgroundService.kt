package com.onecall.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.onecall.MainActivity
import com.onecall.R
import com.onecall.data.settings.SettingsRepository
import com.onecall.network.socket.OneCallConnectionManager

class OneCallBackgroundService : Service() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var settingsRepository: SettingsRepository

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (settingsRepository.autoReconnect) {
                // Trigger connection logic if auto-reconnect is enabled
                // Typically we just ensure the service is running
                if (com.onecall.data.DeviceRoleStore.isMain(applicationContext)) {
                    val prefs = getSharedPreferences("main_setup_code_prefs", Context.MODE_PRIVATE)
                    val code = prefs.getString("generated_code", null)
                    val genAt = prefs.getLong("generated_at_millis", -1L)
                    if (code != null && genAt > 0) {
                        OneCallConnectionManager.ensureMainServerRunning(applicationContext, com.onecall.data.CodeLease(code, genAt))
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            // Handle disconnection gracefully if needed
        }
    }

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository.getInstance(applicationContext)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, createNotification(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Ensure server is running on start if this is main device
        if (com.onecall.data.DeviceRoleStore.isMain(applicationContext)) {
            val prefs = getSharedPreferences("main_setup_code_prefs", Context.MODE_PRIVATE)
            val code = prefs.getString("generated_code", null)
            val genAt = prefs.getLong("generated_at_millis", -1L)
            if (code != null && genAt > 0) {
                OneCallConnectionManager.ensureMainServerRunning(applicationContext, com.onecall.data.CodeLease(code, genAt))
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val channelId = "onecall_background_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Background Connection",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("OneCall active — SIM ready")
            .setContentText("Tap to open app")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 101

        fun start(context: Context) {
            val intent = Intent(context, OneCallBackgroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }
}
