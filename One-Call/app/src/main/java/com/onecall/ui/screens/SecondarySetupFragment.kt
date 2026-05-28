package com.onecall.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.onecall.R
import com.onecall.network.socket.OneCallConnectionManager
import com.onecall.network.sip.SecondarySipClient
import kotlinx.coroutines.launch

class SecondarySetupFragment : Fragment(R.layout.fragment_secondary_setup) {

    private lateinit var codeEntryCard: MaterialCardView
    private lateinit var codeInputsContainer: LinearLayout
    private lateinit var statusText: TextView
    private lateinit var connectingContainer: View
    private lateinit var connectingText: TextView
    private lateinit var connectButton: MaterialButton
    private lateinit var digitInputs: List<DigitCodeEditText>

    private var isConnecting = false
    private var isConnected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeEntryCard = view.findViewById(R.id.secondaryCodeEntryCard)
        codeInputsContainer = view.findViewById(R.id.secondaryCodeInputsContainer)
        statusText = view.findViewById(R.id.secondaryStatusText)
        connectingContainer = view.findViewById(R.id.secondaryConnectingContainer)
        connectingText = view.findViewById(R.id.secondaryConnectingText)
        connectButton = view.findViewById(R.id.secondaryConnectButton)

        buildDigitInputs()
        setInfoState()

        connectButton.setOnClickListener {
            startConnectionAttempt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        digitInputs = emptyList()
    }

    private fun buildDigitInputs() {
        codeInputsContainer.removeAllViews()
        val inputs = mutableListOf<DigitCodeEditText>()
        val boxWidth = 50.dpToPx()
        val boxHeight = 64.dpToPx()
        val boxMargin = 8.dpToPx()

        repeat(6) { index ->
            val input = DigitCodeEditText(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(boxWidth, boxHeight).apply {
                    if (index > 0) {
                        marginStart = boxMargin
                    }
                }
                setBackgroundResource(R.drawable.bg_secondary_code_box)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_blue_dark))
                textSize = 22f

                onDigitEntered = {
                    if (!isConnecting && !isConnected) {
                        clearErrorState()
                        moveToNextInput(index)
                        updateConnectButtonState()
                    }
                }

                onBackspaceAtEmpty = {
                    if (!isConnecting && !isConnected) {
                        moveToPreviousInput(index)
                        updateConnectButtonState()
                    }
                }

                onContentChanged = {
                    if (!isConnecting && !isConnected) {
                        clearErrorState()
                        updateConnectButtonState()
                    }
                }
            }

            inputs.add(input)
            codeInputsContainer.addView(input)
        }

        digitInputs = inputs
        updateConnectButtonState()
    }

    private fun startConnectionAttempt() {
        val code = collectCode() ?: return
        isConnecting = true
        updateConnectButtonState()
        setConnectingState()

        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = OneCallConnectionManager.connectSecondary(requireContext().applicationContext, code)) {
                is OneCallConnectionManager.SecondaryConnectResult.Success -> {
                    isConnected = true
                    isConnecting = false
                    setConnectedState(result.record.mainDeviceName)
                    SecondarySipClient.start(requireContext().applicationContext, result.record)
                    updateConnectButtonState()
                }
                OneCallConnectionManager.SecondaryConnectResult.WrongCode -> {
                    isConnecting = false
                    setErrorState(getString(R.string.secondary_invalid_code))
                    updateConnectButtonState()
                }
                OneCallConnectionManager.SecondaryConnectResult.SameWifiRequired -> {
                    isConnecting = false
                    setErrorState(getString(R.string.secondary_same_wifi_required))
                    updateConnectButtonState()
                }
                is OneCallConnectionManager.SecondaryConnectResult.Failed -> {
                    isConnecting = false
                    setErrorState(result.message)
                    updateConnectButtonState()
                }
            }
        }
    }

    private fun setInfoState() {
        statusText.visibility = View.GONE
        connectingContainer.visibility = View.GONE
        codeEntryCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.onecall_card_stroke)
        digitInputs.forEach { it.setBackgroundResource(R.drawable.bg_secondary_code_box) }
    }

    private fun setConnectingState() {
        statusText.visibility = View.VISIBLE
        statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_gray))
        statusText.text = getString(R.string.secondary_connecting)
        connectingContainer.visibility = View.VISIBLE
        connectingText.text = getString(R.string.secondary_connecting)
        codeEntryCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.onecall_blue)
        digitInputs.forEach { it.setBackgroundResource(R.drawable.bg_secondary_code_box) }
    }

    private fun setErrorState(message: String) {
        statusText.visibility = View.VISIBLE
        statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_error))
        statusText.text = message
        connectingContainer.visibility = View.GONE
        codeEntryCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.onecall_error)
        digitInputs.forEach { it.setBackgroundResource(R.drawable.bg_secondary_code_box_error) }
    }

    private fun setConnectedState(mainDeviceName: String) {
        statusText.visibility = View.VISIBLE
        statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.onecall_success))
        statusText.text = getString(R.string.secondary_connected_to, mainDeviceName)
        connectingContainer.visibility = View.GONE
        codeEntryCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.onecall_success)
        digitInputs.forEach { it.setBackgroundResource(R.drawable.bg_secondary_code_box) }
        digitInputs.forEach { it.isEnabled = false }
        connectButton.isEnabled = false
    }

    private fun clearErrorState() {
        if (isConnected || isConnecting) {
            return
        }

        statusText.visibility = View.GONE
        connectingContainer.visibility = View.GONE
        codeEntryCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.onecall_card_stroke)
        digitInputs.forEach { it.setBackgroundResource(R.drawable.bg_secondary_code_box) }
    }

    private fun updateConnectButtonState() {
        val canConnect = !isConnecting && !isConnected && digitInputs.size == 6 && digitInputs.all { !it.text.isNullOrEmpty() }
        connectButton.isEnabled = canConnect
        connectButton.alpha = if (canConnect) 1f else 0.5f
    }

    private fun collectCode(): String? {
        if (digitInputs.size != 6) {
            return null
        }

        val code = digitInputs.joinToString(separator = "") { it.text?.toString().orEmpty() }
        return code.takeIf { it.length == 6 && it.all(Char::isDigit) }
    }

    private fun moveToNextInput(index: Int) {
        if (index < digitInputs.lastIndex) {
            digitInputs[index + 1].requestFocus()
        } else {
            digitInputs[index].clearFocus()
        }
    }

    private fun moveToPreviousInput(index: Int) {
        if (index > 0) {
            val previous = digitInputs[index - 1]
            previous.requestFocus()
            previous.setSelection(previous.text?.length ?: 0)
        } else {
            digitInputs[index].requestFocus()
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
