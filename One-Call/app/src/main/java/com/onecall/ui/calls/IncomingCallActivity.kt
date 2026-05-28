package com.onecall.ui.calls

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.onecall.R
import com.onecall.data.DeviceRoleStore
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.network.sip.SecondarySipClient

class IncomingCallActivity : AppCompatActivity() {

    private lateinit var callerNameText: TextView
    private lateinit var callerNumberText: TextView
    private lateinit var ringingCountText: TextView
    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null
    private var role: String = DeviceRoleStore.ROLE_SECONDARY
    private var phoneNumber: String? = null
    private var callAccepted = false
    private var callDeclined = false
    private var initialRingingCount = 0

    private val finishReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        callerNameText = findViewById(R.id.incomingCallerNameText)
        callerNumberText = findViewById(R.id.incomingCallerNumberText)
        ringingCountText = findViewById(R.id.incomingRingingCountText)

        role = intent.getStringExtra(EXTRA_ROLE) ?: DeviceRoleStore.getRole(this).orEmpty().ifBlank {
            DeviceRoleStore.ROLE_SECONDARY
        }
        phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER)
        initialRingingCount = intent.getIntExtra(EXTRA_RINGING_COUNT, 0)

        val displayName = resolveCallerName(phoneNumber) ?: phoneNumber ?: getString(R.string.caller_unknown)
        callerNameText.text = displayName
        callerNumberText.text = phoneNumber ?: getString(R.string.caller_unknown_number)
        ringingCountText.text = getString(R.string.ringing_on_devices, initialRingingCount)

        if (role == DeviceRoleStore.ROLE_MAIN) {
            OneCallConnectionManager.connectedDevices.observe(this) { devices ->
                ringingCountText.text = getString(R.string.ringing_on_devices, devices.size)
            }
        } else {
            OneCallConnectionManager.secondaryDeviceCount.observe(this) { count ->
                ringingCountText.text = getString(R.string.ringing_on_devices, count)
            }
        }

        findViewById<ImageButton>(R.id.incomingDeclineButton).setOnClickListener {
            handleDecline()
        }
        findViewById<ImageButton>(R.id.incomingAcceptButton).setOnClickListener {
            handleAccept()
        }

        ContextCompat.registerReceiver(
            this,
            finishReceiver,
            IntentFilter(ACTION_FINISH_INCOMING_CALL),
            RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onStart() {
        super.onStart()
        startRinging()
    }

    override fun onStop() {
        stopRinging()
        super.onStop()
    }

    override fun onDestroy() {
        if (!callAccepted) {
            recordMissedCall()
        }
        unregisterReceiver(finishReceiver)
        super.onDestroy()
    }

    private fun recordMissedCall() {
        val historyEntry = com.onecall.data.history.CallHistoryEntity(
            callerName = callerNameText.text.toString().takeIf { it != getString(R.string.caller_unknown) },
            phoneNumber = phoneNumber ?: "Unknown",
            callType = "MISSED",
            dateTime = System.currentTimeMillis(),
            durationSeconds = 0,
            attendedByDevice = if (role == DeviceRoleStore.ROLE_MAIN) OneCallConnectionManager.getMainDeviceName(this) else OneCallConnectionManager.getSecondaryDeviceName(this),
            ringCount = initialRingingCount
        )
        lifecycleScope.launch {
            com.onecall.data.history.HistoryRepository.getInstance(applicationContext).addCallHistory(historyEntry)
        }
    }

    private fun handleAccept() {
        callAccepted = true
        if (role == DeviceRoleStore.ROLE_MAIN) {
            acceptRingingCall()
        } else {
            SecondarySipClient.acceptIncomingCall()
        }

        CallActiveActivity.start(this, role, callerNameText.text.toString(), phoneNumber)
        finish()
    }

    private fun handleDecline() {
        callDeclined = true // Treat declined as missed call as well
        if (role == DeviceRoleStore.ROLE_MAIN) {
            rejectRingingCall()
        } else {
            SecondarySipClient.declineIncomingCall()
        }
        finish()
    }

    private fun resolveCallerName(number: String?): String? {
        if (number.isNullOrBlank()) {
            return null
        }
        val permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) {
            return null
        }

        val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
            .appendPath(android.net.Uri.encode(number))
            .build()
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }

    private fun startRinging() {
        if (ringtone == null) {
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        }
        ringtone?.play()

        val vib = getSystemService<Vibrator>()
        vibrator = vib
        if (vib != null && vib.hasVibrator()) {
            val pattern = longArrayOf(0, 600, 400)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, 0)
            }
        }
    }

    private fun stopRinging() {
        ringtone?.stop()
        ringtone = null
        vibrator?.cancel()
        vibrator = null
    }

    private fun acceptRingingCall() {
        val telecomManager = getSystemService<TelecomManager>() ?: return
        runCatching { telecomManager.acceptRingingCall() }
    }

    private fun rejectRingingCall() {
        val telecomManager = getSystemService<TelecomManager>() ?: return
        runCatching {
            val method = TelecomManager::class.java.getMethod("endCall")
            method.invoke(telecomManager)
        }
    }

    companion object {
        private const val EXTRA_ROLE = "extra_role"
        private const val EXTRA_PHONE_NUMBER = "extra_phone"
        private const val EXTRA_RINGING_COUNT = "extra_ringing_count"
        private const val ACTION_FINISH_INCOMING_CALL = "com.onecall.ACTION_FINISH_INCOMING_CALL"

        fun startIncoming(context: Context, role: String, phoneNumber: String?, ringingCount: Int) {
            val intent = Intent(context, IncomingCallActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(EXTRA_ROLE, role)
                putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
                putExtra(EXTRA_RINGING_COUNT, ringingCount)
            }
            context.startActivity(intent)
        }

        fun sendFinish(context: Context) {
            val intent = Intent(ACTION_FINISH_INCOMING_CALL).apply {
                setPackage(context.packageName)
            }
            context.sendBroadcast(intent)
        }
    }
}
