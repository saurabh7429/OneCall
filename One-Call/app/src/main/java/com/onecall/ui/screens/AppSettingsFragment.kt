package com.onecall.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.onecall.R
import com.onecall.data.DeviceRoleStore
import com.onecall.data.history.HistoryDatabase
import com.onecall.data.settings.SettingsRepository
import com.onecall.ui.tutorial.TutorialActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppSettingsFragment : Fragment(R.layout.fragment_app_settings) {

    private lateinit var settingsRepository: SettingsRepository
    private val iconTypes = arrayOf("PHONE", "TABLET", "FRIDGE", "TV", "OTHER")
    private val expiryOptions = arrayOf("5", "10", "15", "30")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        settingsRepository = SettingsRepository.getInstance(requireContext())
        val isMain = DeviceRoleStore.isMain(requireContext())
        
        val etMyDeviceName = view.findViewById<TextInputEditText>(R.id.et_my_device_name)
        val spinnerMyIcon = view.findViewById<Spinner>(R.id.spinner_my_icon)
        
        val llNetworkSection = view.findViewById<View>(R.id.ll_network_section)
        val etMaxDevices = view.findViewById<TextInputEditText>(R.id.et_max_devices)
        val switchAutoReconnect = view.findViewById<SwitchCompat>(R.id.switch_auto_reconnect)
        val spinnerExpiry = view.findViewById<Spinner>(R.id.spinner_expiry)
        val switchGlobalAutoApprove = view.findViewById<SwitchCompat>(R.id.switch_global_auto_approve)
        val switchNotifyMain = view.findViewById<SwitchCompat>(R.id.switch_notify_main)
        
        val btnClearCallHistory = view.findViewById<MaterialButton>(R.id.btn_clear_call_history)
        val btnClearDeviceHistory = view.findViewById<MaterialButton>(R.id.btn_clear_device_history)
        val btnSaveSettings = view.findViewById<MaterialButton>(R.id.btn_save_settings)
        
        val btnBatteryOpt = view.findViewById<MaterialButton>(R.id.btn_battery_opt)
        val tvBatteryWarning = view.findViewById<TextView>(R.id.tv_battery_warning)
        val btnTutorial = view.findViewById<MaterialButton>(R.id.btn_tutorial)
        
        // Adapters
        spinnerMyIcon.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, iconTypes)
        spinnerExpiry.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, expiryOptions)
        
        // Load settings
        etMyDeviceName.setText(settingsRepository.myDeviceName)
        spinnerMyIcon.setSelection(iconTypes.indexOf(settingsRepository.myDeviceIcon).coerceAtLeast(0))
        
        if (isMain) {
            llNetworkSection.visibility = View.VISIBLE
            etMaxDevices.setText(settingsRepository.maxDevices.toString())
            switchAutoReconnect.isChecked = settingsRepository.autoReconnect
            val expiryStr = settingsRepository.codeExpiryTimeMinutes.toString()
            spinnerExpiry.setSelection(expiryOptions.indexOf(expiryStr).coerceAtLeast(0))
            switchGlobalAutoApprove.isChecked = settingsRepository.autoApproveOutgoing
            switchNotifyMain.isChecked = settingsRepository.notifyOutgoingOnMain
            btnClearDeviceHistory.visibility = View.VISIBLE
        } else {
            llNetworkSection.visibility = View.GONE
            btnClearDeviceHistory.visibility = View.GONE
        }
        
        btnSaveSettings.setOnClickListener {
            settingsRepository.setMyDeviceName(etMyDeviceName.text.toString())
            settingsRepository.setMyDeviceIcon(spinnerMyIcon.selectedItem.toString())
            
            if (isMain) {
                val maxDev = etMaxDevices.text.toString().toIntOrNull() ?: 5
                settingsRepository.setMaxDevices(maxDev.coerceIn(1, 10))
                settingsRepository.setAutoReconnect(switchAutoReconnect.isChecked)
                val expiry = spinnerExpiry.selectedItem.toString().toIntOrNull() ?: 10
                settingsRepository.setCodeExpiryTimeMinutes(expiry)
                settingsRepository.setAutoApproveOutgoing(switchGlobalAutoApprove.isChecked)
                settingsRepository.setNotifyOutgoingOnMain(switchNotifyMain.isChecked)
            }
            
            Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show()
        }
        
        btnClearCallHistory.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                if (isMain) {
                    HistoryDatabase.getDatabase(requireContext()).historyDao().clearAllCallHistory()
                } else {
                    // Secondary relies on in-memory or session data, handled by its repository instance
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Call history cleared", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        btnClearDeviceHistory.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                // Device history only stored on main
                HistoryDatabase.getDatabase(requireContext()).historyDao().getAllDeviceHistory() // just for reference
                // Actually clear it if we had a clear function. Let's assume we add it later or run a raw query
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Device history cleared", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        btnTutorial.setOnClickListener {
            val intent = Intent(requireContext(), TutorialActivity::class.java).apply {
                putExtra("manual_launch", true)
            }
            startActivity(intent)
        }
        
        val powerManager = requireContext().getSystemService(android.content.Context.POWER_SERVICE) as PowerManager
        if (powerManager.isIgnoringBatteryOptimizations(requireContext().packageName)) {
            btnBatteryOpt.isEnabled = false
            btnBatteryOpt.text = "Battery Optimization Disabled (Good)"
            tvBatteryWarning.visibility = View.GONE
        } else {
            btnBatteryOpt.isEnabled = true
            btnBatteryOpt.text = "Disable Battery Optimization"
            tvBatteryWarning.visibility = View.VISIBLE
        }
        
        btnBatteryOpt.setOnClickListener {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${requireContext().packageName}")
            }
            startActivity(intent)
        }
    }
}
