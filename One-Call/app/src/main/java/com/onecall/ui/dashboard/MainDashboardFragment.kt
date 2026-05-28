package com.onecall.ui.dashboard

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
import com.onecall.databinding.FragmentMainDashboardBinding
import com.onecall.model.ConnectionState
import com.onecall.service.OneCallService

class MainDashboardFragment : Fragment() {

    private var _binding: FragmentMainDashboardBinding? = null
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
        _binding = FragmentMainDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicePrefs = BluetoothDevicePreference(requireContext())

        requireActivity().registerReceiver(
            connectionReceiver,
            IntentFilter(OneCallService.BROADCAST_CONNECTION_CHANGED)
        )

        // Refresh from running service
        val service = OneCallService.instance
        if (service != null) {
            updateConnectionUI(service.connectionState.name, service.connectedDeviceName)
        } else {
            updateConnectionUI(ConnectionState.DISCONNECTED.name, null)
        }

        binding.btnDisconnect.setOnClickListener {
            // Ask service to disconnect HFP
        }

        binding.btnStopCalls.setOnClickListener {
            val stopped = devicePrefs.isCallsStopped()
            devicePrefs.setCallsStopped(!stopped)
            binding.btnStopCalls.text = if (!stopped) "Resume Calls" else getString(R.string.btn_stop_calls)
        }

        binding.btnReconnect.setOnClickListener {
            val intent = Intent(requireContext(), OneCallService::class.java)
            requireActivity().startForegroundService(intent)
        }

        binding.btnOpenDialpad.setOnClickListener {
            findNavController().navigate(R.id.dialpadFragment)
        }
    }

    private fun updateConnectionUI(stateName: String?, deviceName: String?) {
        val state = try { ConnectionState.valueOf(stateName ?: "") } catch (e: Exception) { ConnectionState.DISCONNECTED }
        when (state) {
            ConnectionState.CONNECTED -> {
                binding.statusDot.setBackgroundResource(R.drawable.ic_connected)
                binding.tvConnectionState.setText(R.string.state_connected)
                binding.tvDeviceName.text = deviceName ?: devicePrefs.getPairedDeviceName() ?: "Unknown Device"
                binding.btnDisconnect.visibility = View.VISIBLE
                binding.btnReconnect.visibility = View.GONE
            }
            ConnectionState.DISCONNECTED -> {
                binding.statusDot.setBackgroundResource(R.drawable.ic_disconnected)
                binding.tvConnectionState.setText(R.string.state_disconnected)
                binding.tvDeviceName.text = devicePrefs.getPairedDeviceName() ?: "No device paired"
                binding.btnDisconnect.visibility = View.GONE
                binding.btnReconnect.visibility = View.VISIBLE
            }
            ConnectionState.CONNECTING -> {
                binding.tvConnectionState.setText(R.string.state_connecting)
                binding.btnReconnect.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireActivity().unregisterReceiver(connectionReceiver) } catch (e: Exception) {}
        _binding = null
    }
}
