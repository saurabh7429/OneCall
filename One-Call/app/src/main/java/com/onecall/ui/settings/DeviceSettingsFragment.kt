package com.onecall.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onecall.core.BluetoothDevicePreference
import com.onecall.databinding.FragmentDeviceSettingsBinding

class DeviceSettingsFragment : Fragment() {

    private var _binding: FragmentDeviceSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var devicePrefs: BluetoothDevicePreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDeviceSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicePrefs = BluetoothDevicePreference(requireContext())

        binding.switchRing.isChecked = devicePrefs.isRingEnabled()
        binding.switchAllowOutgoing.isChecked = devicePrefs.isAllowOutgoing()
        binding.switchAutoApprove.isChecked = devicePrefs.getAutoApproveOutgoing()

        binding.switchRing.setOnCheckedChangeListener { _, c -> devicePrefs.setRingEnabled(c) }
        binding.switchAllowOutgoing.setOnCheckedChangeListener { _, c -> devicePrefs.setAllowOutgoing(c) }
        binding.switchAutoApprove.setOnCheckedChangeListener { _, c -> devicePrefs.setAutoApproveOutgoing(c) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
