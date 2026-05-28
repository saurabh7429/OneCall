package com.onecall.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.onecall.service.OneCallBackgroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            OneCallBackgroundService.start(context)
        }
    }
}
