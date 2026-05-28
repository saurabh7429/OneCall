package com.onecall.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import com.onecall.data.settings.SettingsRepository
import com.onecall.network.socket.OneCallConnectionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeDashboardFragment : Fragment(R.layout.fragment_home_dashboard) {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var adapter: ConnectedDevicesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        settingsRepository = SettingsRepository.getInstance(requireContext())
        
        val tvHeading = view.findViewById<TextView>(R.id.tv_connected_devices_heading)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_connected_devices)

        adapter = ConnectedDevicesAdapter(
            onSettingsClicked = { deviceId ->
                // Navigate to per-device settings
                val fragment = DeviceSettingsFragment.newInstance(deviceId)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onStopCallsClicked = { deviceId, isPaused ->
                lifecycleScope.launch {
                    val config = settingsRepository.getDeviceConfig(deviceId)
                    settingsRepository.saveDeviceConfig(config.copy(isCallsPaused = !isPaused))
                }
            },
            onDisconnectClicked = { deviceId ->
                OneCallConnectionManager.disconnectDevice(deviceId)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        OneCallConnectionManager.connectedDevices.observe(viewLifecycleOwner) { devices ->
            val max = settingsRepository.maxDevices
            tvHeading.text = "Connected Devices (${devices.size}/$max)"
            
            val emptyView = view.findViewById<TextView>(R.id.tv_empty_devices)
            emptyView.visibility = if (devices.isEmpty()) View.VISIBLE else View.GONE
            
            adapter.submitList(devices)
        }
    }
}
