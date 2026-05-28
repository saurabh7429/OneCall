package com.onecall.ui.calls

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.onecall.data.DeviceRoleStore
import com.onecall.network.socket.OneCallConnectionManager

class CallTransferActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val requestId = intent.getStringExtra(CallTransferNotifications.EXTRA_REQUEST_ID) ?: return
        val callerName = intent.getStringExtra(CallTransferNotifications.EXTRA_CALLER_NAME)
        val callerNumber = intent.getStringExtra(CallTransferNotifications.EXTRA_CALLER_NUMBER)
        val notificationId = intent.getIntExtra(CallTransferNotifications.EXTRA_NOTIFICATION_ID, requestId.hashCode())

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)

        when (intent.action) {
            CallTransferNotifications.ACTION_ACCEPT -> {
                OneCallConnectionManager.sendTransferResponse(context, requestId, accepted = true)
                CallActiveActivity.start(
                    context = context,
                    role = DeviceRoleStore.ROLE_SECONDARY,
                    callerName = callerName.orEmpty(),
                    phoneNumber = callerNumber,
                )
            }
            CallTransferNotifications.ACTION_REJECT -> {
                OneCallConnectionManager.sendTransferResponse(context, requestId, accepted = false)
            }
        }
    }
}
