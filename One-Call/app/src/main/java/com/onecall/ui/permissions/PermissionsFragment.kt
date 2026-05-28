package com.onecall.ui.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onecall.R
import com.onecall.databinding.FragmentPermissionsBinding
import com.onecall.databinding.ItemPermissionBinding
import com.onecall.model.DeviceMode

data class PermissionItem(
    val permission: String,
    val nameRes: Int,
    val descRes: Int,
    val isRequired: Boolean
)

class PermissionsFragment : Fragment() {

    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var requiredAdapter: PermissionAdapter
    private lateinit var optionalAdapter: PermissionAdapter

    private val requiredPermissions = mutableListOf<PermissionItem>()
    private val optionalPermissions = mutableListOf<PermissionItem>()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.forEach { (permission, granted) ->
            if (!granted) {
                val isRequired = requiredPermissions.any { it.permission == permission }
                if (isRequired) {
                    Toast.makeText(requireContext(), getString(R.string.permission_required_warning), Toast.LENGTH_SHORT).show()
                }
            }
        }
        updatePermissionStates()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildPermissionLists()

        requiredAdapter = PermissionAdapter(requiredPermissions, true) { permission ->
            permissionLauncher.launch(arrayOf(permission))
        }
        optionalAdapter = PermissionAdapter(optionalPermissions, false) { permission ->
            permissionLauncher.launch(arrayOf(permission))
        }

        binding.rvRequiredPermissions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requiredAdapter
        }
        binding.rvOptionalPermissions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = optionalAdapter
        }

        binding.btnGrantAll.setOnClickListener {
            val allPerms = (requiredPermissions + optionalPermissions)
                .map { it.permission }
                .filter { !isGranted(it) }
                .toTypedArray()
            if (allPerms.isNotEmpty()) {
                permissionLauncher.launch(allPerms)
            } else {
                navigateNext()
            }
        }

        binding.btnContinue.setOnClickListener {
            navigateNext()
        }
    }

    private fun buildPermissionLists() {
        requiredPermissions.clear()
        optionalPermissions.clear()

        // Required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(PermissionItem(Manifest.permission.BLUETOOTH_CONNECT, R.string.permission_nearby, R.string.permission_nearby_desc, true))
            requiredPermissions.add(PermissionItem(Manifest.permission.BLUETOOTH_SCAN, R.string.permission_nearby, R.string.permission_nearby_desc, true))
        } else {
            requiredPermissions.add(PermissionItem(Manifest.permission.BLUETOOTH, R.string.permission_bluetooth, R.string.permission_bluetooth_desc, true))
        }
        requiredPermissions.add(PermissionItem(Manifest.permission.READ_PHONE_STATE, R.string.permission_phone, R.string.permission_phone_desc, true))
        requiredPermissions.add(PermissionItem(Manifest.permission.RECORD_AUDIO, R.string.permission_microphone, R.string.permission_microphone_desc, true))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requiredPermissions.add(PermissionItem(Manifest.permission.ANSWER_PHONE_CALLS, R.string.permission_answer_calls, R.string.permission_answer_calls_desc, true))
        }
        requiredPermissions.add(PermissionItem(Manifest.permission.CALL_PHONE, R.string.permission_call_phone, R.string.permission_call_phone_desc, true))

        // Optional
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            optionalPermissions.add(PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_location, R.string.permission_location_desc, false))
        }
        optionalPermissions.add(PermissionItem(Manifest.permission.READ_CONTACTS, R.string.permission_contacts, R.string.permission_contacts_desc, false))
        optionalPermissions.add(PermissionItem(Manifest.permission.READ_CALL_LOG, R.string.permission_call_log, R.string.permission_call_log_desc, false))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            optionalPermissions.add(PermissionItem(Manifest.permission.POST_NOTIFICATIONS, R.string.permission_notifications, R.string.permission_notifications_desc, false))
        }
    }

    private fun isGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

    private fun updatePermissionStates() {
        requiredAdapter.notifyDataSetChanged()
        optionalAdapter.notifyDataSetChanged()
    }

    private fun navigateNext() {
        val modeArg = arguments?.getString("deviceMode") ?: DeviceMode.MAIN.name
        val mode = DeviceMode.fromString(modeArg) ?: DeviceMode.MAIN
        if (mode == DeviceMode.MAIN) {
            findNavController().navigate(R.id.action_permissions_to_main_setup)
        } else {
            findNavController().navigate(R.id.action_permissions_to_secondary_setup)
        }
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PermissionAdapter(
    private val items: List<PermissionItem>,
    private val required: Boolean,
    private val onGrant: (String) -> Unit
) : RecyclerView.Adapter<PermissionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPermissionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val context = holder.binding.root.context
        val isGranted = ContextCompat.checkSelfPermission(context, item.permission) == PackageManager.PERMISSION_GRANTED

        holder.binding.tvPermissionName.setText(item.nameRes)
        holder.binding.tvPermissionDesc.setText(item.descRes)

        if (isGranted) {
            holder.binding.btnGrant.visibility = View.GONE
            holder.binding.ivGranted.visibility = View.VISIBLE
            holder.binding.tvPermissionStatus.visibility = View.GONE
        } else {
            holder.binding.btnGrant.visibility = View.VISIBLE
            holder.binding.ivGranted.visibility = View.GONE
            if (required) {
                holder.binding.tvPermissionStatus.visibility = View.VISIBLE
                holder.binding.tvPermissionStatus.setText(R.string.permission_required_warning)
            } else {
                holder.binding.tvPermissionStatus.visibility = View.VISIBLE
                holder.binding.tvPermissionStatus.setText(R.string.permission_optional_info)
                holder.binding.tvPermissionStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.text_hint)
                )
            }
            holder.binding.btnGrant.setOnClickListener { onGrant(item.permission) }
        }
    }

    override fun getItemCount() = items.size
}
