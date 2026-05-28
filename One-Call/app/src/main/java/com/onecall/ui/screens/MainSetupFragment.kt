package com.onecall.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.onecall.data.CodeLease
import com.onecall.data.ConnectedDevice
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.service.SipServerService
import com.onecall.R
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class MainSetupFragment : Fragment(R.layout.fragment_main_setup) {

	private data class CodeState(
		val code: String,
		val generatedAtMillis: Long,
	) {
		val expiresAtMillis: Long
			get() = generatedAtMillis + CODE_VALIDITY_MILLIS
	}

	private var codeTimer: CountDownTimer? = null
	private var currentCodeState: CodeState? = null
	private var expiredInThisSession = false

	private lateinit var codeDigitsContainer: LinearLayout
	private lateinit var countdownText: TextView
	private lateinit var expiredText: TextView
	private lateinit var regenerateButton: MaterialButton
	private lateinit var shareButton: MaterialButton
	private lateinit var mainCopyCodeButton: ImageButton
	private lateinit var connectedDevicesHeadingText: TextView
	private lateinit var connectedDevicesEmptyText: TextView
	private lateinit var connectedDevicesContainer: LinearLayout

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		codeDigitsContainer = view.findViewById(R.id.codeDigitsContainer)
		countdownText = view.findViewById(R.id.codeCountdownText)
		expiredText = view.findViewById(R.id.codeExpiredText)
		regenerateButton = view.findViewById(R.id.regenerateCodeButton)
		shareButton = view.findViewById(R.id.shareCodeButton)
		mainCopyCodeButton = view.findViewById(R.id.mainCopyCodeButton)
		connectedDevicesHeadingText = view.findViewById(R.id.connectedDevicesHeadingText)
		connectedDevicesEmptyText = view.findViewById(R.id.connectedDevicesEmptyText)
		connectedDevicesContainer = view.findViewById(R.id.connectedDevicesContainer)

		observeConnectedDevices()
		SipServerService.start(requireContext())

		view.findViewById<MaterialButton>(R.id.continueToDashboardButton).setOnClickListener {
			findNavController().navigate(R.id.action_mainSetupFragment_to_dashboardFragment)
		}

		regenerateButton.setOnClickListener {
			generateAndShowNewCode()
		}

		shareButton.setOnClickListener {
			shareCurrentCode()
		}

		mainCopyCodeButton.setOnClickListener {
			copyCurrentCode()
		}

		restoreCodeState()
	}

	override fun onResume() {
		super.onResume()
		restoreCodeState()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		cancelTimer()
	}

	private fun restoreCodeState() {
		val hadRunningTimer = codeTimer != null
		cancelTimer()

		val storedState = readStoredCodeState()
		if (storedState == null) {
			generateAndShowNewCode()
			return
		}

		val remainingMillis = storedState.expiresAtMillis - System.currentTimeMillis()
		when {
			remainingMillis > 0 -> {
				expiredInThisSession = false
				currentCodeState = storedState
				syncMainServer(storedState)
				renderActiveCode(storedState.code, remainingMillis)
				startTimer(remainingMillis)
			}
			expiredInThisSession || hadRunningTimer -> {
				currentCodeState = storedState
				syncMainServer(storedState)
				renderExpiredCode(storedState.code)
			}
			else -> generateAndShowNewCode()
		}
	}

	private fun generateAndShowNewCode() {
		cancelTimer()

		val newState = CodeState(
			code = generateRandomCode(),
			generatedAtMillis = System.currentTimeMillis(),
		)
		persistCodeState(newState)

		currentCodeState = newState
		expiredInThisSession = false
		syncMainServer(newState)
		renderActiveCode(newState.code, CODE_VALIDITY_MILLIS)
		startTimer(CODE_VALIDITY_MILLIS)
	}

	private fun renderActiveCode(code: String, remainingMillis: Long) {
		renderCodeDigits(code, expired = false)
		countdownText.text = formatRemainingTime(remainingMillis)
		countdownText.setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_gray))
		expiredText.visibility = View.GONE
		regenerateButton.visibility = View.GONE
	}

	private fun renderExpiredCode(code: String) {
		cancelTimer()
		renderCodeDigits(code, expired = true)
		countdownText.text = getString(R.string.code_expired)
		countdownText.setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_error))
		expiredText.visibility = View.GONE
		regenerateButton.visibility = View.VISIBLE
	}

	private fun syncMainServer(codeState: CodeState) {
		val lease = CodeLease(
			code = codeState.code,
			generatedAtMillis = codeState.generatedAtMillis,
		)
		OneCallConnectionManager.ensureMainServerRunning(requireContext().applicationContext, lease)
		OneCallConnectionManager.updateMainCodeLease(requireContext().applicationContext, lease)
	}

	private fun observeConnectedDevices() {
		OneCallConnectionManager.connectedDevices.observe(viewLifecycleOwner) { devices ->
			renderConnectedDevices(devices)
		}
	}

	private fun renderConnectedDevices(devices: List<ConnectedDevice>) {
		connectedDevicesHeadingText.text = getString(R.string.connected_devices_heading, devices.size, 5)
		connectedDevicesEmptyText.visibility = if (devices.isEmpty()) View.VISIBLE else View.GONE
		connectedDevicesContainer.visibility = if (devices.isEmpty()) View.GONE else View.VISIBLE
		connectedDevicesContainer.removeAllViews()

		if (devices.isEmpty()) {
			return
		}

		val inflater = LayoutInflater.from(requireContext())
		devices.forEach { device ->
			val row = inflater.inflate(R.layout.item_setup_connected_device, connectedDevicesContainer, false)
			row.findViewById<TextView>(R.id.connectedDeviceNameText).text = device.displayName
			row.findViewById<TextView>(R.id.connectedDeviceMetaText).text = buildString {
				append(device.deviceId)
				append(" • ")
				append(device.ipAddress)
				append(" • Connected")
			}
			connectedDevicesContainer.addView(row)
		}
	}

	private fun renderCodeDigits(code: String, expired: Boolean) {
		codeDigitsContainer.removeAllViews()

		val context = requireContext()
		val digitSize = 46.dpToPx()
		val digitMarginEnd = 8.dpToPx()
		val textColor = ContextCompat.getColor(context, R.color.onecall_blue_dark)
		val boxBackground = ResourcesCompat.getDrawable(resources, R.drawable.bg_code_digit_box, context.theme)

		code.forEachIndexed { index, digit ->
			val digitView = TextView(context).apply {
				layoutParams = LinearLayout.LayoutParams(digitSize, 58.dpToPx()).apply {
					if (index < code.lastIndex) {
						marginEnd = digitMarginEnd
					}
				}
				background = boxBackground?.constantState?.newDrawable()?.mutate()
				gravity = Gravity.CENTER
				includeFontPadding = false
				text = digit.toString()
				textSize = 22f
				setTextColor(textColor)
				typeface = Typeface.DEFAULT_BOLD
			}

			applyStrikeThrough(digitView, expired)
			codeDigitsContainer.addView(digitView)
		}
	}

	private fun applyStrikeThrough(textView: TextView, enabled: Boolean) {
		textView.paintFlags = if (enabled) {
			textView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
		} else {
			textView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
		}
	}

	private fun startTimer(remainingMillis: Long) {
		codeTimer = object : CountDownTimer(remainingMillis, 1000L) {
			override fun onTick(millisUntilFinished: Long) {
				countdownText.text = formatRemainingTime(millisUntilFinished)
			}

			override fun onFinish() {
				expiredInThisSession = true
				currentCodeState?.let { renderExpiredCode(it.code) }
			}
		}.start()
	}

	private fun cancelTimer() {
		codeTimer?.cancel()
		codeTimer = null
	}

	private fun shareCurrentCode() {
		val code = currentCodeState?.code ?: return
		val shareText = getString(R.string.share_code_message, code)
		val intent = Intent(Intent.ACTION_SEND).apply {
			type = "text/plain"
			putExtra(Intent.EXTRA_TEXT, shareText)
		}

		startActivity(Intent.createChooser(intent, getString(R.string.share_code)))
	}

	private fun copyCurrentCode() {
		val code = currentCodeState?.code ?: return
		val clipboardManager = requireContext().getSystemService<ClipboardManager>() ?: return
		clipboardManager.setPrimaryClip(ClipData.newPlainText(getString(R.string.copy_code), code))
		Toast.makeText(requireContext(), R.string.code_copied, Toast.LENGTH_SHORT).show()
	}

	private fun readStoredCodeState(): CodeState? {
		val preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		val code = preferences.getString(KEY_CODE, null) ?: return null
		val generatedAtMillis = preferences.getLong(KEY_GENERATED_AT, -1L)
		if (generatedAtMillis <= 0L) {
			return null
		}
		return CodeState(code = code, generatedAtMillis = generatedAtMillis)
	}

	private fun persistCodeState(codeState: CodeState) {
		requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
			putString(KEY_CODE, codeState.code)
			putLong(KEY_GENERATED_AT, codeState.generatedAtMillis)
		}
	}

	private fun generateRandomCode(): String {
		return buildString(CODE_LENGTH) {
			repeat(CODE_LENGTH) {
				append(Random.nextInt(0, 10))
			}
		}
	}

	private fun formatRemainingTime(remainingMillis: Long): String {
		val totalSeconds = max(0L, remainingMillis / 1000L)
		val minutes = (totalSeconds / 60L).toInt()
		val seconds = (totalSeconds % 60L).toInt()
		return String.format(Locale.getDefault(), "%02d:%02d remaining", minutes, seconds)
	}

	private fun Int.dpToPx(): Int {
		return (this * resources.displayMetrics.density).roundToInt()
	}

	companion object {
		private const val PREFS_NAME = "main_setup_code_prefs"
		private const val KEY_CODE = "generated_code"
		private const val KEY_GENERATED_AT = "generated_at_millis"
		private const val CODE_LENGTH = 6
		private const val CODE_VALIDITY_MILLIS = 10 * 60 * 1000L
	}
}
