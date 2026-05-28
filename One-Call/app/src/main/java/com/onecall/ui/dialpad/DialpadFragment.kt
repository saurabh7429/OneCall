package com.onecall.ui.dialpad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.onecall.databinding.FragmentDialpadBinding
import com.onecall.model.DeviceMode
import com.onecall.service.OneCallService

class DialpadFragment : Fragment() {

    private var _binding: FragmentDialpadBinding? = null
    private val binding get() = _binding!!
    private val numberBuilder = StringBuilder()

    private val outgoingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == OneCallService.BROADCAST_OUTGOING_REQUEST) {
                val approved = intent.getBooleanExtra(OneCallService.EXTRA_APPROVED, false)
                if (!approved) {
                    Toast.makeText(requireContext(), "Call blocked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDialpadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().registerReceiver(outgoingReceiver, IntentFilter(OneCallService.BROADCAST_OUTGOING_REQUEST))

        val dialButtons = mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9", binding.btnStar to "*", binding.btnHash to "#"
        )

        dialButtons.forEach { (btn, digit) ->
            btn.setOnClickListener {
                numberBuilder.append(digit)
                binding.tvNumber.text = numberBuilder.toString()
            }
        }

        binding.btnBackspace.setOnClickListener {
            if (numberBuilder.isNotEmpty()) {
                numberBuilder.deleteCharAt(numberBuilder.length - 1)
                binding.tvNumber.text = numberBuilder.toString()
            }
        }

        binding.btnBackspace.setOnLongClickListener {
            numberBuilder.clear()
            binding.tvNumber.text = ""
            true
        }

        binding.btnCall.setOnClickListener {
            val number = numberBuilder.toString()
            if (number.isNotBlank()) {
                initiateCall(number)
            } else {
                Toast.makeText(requireContext(), "Enter a number first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initiateCall(number: String) {
        val service = OneCallService.instance
        if (service != null) {
            if (service.currentMode == DeviceMode.SECONDARY) {
                // Request from secondary — show confirmation on main
                showOutgoingRequestConfirmation(number)
                service.requestOutgoingCall(number)
            } else {
                service.requestOutgoingCall(number)
            }
        }
    }

    private fun showOutgoingRequestConfirmation(number: String) {
        Toast.makeText(requireContext(), "Requesting call to $number…", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireActivity().unregisterReceiver(outgoingReceiver) } catch (e: Exception) {}
        _binding = null
    }
}
