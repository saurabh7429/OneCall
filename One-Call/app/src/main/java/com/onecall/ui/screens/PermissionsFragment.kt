package com.onecall.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onecall.R

class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private data class PermissionSpec(
        val permission: String,
        val title: String,
        val description: String,
        val required: Boolean,
        val requestable: Boolean,
        val optionalDeniedHint: String? = null,
        val statusChecker: (Context) -> Boolean,
    )

    private data class PermissionRowState(
        val spec: PermissionSpec,
        val card: MaterialCardView,
        val statusText: TextView,
        val optionalHintText: TextView,
    )

    private val requiredPermissionSpecs = listOf(
        PermissionSpec(
            permission = Manifest.permission.READ_PHONE_STATE,
            title = "Read phone state",
            description = "Incoming call aur device state detect karne ke liye",
            required = true,
            requestable = true,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.RECORD_AUDIO,
            title = "Record audio",
            description = "Voice capture aur call audio ke liye",
            required = true,
            requestable = true,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.ANSWER_PHONE_CALLS,
            title = "Answer phone calls",
            description = "Main device par incoming call receive karne ke liye",
            required = true,
            requestable = true,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.CALL_PHONE,
            title = "Call phone",
            description = "Outbound call trigger karne ke liye",
            required = true,
            requestable = true,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.MANAGE_OWN_CALLS,
            title = "Manage own calls",
            description = "System call routing ke liye",
            required = true,
            requestable = false,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_OWN_CALLS) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.FOREGROUND_SERVICE,
            title = "Foreground service",
            description = "Background calling service ko stable rakhne ke liye",
            required = true,
            requestable = false,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.INTERNET,
            title = "Internet",
            description = "Local Wi-Fi signalling aur device sync ke liye",
            required = true,
            requestable = false,
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
            },
        ),
    )

    private val optionalPermissionSpecs = listOf(
        PermissionSpec(
            permission = Manifest.permission.READ_CONTACTS,
            title = "Read contacts",
            description = "Caller naam match karne aur contact lookup ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Contacts denied: Caller naam nahi dikhega, sirf number",
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.READ_CALL_LOG,
            title = "Read call log",
            description = "Recent call history read karne ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Call log denied: Recent call history sync nahi hogi",
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.WRITE_CALL_LOG,
            title = "Write call log",
            description = "Call history save aur update karne ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Call log write denied: Call history save nahi hogi",
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            title = "Post notifications",
            description = "Incoming call aur setup alerts dikhane ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Notifications denied: Incoming call alerts miss ho sakte hain",
            statusChecker = { context ->
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.WAKE_LOCK,
            title = "Wake lock",
            description = "Incoming call pe screen wake rakhne ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Wake lock denied: Screen wake stability kam ho sakti hai",
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.RECEIVE_BOOT_COMPLETED,
            title = "Receive boot completed",
            description = "Reboot ke baad service auto-start karne ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Boot completed denied: Reboot ke baad service auto-start nahi hogi",
            statusChecker = { context ->
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED
            },
        ),
        PermissionSpec(
            permission = Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            title = "Ignore battery optimizations",
            description = "Battery saver se background service ko bachane ke liye",
            required = false,
            requestable = false,
            optionalDeniedHint = "Battery optimization deny ho to background service ko Android kill kar sakta hai",
            statusChecker = { context ->
                context.isIgnoringBatteryOptimizations()
            },
        ),
    )

    private val permissionRowStates = mutableListOf<PermissionRowState>()

    private var warningBannerCard: MaterialCardView? = null
    private var warningBannerText: TextView? = null
    private var requiredPermissionsContainer: LinearLayout? = null
    private var optionalPermissionsContainer: LinearLayout? = null

    private val requiredPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        refreshPermissionState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        warningBannerCard = view.findViewById(R.id.permissionsWarningBanner)
        warningBannerText = view.findViewById(R.id.permissionsWarningText)
        requiredPermissionsContainer = view.findViewById(R.id.requiredPermissionsContainer)
        optionalPermissionsContainer = view.findViewById(R.id.optionalPermissionsContainer)

        view.findViewById<MaterialButton>(R.id.grantRequiredPermissionsButton).setOnClickListener {
            requestRequiredPermissions()
        }

        view.findViewById<MaterialButton>(R.id.skipPermissionsButton).setOnClickListener {
            showSkipWarningDialog()
        }

        buildPermissionRows()
        refreshPermissionState()
    }

    override fun onResume() {
        super.onResume()
        refreshPermissionState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        permissionRowStates.clear()
        warningBannerCard = null
        warningBannerText = null
        requiredPermissionsContainer = null
        optionalPermissionsContainer = null
    }

    private fun buildPermissionRows() {
        permissionRowStates.clear()
        requiredPermissionsContainer?.removeAllViews()
        optionalPermissionsContainer?.removeAllViews()

        requiredPermissionSpecs.forEach { addPermissionRow(it, requiredPermissionsContainer) }
        optionalPermissionSpecs.forEach { addPermissionRow(it, optionalPermissionsContainer) }
    }

    private fun addPermissionRow(spec: PermissionSpec, container: LinearLayout?) {
        val parent = container ?: return
        val rowView = LayoutInflater.from(requireContext()).inflate(R.layout.item_permission_row, parent, false)
        val card = rowView.findViewById<MaterialCardView>(R.id.permissionRowCard)
        val statusText = rowView.findViewById<TextView>(R.id.permissionStatusText)
        val optionalHintText = rowView.findViewById<TextView>(R.id.permissionOptionalHintText)

        rowView.findViewById<TextView>(R.id.permissionNameText).text = spec.title
        rowView.findViewById<TextView>(R.id.permissionDescriptionText).text = spec.description

        card.setOnClickListener { openAppSettings() }

        parent.addView(rowView)
        permissionRowStates.add(
            PermissionRowState(
                spec = spec,
                card = card,
                statusText = statusText,
                optionalHintText = optionalHintText,
            ),
        )
    }

    private fun refreshPermissionState() {
        val bannerCard = warningBannerCard ?: return
        val bannerText = warningBannerText ?: return

        var missingRequiredCount = 0
        permissionRowStates.forEach { rowState ->
            val granted = rowState.spec.statusChecker(requireContext())
            if (rowState.spec.required && !granted) {
                missingRequiredCount += 1
            }
            bindPermissionRow(rowState, granted)
        }

        if (missingRequiredCount > 0) {
            bannerCard.visibility = View.VISIBLE
            bannerText.text = getString(R.string.required_permissions_missing_warning, missingRequiredCount)
        } else {
            bannerCard.visibility = View.GONE
        }
    }

    private fun bindPermissionRow(rowState: PermissionRowState, granted: Boolean) {
        val context = requireContext()
        val grantedColor = ContextCompat.getColor(context, R.color.onecall_success)
        val deniedColor = ContextCompat.getColor(context, R.color.onecall_error)
        val strokeColor = if (granted) grantedColor else deniedColor

        rowState.card.setStrokeColor(strokeColor)
        rowState.statusText.setTextColor(strokeColor)
        rowState.statusText.text = if (granted) {
            "✓ ${getString(R.string.permission_granted)}"
        } else {
            "✕ ${getString(R.string.permission_denied)}"
        }

        if (rowState.spec.required) {
            rowState.optionalHintText.visibility = View.GONE
            rowState.optionalHintText.text = null
        } else if (granted) {
            rowState.optionalHintText.visibility = View.GONE
            rowState.optionalHintText.text = null
        } else {
            rowState.optionalHintText.visibility = View.VISIBLE
            rowState.optionalHintText.text = rowState.spec.optionalDeniedHint
        }
    }

    private fun requestRequiredPermissions() {
        val requestablePermissions = requiredPermissionSpecs
            .filter { it.requestable }
            .map { it.permission }
            .toTypedArray()

        if (requestablePermissions.isEmpty()) {
            refreshPermissionState()
            return
        }

        requiredPermissionsLauncher.launch(requestablePermissions)
    }

    private fun showSkipWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.permissions_screen_title)
            .setMessage(R.string.permissions_skip_warning)
            .setPositiveButton(R.string.continue_anyway) { dialog, _ ->
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.go_back) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun Context.isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }
}