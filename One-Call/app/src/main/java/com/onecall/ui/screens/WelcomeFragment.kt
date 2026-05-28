package com.onecall.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.onecall.data.DeviceRoleStore
import com.onecall.R

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.mainDeviceSetupButton).setOnClickListener {
            DeviceRoleStore.setRole(requireContext(), DeviceRoleStore.ROLE_MAIN)
            findNavController().navigate(R.id.action_welcomeFragment_to_mainSetupFragment)
        }

        view.findViewById<MaterialButton>(R.id.secondaryDeviceSetupButton).setOnClickListener {
            DeviceRoleStore.setRole(requireContext(), DeviceRoleStore.ROLE_SECONDARY)
            findNavController().navigate(R.id.action_welcomeFragment_to_secondarySetupFragment)
        }
    }
}
