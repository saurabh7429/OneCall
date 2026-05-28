package com.onecall.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.telecom.TelecomManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.onecall.R
import com.onecall.data.DeviceRoleStore
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.network.sip.MainSipServerEngine
import com.onecall.ui.calls.IncomingCallActivity

class SipServerService : Service() {

    private var sipEngine: MainSipServerEngine? = null
    private var phoneStateListener: PhoneStateListener? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, buildNotification(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!DeviceRoleStore.isMain(this)) {
            stopSelf()
            return START_NOT_STICKY
        }

        if (sipEngine == null) {
            sipEngine = MainSipServerEngine(applicationContext)
        }
        
        val action = intent?.action
        val number = intent?.getStringExtra(EXTRA_NUMBER)
        if (action == ACTION_APPROVE_OUTGOING_CALL && number != null) {
            sipEngine?.approvePendingOutgoingCall(number)
            return START_STICKY
        } else if (action == ACTION_REJECT_OUTGOING_CALL && number != null) {
            sipEngine?.rejectPendingOutgoingCall(number)
            return START_STICKY
        }

        val started = sipEngine?.start() ?: false
        if (!started) {
            Log.w(TAG, "SIP engine not started: missing local IP")
        }

        registerPhoneStateListener()
        return START_STICKY
    }

    override fun onDestroy() {
        unregisterPhoneStateListener()
        sipEngine?.stop()
        sipEngine = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun registerPhoneStateListener() {
        if (phoneStateListener != null) {
            return
        }

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val listener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String?) {
                if (state == TelephonyManager.CALL_STATE_RINGING || telecomManager.isInCall) {
                    Log.i(TAG, "INCOMING CALL DETECTED: ${incomingNumber ?: "unknown"}")
                    val secondaryCount = sipEngine?.getRegisteredSecondaryCount() ?: 0
                    if (secondaryCount > 0) {
                        sipEngine?.broadcastIncomingCall(incomingNumber)
                    } else {
                        IncomingCallActivity.startIncoming(
                            context = applicationContext,
                            role = DeviceRoleStore.ROLE_MAIN,
                            phoneNumber = incomingNumber,
                            ringingCount = OneCallConnectionManager.connectedDeviceCount(),
                        )
                    }
                }
            }
        }
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
        phoneStateListener = listener
    }

    private fun unregisterPhoneStateListener() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener?.let { telephonyManager.listen(it, PhoneStateListener.LISTEN_NONE) }
        phoneStateListener = null
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .setContentTitle(getString(R.string.sip_service_title))
            .setContentText(getString(R.string.sip_service_description))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.sip_service_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        )
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "SipServerService"
        private const val NOTIFICATION_ID = 2001
        private const val NOTIFICATION_CHANNEL_ID = "onecall_sip_service"

        const val ACTION_APPROVE_OUTGOING_CALL = "com.onecall.APPROVE_OUTGOING_CALL"
        const val ACTION_REJECT_OUTGOING_CALL = "com.onecall.REJECT_OUTGOING_CALL"
        const val EXTRA_NUMBER = "number"

        fun start(context: Context) {
            val intent = Intent(context, SipServerService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun approveCall(context: Context, number: String) {
            val intent = Intent(context, SipServerService::class.java).apply {
                action = ACTION_APPROVE_OUTGOING_CALL
                putExtra(EXTRA_NUMBER, number)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun rejectCall(context: Context, number: String) {
            val intent = Intent(context, SipServerService::class.java).apply {
                action = ACTION_REJECT_OUTGOING_CALL
                putExtra(EXTRA_NUMBER, number)
            }
            ContextCompat.startForegroundService(context, intent)
        }
    }
}
