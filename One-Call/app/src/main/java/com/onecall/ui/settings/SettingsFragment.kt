package com.onecall.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.data.db.OneCallDatabase
import com.onecall.data.repository.CallHistoryRepository
import com.onecall.databinding.FragmentSettingsBinding
import com.onecall.model.DeviceMode
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var devicePrefs: BluetoothDevicePreference
    private lateinit var repository: CallHistoryRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicePrefs = BluetoothDevicePreference(requireContext())
        repository = CallHistoryRepository(OneCallDatabase.getDatabase(requireContext()).callHistoryDao())

        // Show BT section only for main device
        if (devicePrefs.getDeviceMode() == DeviceMode.MAIN) {
            binding.labelBtSection.visibility = View.VISIBLE
            binding.cardBtSection.visibility = View.VISIBLE
            binding.tvPairedDevice.text = devicePrefs.getPairedDeviceName() ?: "None"
        }

        binding.tvNicknameValue.text = devicePrefs.getDeviceNickname()
        binding.switchAutoReconnect.isChecked = devicePrefs.getAutoReconnect()
        binding.switchAutoApprove.isChecked = devicePrefs.getAutoApproveOutgoing()

        binding.itemNickname.setOnClickListener {
            showNicknameDialog()
        }

        binding.btnForgetPaired.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Forget Device")
                .setMessage("Are you sure you want to unpair the device?")
                .setPositiveButton("Forget") { _, _ ->
                    devicePrefs.clearPairedDevice()
                    binding.tvPairedDevice.text = "None"
                    Toast.makeText(requireContext(), "Device forgotten", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.switchAutoReconnect.setOnCheckedChangeListener { _, checked ->
            devicePrefs.setAutoReconnect(checked)
        }
        binding.switchAutoApprove.setOnCheckedChangeListener { _, checked ->
            devicePrefs.setAutoApproveOutgoing(checked)
        }

        binding.itemClearHistory.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.clear_call_history))
                .setMessage("All call history will be deleted. Are you sure?")
                .setPositiveButton("Clear") { _, _ ->
                    lifecycleScope.launch {
                        repository.clearPermanentHistory()
                        Toast.makeText(requireContext(), getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.itemTutorial.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_tutorial)
        }

        binding.itemPermissions.setOnClickListener {
            val bundle = android.os.Bundle().apply {
                putString("deviceMode", devicePrefs.getDeviceMode()?.name ?: DeviceMode.MAIN.name)
            }
            findNavController().navigate(R.id.action_settings_to_permissions, bundle)
        }

        binding.itemPrivacy.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_privacy)
        }
    }

    private fun showNicknameDialog() {
        val editText = android.widget.EditText(requireContext()).apply {
            setText(devicePrefs.getDeviceNickname())
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.device_nickname))
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val nickname = editText.text.toString().trim()
                if (nickname.isNotBlank()) {
                    devicePrefs.setDeviceNickname(nickname)
                    binding.tvNicknameValue.text = nickname
                    Toast.makeText(requireContext(), getString(R.string.nickname_updated), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
