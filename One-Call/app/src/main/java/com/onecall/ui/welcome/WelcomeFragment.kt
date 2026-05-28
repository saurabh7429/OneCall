package com.onecall.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.databinding.FragmentWelcomeBinding
import com.onecall.model.DeviceMode

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var devicePrefs: BluetoothDevicePreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicePrefs = BluetoothDevicePreference(requireContext())

        // If already configured, go to dashboard
        val existingMode = devicePrefs.getDeviceMode()
        if (existingMode != null) {
            navigateToDashboard(existingMode)
            return
        }

        binding.btnMainDevice.setOnClickListener {
            devicePrefs.setDeviceMode(DeviceMode.MAIN)
            val bundle = android.os.Bundle().apply {
                putString("deviceMode", DeviceMode.MAIN.name)
            }
            findNavController().navigate(R.id.action_welcome_to_permissions, bundle)
        }

        binding.btnSecondaryDevice.setOnClickListener {
            devicePrefs.setDeviceMode(DeviceMode.SECONDARY)
            val bundle = android.os.Bundle().apply {
                putString("deviceMode", DeviceMode.SECONDARY.name)
            }
            findNavController().navigate(R.id.action_welcome_to_permissions, bundle)
        }

        binding.btnTutorial.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_tutorial)
        }
    }

    private fun navigateToDashboard(mode: DeviceMode) {
        val destination = if (mode == DeviceMode.MAIN) {
            R.id.action_welcome_to_main_dashboard
        } else {
            R.id.action_welcome_to_secondary_dashboard
        }
        findNavController().navigate(destination)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
