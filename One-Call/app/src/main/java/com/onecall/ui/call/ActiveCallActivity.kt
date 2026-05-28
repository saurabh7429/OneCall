package com.onecall.ui.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onecall.R
import com.onecall.databinding.ActivityActiveCallBinding
import com.onecall.service.OneCallService

class ActiveCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActiveCallBinding
    private lateinit var audioManager: AudioManager
    private val timerHandler = Handler(Looper.getMainLooper())
    private var elapsedSeconds = 0
    private var isMuted = false
    private var isSpeaker = false

    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedSeconds++
            binding.tvCallDuration.text = formatTime(elapsedSeconds)
            timerHandler.postDelayed(this, 1000)
        }
    }

    private val callEndedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == OneCallService.BROADCAST_CALL_ENDED) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val callerName = intent.getStringExtra(OneCallService.EXTRA_CALLER_NAME)
        val callerNumber = intent.getStringExtra(OneCallService.EXTRA_CALLER_NUMBER)

        binding.tvCallerName.text = callerName ?: getString(R.string.unknown_caller)
        binding.tvCallerNumber.text = callerNumber ?: ""

        registerReceiver(callEndedReceiver, IntentFilter(OneCallService.BROADCAST_CALL_ENDED))

        // Start timer
        timerHandler.post(timerRunnable)

        // Mute
        binding.btnMute.setOnClickListener {
            isMuted = !isMuted
            audioManager.isMicrophoneMute = isMuted
            binding.ivMuteIcon.setImageResource(
                if (isMuted) R.drawable.ic_mic_off else R.drawable.ic_mic
            )
        }

        // Speaker — only allowed on explicit user request
        binding.btnSpeaker.setOnClickListener {
            isSpeaker = !isSpeaker
            // RULE: speakerphone is OFF by default, only enable when user explicitly taps
            audioManager.isSpeakerphoneOn = isSpeaker
        }

        // Transfer
        binding.btnTransfer.setOnClickListener {
            showTransferDialog()
        }

        // End Call
        binding.btnEndCall.setOnClickListener {
            OneCallService.instance?.handleEndCall()
            finish()
        }
    }

    private fun showTransferDialog() {
        val deviceName = OneCallService.instance?.connectedDeviceName ?: "Secondary Device"
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.transfer_title))
            .setMessage(getString(R.string.transfer_to, deviceName))
            .setPositiveButton("Transfer") { _, _ ->
                OneCallService.instance?.sendTransferRequest(deviceName)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun formatTime(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) String.format("%02d:%02d:%02d", h, m, s)
        else String.format("%02d:%02d", m, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        try { unregisterReceiver(callEndedReceiver) } catch (e: Exception) {}
        // Restore audio mode
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.isSpeakerphoneOn = false
    }
}
