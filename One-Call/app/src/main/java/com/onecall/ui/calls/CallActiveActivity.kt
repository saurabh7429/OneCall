package com.onecall.ui.calls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.telecom.TelecomManager
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.onecall.R
import com.onecall.data.ConnectedDevice
import com.onecall.data.DeviceRoleStore
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.network.sip.SecondarySipClient
import java.util.Locale
import java.util.UUID

class CallActiveActivity : AppCompatActivity() {

    private var role: String = DeviceRoleStore.ROLE_SECONDARY
    private var phoneNumber: String? = null
    private var callerName: String = ""
    private var currentDeviceName: String = ""

    private lateinit var callTimerText: TextView
    private lateinit var deviceIndicatorText: TextView
    private lateinit var muteButton: ImageButton
    private lateinit var speakerButton: ImageButton
    private lateinit var transferButton: ImageButton
    private lateinit var audioManager: AudioManager

    private val handler = Handler(Looper.getMainLooper())
    private var callStartElapsed: Long = 0L
    private var timerRunning = false

    private var isMuted = false
    private var isSpeakerOn = false

    private var pendingTransferRequestId: String? = null
    private var pendingTransferTargetName: String? = null
    private var connectedDevices: List<ConnectedDevice> = emptyList()

    private var transferDialog: BottomSheetDialog? = null
    private var transferDevicesContainer: LinearLayout? = null
    private var transferEmptyText: TextView? = null

