package com.onecall.ui.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.onecall.R
import com.onecall.databinding.ActivityIncomingCallBinding
import com.onecall.service.OneCallService

class IncomingCallActivity : AppCompatActivity() {

    companion object {
        const val ACTION_DISMISS = "com.onecall.DISMISS_INCOMING_CALL"
    }

    private lateinit var binding: ActivityIncomingCallBinding
    private lateinit var audioManager: AudioManager

    private val dismissReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_DISMISS) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show on lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        binding = ActivityIncomingCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val callerName = intent.getStringExtra(OneCallService.EXTRA_CALLER_NAME)
        val callerNumber = intent.getStringExtra(OneCallService.EXTRA_CALLER_NUMBER)
        val isSecondaryConnected = intent.getBooleanExtra("is_secondary_connected", false)

        binding.tvCallerName.text = callerName ?: getString(R.string.unknown_caller)
        binding.tvCallerNumber.text = callerNumber ?: ""
        binding.tvRingingStatus.text = if (isSecondaryConnected) {
            getString(R.string.ringing_on_two)
        } else {
            getString(R.string.ringing_on_one)
        }

        registerReceiver(dismissReceiver, IntentFilter(ACTION_DISMISS))

        binding.btnAccept.setOnClickListener {
            // DO NOT enable speakerphone — earpiece only
            audioManager.mode = AudioManager.MODE_IN_CALL
            // audioManager.isSpeakerphoneOn = false  // Already false by default

            OneCallService.instance?.handleAcceptCall(false)
            finish()
        }

        binding.btnDecline.setOnClickListener {
            OneCallService.instance?.handleDeclineCall()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(dismissReceiver) } catch (e: Exception) {}
    }
}
