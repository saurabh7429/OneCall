package com.onecall.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.onecall.R
import com.onecall.data.DeviceRoleStore
import com.onecall.utils.getCurrentWifiName

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val toolbarWifiName = view.findViewById<TextView>(R.id.toolbar_wifi_name)
        val toolbarSimStatus = view.findViewById<ImageView>(R.id.toolbar_sim_status)
        val toolbarNotificationBell = view.findViewById<ImageView>(R.id.toolbar_notification_bell)
        
        val isMain = DeviceRoleStore.isMain(requireContext())
        val wifiName = requireContext().getCurrentWifiName() ?: "Unknown Wi-Fi"
        
        if (isMain) {
            bottomNav.inflateMenu(R.menu.bottom_nav_menu)
            toolbarWifiName.text = wifiName
            toolbarSimStatus.visibility = View.VISIBLE
            // Basic SIM check mock for now
            toolbarSimStatus.setColorFilter(ContextCompat.getColor(requireContext(), R.color.onecall_success))
        } else {
            bottomNav.inflateMenu(R.menu.bottom_nav_secondary_menu)
            toolbarWifiName.text = "Connected to: $wifiName"
            toolbarSimStatus.visibility = View.GONE
        }
        
        if (savedInstanceState == null) {
            val defaultFragment = if (isMain) HomeDashboardFragment() else DialpadFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.dashboard_container, defaultFragment)
                .commit()
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeDashboardFragment()
                R.id.nav_dialpad -> DialpadFragment()
                R.id.nav_contacts -> ContactsFragment()
                R.id.nav_history -> com.onecall.ui.history.HistoryParentFragment()
                R.id.nav_settings -> AppSettingsFragment()
                else -> return@setOnItemSelectedListener false
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.dashboard_container, fragment)
                .commit()
            true
        }
    }
}