package com.onecall.ui.dashboard

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.core.RfcommSignalingService
import com.onecall.databinding.FragmentSecondaryDashboardBinding
import com.onecall.model.ConnectionState
import com.onecall.service.OneCallService

class SecondaryDashboardFragment : Fragment() {

    private var _binding: FragmentSecondaryDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var devicePrefs: BluetoothDevicePreference

    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == OneCallService.BROADCAST_CONNECTION_CHANGED) {
                val state = intent.getStringExtra(OneCallService.EXTRA_CONNECTION_STATE)
                val deviceName = intent.getStringExtra(OneCallService.EXTRA_DEVICE_NAME)
                updateConnectionUI(state, deviceName)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecondaryDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicePrefs = BluetoothDevicePreference(requireContext())

        requireActivity().registerReceiver(
            connectionReceiver,
            IntentFilter(OneCallService.BROADCAST_CONNECTION_CHANGED)
        )

        // Refresh UI from currently running service
        val service = OneCallService.instance
        if (service != null) {
            updateConnectionUI(service.connectionState.name, service.connectedDeviceName)
        } else {
            updateConnectionUI(ConnectionState.DISCONNECTED.name, null)
        }

        // Open Dialpad
        binding.btnOpenDialpad.setOnClickListener {
            findNavController().navigate(R.id.dialpadFragment)
        }

        // Reconnect: restart service which will auto-connect to saved paired device
        binding.btnReconnect.setOnClickListener {
            val pairedAddress = devicePrefs.getPairedDeviceAddress()
            if (pairedAddress == null) {
                // No paired device saved — go to setup to scan
                findNavController().navigate(R.id.action_secondary_dashboard_to_secondary_setup)
                return@setOnClickListener
            }
            // Restart the OneCallService to trigger RFCOMM reconnect
            val intent = Intent(requireContext(), OneCallService::class.java)
            try {
                requireActivity().startForegroundService(intent)
            } catch (e: Exception) {
                requireActivity().startService(intent)
            }
            binding.tvConnectionState.text = getString(R.string.state_connecting)
            binding.btnReconnect.isEnabled = false
            binding.btnReconnect.postDelayed({ binding.btnReconnect.isEnabled = true }, 3000)
        }

        // Go to setup screen to scan/pair again
        binding.btnGoSetup.setOnClickListener {
            findNavController().navigate(R.id.action_secondary_dashboard_to_secondary_setup)
        }
    }

    private fun updateConnectionUI(stateName: String?, deviceName: String?) {
        val state = try { ConnectionState.valueOf(stateName ?: "") } catch (e: Exception) { ConnectionState.DISCONNECTED }
        when (state) {
            ConnectionState.CONNECTED -> {
                binding.statusDot.setBackgroundResource(R.drawable.ic_connected)
                binding.tvConnectionState.setText(R.string.state_connected)
                binding.tvConnectedTo.text = getString(R.string.connected_to, deviceName ?: "Main Device")
                binding.btnReconnect.visibility = View.GONE
                binding.btnGoSetup.visibility = View.GONE
            }
            ConnectionState.DISCONNECTED -> {
                binding.statusDot.setBackgroundResource(R.drawable.ic_disconnected)
                binding.tvConnectionState.setText(R.string.state_disconnected)
                val name = devicePrefs.getPairedDeviceName()
                binding.tvConnectedTo.text = if (name != null) "Last: $name" else "Not Connected"
                binding.btnReconnect.visibility = View.VISIBLE
                binding.btnGoSetup.visibility = View.VISIBLE
            }
            ConnectionState.CONNECTING -> {
                binding.tvConnectionState.setText(R.string.state_connecting)
                binding.tvConnectedTo.text = "Connecting…"
                binding.btnReconnect.visibility = View.GONE
                binding.btnGoSetup.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireActivity().unregisterReceiver(connectionReceiver) } catch (e: Exception) {}
        _binding = null
    }
}