    private val callEventsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                CallTransferConstants.ACTION_TRANSFER_RESPONSE -> handleTransferResponse(intent)
                CallTransferConstants.ACTION_FINISH_ACTIVE_CALL -> finish()
            }
        }
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            if (timerRunning) {
                handler.postDelayed(this, 1000L)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_active)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        role = intent.getStringExtra(EXTRA_ROLE) ?: DeviceRoleStore.getRole(this).orEmpty().ifBlank {
            DeviceRoleStore.ROLE_SECONDARY
        }
        callerName = intent.getStringExtra(EXTRA_CALLER_NAME).orEmpty()
        phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER)
        callStartElapsed = intent.getLongExtra(EXTRA_CALL_START, SystemClock.elapsedRealtime())

        currentDeviceName = when (role) {
            DeviceRoleStore.ROLE_MAIN -> OneCallConnectionManager.getMainDeviceName(this)
            else -> OneCallConnectionManager.getSecondaryDeviceName(this)
        }

        val displayName = if (callerName.isBlank()) getString(R.string.caller_unknown) else callerName
        findViewById<TextView>(R.id.callActiveNameText).text = displayName
        findViewById<TextView>(R.id.callActiveNumberText).text = phoneNumber ?: getString(R.string.caller_unknown_number)

        callTimerText = findViewById(R.id.callActiveTimerText)
        deviceIndicatorText = findViewById(R.id.callActiveDeviceText)
        muteButton = findViewById(R.id.callActiveMuteButton)
        speakerButton = findViewById(R.id.callActiveSpeakerButton)
        transferButton = findViewById(R.id.callActiveTransferButton)

        deviceIndicatorText.text = getString(R.string.device_indicator, currentDeviceName)

        audioManager = getSystemService<AudioManager>() ?: run {
            Toast.makeText(this, R.string.audio_setup_failed, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION

        isMuted = audioManager.isMicrophoneMute
        isSpeakerOn = audioManager.isSpeakerphoneOn
        updateMuteUi()
        updateSpeakerUi()

        muteButton.setOnClickListener { toggleMute() }
        speakerButton.setOnClickListener { toggleSpeaker() }
        transferButton.setOnClickListener { showTransferSheet() }
        findViewById<ImageButton>(R.id.callActiveEndButton).setOnClickListener { endCall() }

        ContextCompat.registerReceiver(
            this,
            callEventsReceiver,
            IntentFilter().apply {
                addAction(CallTransferConstants.ACTION_TRANSFER_RESPONSE)
                addAction(CallTransferConstants.ACTION_FINISH_ACTIVE_CALL)
            },
            RECEIVER_NOT_EXPORTED,
        )

        if (role == DeviceRoleStore.ROLE_MAIN) {
            OneCallConnectionManager.connectedDevices.observe(this) { devices ->
                connectedDevices = devices
                renderTransferDevices()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        stopTimer()
        super.onStop()
    }

    override fun onDestroy() {
        unregisterReceiver(callEventsReceiver)
        transferDialog?.dismiss()
        super.onDestroy()
    }

    private fun startTimer() {
        if (timerRunning) return
        timerRunning = true
        handler.post(timerRunnable)
    }

    private fun stopTimer() {
        timerRunning = false
        handler.removeCallbacks(timerRunnable)
    }

    private fun updateTimer() {
        val elapsedSeconds = (SystemClock.elapsedRealtime() - callStartElapsed) / 1000
        val minutes = elapsedSeconds / 60
        val seconds = elapsedSeconds % 60
        callTimerText.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun toggleMute() {
        val nextState = !isMuted
        audioManager.setMicrophoneMute(nextState)
        isMuted = nextState
        updateMuteUi()
    }

    private fun toggleSpeaker() {
        val nextState = !isSpeakerOn
        audioManager.setSpeakerphoneOn(nextState)
        isSpeakerOn = nextState
        updateSpeakerUi()
    }

    private fun updateMuteUi() {
        muteButton.isSelected = isMuted
        val icon = if (isMuted) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_btn_speak_now
        muteButton.setImageResource(icon)
    }

    private fun updateSpeakerUi() {
        speakerButton.isSelected = isSpeakerOn
        val icon = if (isSpeakerOn) android.R.drawable.ic_lock_silent_mode_off else android.R.drawable.ic_lock_silent_mode
        speakerButton.setImageResource(icon)
    }

    private fun showTransferSheet() {
        if (role != DeviceRoleStore.ROLE_MAIN) {
            Toast.makeText(this, R.string.transfer_unavailable, Toast.LENGTH_SHORT).show()
            return
        }

        if (transferDialog == null) {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_transfer, null)
            transferDevicesContainer = view.findViewById(R.id.transferDevicesContainer)
            transferEmptyText = view.findViewById(R.id.transferEmptyText)
            dialog.setContentView(view)
            transferDialog = dialog
        }

        renderTransferDevices()
        transferDialog?.show()
    }

    private fun renderTransferDevices() {
        val container = transferDevicesContainer ?: return
        val emptyText = transferEmptyText ?: return
        val filtered = connectedDevices.filterNot { OneCallConnectionManager.isRingingPaused(it.deviceId) }

        container.removeAllViews()
        if (filtered.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            return
        }

        emptyText.visibility = View.GONE
        val inflater = layoutInflater
        filtered.forEach { device ->
            val row = inflater.inflate(R.layout.item_transfer_device, container, false)
            row.findViewById<TextView>(R.id.transferDeviceNameText).text = device.displayName
            row.findViewById<TextView>(R.id.transferDeviceStatusText).text = getString(R.string.transfer_device_online)
            row.setOnClickListener {
                requestTransfer(device)
                transferDialog?.dismiss()
            }
            container.addView(row)
        }
    }

    private fun requestTransfer(device: ConnectedDevice) {
        val requestId = UUID.randomUUID().toString()
        pendingTransferRequestId = requestId
        pendingTransferTargetName = device.displayName

        val sent = OneCallConnectionManager.sendTransferRequest(
            context = this,
            targetDeviceId = device.deviceId,
            requestId = requestId,
            callerName = callerName,
            callerNumber = phoneNumber,
            fromDeviceName = currentDeviceName,
        )

        if (sent) {
            Toast.makeText(this, getString(R.string.transfer_sent, device.displayName), Toast.LENGTH_SHORT).show()
        } else {
            pendingTransferRequestId = null
            pendingTransferTargetName = null
            Toast.makeText(this, R.string.transfer_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleTransferResponse(intent: Intent) {
        val requestId = intent.getStringExtra(CallTransferConstants.EXTRA_REQUEST_ID) ?: return
        if (requestId != pendingTransferRequestId) {
            return
        }

        val status = intent.getStringExtra(CallTransferConstants.EXTRA_STATUS) ?: return
        val deviceName = intent.getStringExtra(CallTransferConstants.EXTRA_DEVICE_NAME)
            ?: pendingTransferTargetName
            ?: getString(R.string.caller_unknown)

        pendingTransferRequestId = null
        pendingTransferTargetName = null

        when (status) {
            CallTransferConstants.STATUS_ACCEPTED -> {
                Toast.makeText(this, getString(R.string.transfer_accepted, deviceName), Toast.LENGTH_SHORT).show()
                finish()
            }
            CallTransferConstants.STATUS_REJECTED -> {
                Toast.makeText(this, getString(R.string.transfer_rejected, deviceName), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun endCall() {
        val elapsedSeconds = ((SystemClock.elapsedRealtime() - callStartElapsed) / 1000).toInt()
        val isOutgoing = intent.getBooleanExtra(EXTRA_IS_OUTGOING, false)
        val type = if (isOutgoing) "OUTGOING" else "INCOMING"
        
        val historyEntry = com.onecall.data.history.CallHistoryEntity(
            callerName = callerName.takeIf { it.isNotBlank() },
            phoneNumber = phoneNumber ?: "Unknown",
            callType = type,
            dateTime = System.currentTimeMillis() - (elapsedSeconds * 1000L),
            durationSeconds = elapsedSeconds,
            attendedByDevice = currentDeviceName,
            ringCount = 0
        )
        
        // Record History
        lifecycleScope.launch {
            com.onecall.data.history.HistoryRepository.getInstance(applicationContext).addCallHistory(historyEntry)
        }

        if (role == DeviceRoleStore.ROLE_MAIN) {
            val telecomManager = getSystemService<TelecomManager>() ?: return
            runCatching {
                val method = TelecomManager::class.java.getMethod("endCall")
                method.invoke(telecomManager)
            }
        } else {
            SecondarySipClient.declineIncomingCall()
            OneCallConnectionManager.sendEndCallToMain(this)
        }
        finish()
    }

    companion object {
        private const val EXTRA_ROLE = "extra_role"
        private const val EXTRA_CALLER_NAME = "extra_caller_name"
        private const val EXTRA_PHONE_NUMBER = "extra_phone"
        private const val EXTRA_CALL_START = "extra_call_start"
        private const val EXTRA_IS_OUTGOING = "extra_is_outgoing"

        fun start(context: Context, role: String, callerName: String, phoneNumber: String?) {
            val intent = Intent(context, CallActiveActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(EXTRA_ROLE, role)
                putExtra(EXTRA_CALLER_NAME, callerName)
                putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
                putExtra(EXTRA_CALL_START, SystemClock.elapsedRealtime())
                putExtra(EXTRA_IS_OUTGOING, false)
            }
            context.startActivity(intent)
        }

        fun startOutgoing(context: Context, phoneNumber: String, contactName: String) {
            val role = DeviceRoleStore.getRole(context) ?: DeviceRoleStore.ROLE_SECONDARY
            val intent = Intent(context, CallActiveActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(EXTRA_ROLE, role)
                putExtra(EXTRA_CALLER_NAME, contactName)
                putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
                putExtra(EXTRA_CALL_START, SystemClock.elapsedRealtime())
                putExtra(EXTRA_IS_OUTGOING, true)
            }
            context.startActivity(intent)
        }

        fun sendFinish(context: Context) {
            val intent = Intent(CallTransferConstants.ACTION_FINISH_ACTIVE_CALL).apply {
                setPackage(context.packageName)
            }
            context.sendBroadcast(intent)
        }
    }
}
