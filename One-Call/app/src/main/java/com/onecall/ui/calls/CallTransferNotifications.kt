package com.onecall.ui.calls

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.onecall.R

object CallTransferNotifications {
    const val ACTION_ACCEPT = "com.onecall.ACTION_TRANSFER_ACCEPT"
    const val ACTION_REJECT = "com.onecall.ACTION_TRANSFER_REJECT"

    const val EXTRA_REQUEST_ID = "extra_request_id"
    const val EXTRA_FROM_DEVICE_NAME = "extra_from_device_name"
    const val EXTRA_CALLER_NAME = "extra_caller_name"
    const val EXTRA_CALLER_NUMBER = "extra_caller_number"
    const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

    private const val CHANNEL_ID = "onecall_transfer"
    private const val CHANNEL_NAME = "OneCall Transfer"

    fun showTransferRequest(
        context: Context,
        requestId: String,
        fromDeviceName: String,
        callerName: String?,
        callerNumber: String?,
    ) {
        ensureChannel(context)

        val notificationId = requestId.hashCode()
        val callerLabel = buildCallerLabel(context, callerName, callerNumber)

        val acceptIntent = Intent(context, CallTransferActionReceiver::class.java).apply {
            action = ACTION_ACCEPT
            putExtra(EXTRA_REQUEST_ID, requestId)
            putExtra(EXTRA_FROM_DEVICE_NAME, fromDeviceName)
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra(EXTRA_CALLER_NUMBER, callerNumber)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val rejectIntent = Intent(context, CallTransferActionReceiver::class.java).apply {
            action = ACTION_REJECT
            putExtra(EXTRA_REQUEST_ID, requestId)
            putExtra(EXTRA_FROM_DEVICE_NAME, fromDeviceName)
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra(EXTRA_CALLER_NUMBER, callerNumber)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }

        val acceptPending = PendingIntent.getBroadcast(
            context,
            notificationId,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val rejectPending = PendingIntent.getBroadcast(
            context,
            notificationId + 1,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .setContentTitle(context.getString(R.string.transfer_notification_title, fromDeviceName))
            .setContentText(context.getString(R.string.transfer_notification_body, callerLabel))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.transfer_accept), acceptPending)
            .addAction(0, context.getString(R.string.transfer_reject), rejectPending)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    private fun buildCallerLabel(context: Context, callerName: String?, callerNumber: String?): String {
        return when {
            !callerName.isNullOrBlank() -> callerName
            !callerNumber.isNullOrBlank() -> callerNumber
            else -> context.getString(R.string.caller_unknown)
        }
    }

    private fun ensureChannel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = manager.getNotificationChannel(CHANNEL_ID)
        if (existing != null) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        manager.createNotificationChannel(channel)
    }
}
