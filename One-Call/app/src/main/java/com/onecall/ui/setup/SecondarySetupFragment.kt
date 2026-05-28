package com.onecall.ui.setup

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.core.BluetoothManager
import com.onecall.databinding.FragmentSecondarySetupBinding
import com.onecall.databinding.ItemDeviceBinding

class SecondarySetupFragment : Fragment() {

    private var _binding: FragmentSecondarySetupBinding? = null
    private val binding get() = _binding!!

    private lateinit var btManager: BluetoothManager
    private lateinit var devicePrefs: BluetoothDevicePreference
    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private lateinit var deviceAdapter: DeviceAdapter

    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = if (android.os.Build.VERSION.SDK_INT >= 33) {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    device?.let {
                        if (!discoveredDevices.contains(it)) {
                            discoveredDevices.add(it)
                            deviceAdapter.notifyItemInserted(discoveredDevices.size - 1)
                        }
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device = if (android.os.Build.VERSION.SDK_INT >= 33) {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    if (state == BluetoothDevice.BOND_BONDED && device != null) {
                        val name = try { device.name ?: device.address } catch (e: SecurityException) { device.address }
                        devicePrefs.savePairedDeviceAddress(device.address, name)
                        Toast.makeText(requireContext(), "Connected to $name", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_secondary_setup_to_dashboard)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecondarySetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btManager = BluetoothManager(requireContext())
        devicePrefs = BluetoothDevicePreference(requireContext())

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        requireActivity().registerReceiver(discoveryReceiver, filter)

        deviceAdapter = DeviceAdapter(discoveredDevices) { device ->
            btManager.stopDiscovery()
            try {
                device.createBond()
            } catch (e: SecurityException) {
                Toast.makeText(requireContext(), "Permission needed to pair", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvDevices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deviceAdapter
        }

        binding.btnScan.setOnClickListener {
            startScan()
        }

        // Check if already paired
        val pairedAddress = devicePrefs.getPairedDeviceAddress()
        if (pairedAddress != null) {
            binding.tvAutoConnecting.visibility = View.VISIBLE
        }

        // Pre-populate bonded devices
        btManager.getBondedDevices().forEach { device ->
            if (!discoveredDevices.contains(device)) {
                discoveredDevices.add(device)
            }
        }
        deviceAdapter.notifyDataSetChanged()
    }

    private fun startScan() {
        if (!btManager.isBluetoothEnabled()) {
            btManager.enableBluetooth()
            return
        }
        discoveredDevices.clear()
        // Re-add bonded
        btManager.getBondedDevices().forEach {
            discoveredDevices.add(it)
        }
        deviceAdapter.notifyDataSetChanged()
        btManager.startDiscovery()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireActivity().unregisterReceiver(discoveryReceiver) } catch (e: Exception) {}
        btManager.stopDiscovery()
        _binding = null
    }
}

class DeviceAdapter(
    private val devices: List<BluetoothDevice>,
    private val onConnect: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        try {
            holder.binding.tvDeviceName.text = device.name ?: "Unknown Device"
            holder.binding.tvDeviceAddress.text = device.address
        } catch (e: SecurityException) {
            holder.binding.tvDeviceName.text = "Device"
            holder.binding.tvDeviceAddress.text = device.address
        }
        holder.binding.btnConnect.setOnClickListener { onConnect(device) }
    }

    override fun getItemCount() = devices.size
}
