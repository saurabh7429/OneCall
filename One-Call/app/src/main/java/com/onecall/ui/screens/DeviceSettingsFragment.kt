package com.onecall.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.onecall.R
import com.onecall.data.settings.DeviceConfigEntity
import com.onecall.data.settings.SettingsRepository
import kotlinx.coroutines.launch
import java.util.Locale

class DeviceSettingsFragment : Fragment(R.layout.fragment_device_settings) {

    private lateinit var deviceId: String
    private lateinit var settingsRepository: SettingsRepository
    private var currentConfig: DeviceConfigEntity? = null
    
    private val iconTypes = arrayOf("PHONE", "TABLET", "FRIDGE", "TV", "OTHER")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = arguments?.getString(ARG_DEVICE_ID) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsRepository = SettingsRepository.getInstance(requireContext())

        val etNickname = view.findViewById<TextInputEditText>(R.id.et_nickname)
        val spinnerIcon = view.findViewById<Spinner>(R.id.spinner_icon)
        val switchRing = view.findViewById<SwitchCompat>(R.id.switch_ring)
        val switchAllowOutgoing = view.findViewById<SwitchCompat>(R.id.switch_allow_outgoing)
        val switchAutoApprove = view.findViewById<SwitchCompat>(R.id.switch_auto_approve)
        val sliderVolume = view.findViewById<Slider>(R.id.slider_volume)
        val btnDndStart = view.findViewById<MaterialButton>(R.id.btn_dnd_start)
        val btnDndEnd = view.findViewById<MaterialButton>(R.id.btn_dnd_end)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        val btnStopCalls = view.findViewById<MaterialButton>(R.id.btn_stop_calls)
        val btnDisconnect = view.findViewById<MaterialButton>(R.id.btn_disconnect)

        spinnerIcon.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, iconTypes)

        // Load config
        viewLifecycleOwner.lifecycleScope.launch {
            val config = settingsRepository.getDeviceConfig(deviceId)
            currentConfig = config
            
            etNickname.setText(config.nickname)
            spinnerIcon.setSelection(iconTypes.indexOf(config.deviceIcon).coerceAtLeast(0))
            switchRing.isChecked = config.ringOnDevice
            switchAllowOutgoing.isChecked = config.allowOutgoing
            switchAutoApprove.isChecked = config.autoApproveOutgoing
            sliderVolume.value = config.ringVolume.toFloat()
            btnDndStart.text = "Start: ${config.dndStartTime}"
            btnDndEnd.text = "End: ${config.dndEndTime}"
            
            updateStopCallsButton(btnStopCalls, config.isCallsPaused)
        }

        btnDndStart.setOnClickListener { showTimePicker(btnDndStart, "Start") }
        btnDndEnd.setOnClickListener { showTimePicker(btnDndEnd, "End") }

        btnStopCalls.setOnClickListener {
            val config = currentConfig ?: return@setOnClickListener
            val newPausedState = !config.isCallsPaused
            currentConfig = config.copy(isCallsPaused = newPausedState)
            updateStopCallsButton(btnStopCalls, newPausedState)
        }

        btnSave.setOnClickListener {
            val config = currentConfig?.copy(
                nickname = etNickname.text.toString(),
                deviceIcon = spinnerIcon.selectedItem.toString(),
                ringOnDevice = switchRing.isChecked,
                allowOutgoing = switchAllowOutgoing.isChecked,
                autoApproveOutgoing = switchAutoApprove.isChecked,
                ringVolume = sliderVolume.value.toInt(),
                dndStartTime = btnDndStart.text.toString().removePrefix("Start: "),
                dndEndTime = btnDndEnd.text.toString().removePrefix("End: ")
            ) ?: return@setOnClickListener

            viewLifecycleOwner.lifecycleScope.launch {
                settingsRepository.saveDeviceConfig(config)
                parentFragmentManager.popBackStack()
            }
        }
        
        btnDisconnect.setOnClickListener {
            com.onecall.network.socket.OneCallConnectionManager.disconnectDevice(deviceId)
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateStopCallsButton(btn: MaterialButton, isPaused: Boolean) {
        if (isPaused) {
            btn.text = "Resume Calls"
            btn.setTextColor(requireContext().getColor(R.color.onecall_success))
            btn.strokeColor = requireContext().getColorStateList(R.color.onecall_success)
        } else {
            btn.text = "Stop Calls"
            btn.setTextColor(requireContext().getColor(R.color.onecall_error))
            btn.strokeColor = requireContext().getColorStateList(R.color.onecall_error)
        }
    }

    private fun showTimePicker(button: MaterialButton, prefix: String) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select $prefix Time")
            .build()
            
        picker.addOnPositiveButtonClickListener {
            val time = String.format(Locale.getDefault(), "%02d:%02d", picker.hour, picker.minute)
            button.text = "$prefix: $time"
        }
        
        picker.show(childFragmentManager, "time_picker")
    }

    companion object {
        private const val ARG_DEVICE_ID = "device_id"
        
        fun newInstance(deviceId: String): DeviceSettingsFragment {
            val fragment = DeviceSettingsFragment()
            val args = Bundle()
            args.putString(ARG_DEVICE_ID, deviceId)
            fragment.arguments = args
            return fragment
        }
    }
}
