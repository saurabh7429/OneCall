package com.onecall.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.databinding.ActivityMainBinding
import com.onecall.model.DeviceMode
import com.onecall.service.OneCallService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var devicePrefs: BluetoothDevicePreference

    private var oneCallService: OneCallService? = null
    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? OneCallService.LocalBinder
            oneCallService = binder?.getService()
            serviceBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            oneCallService = null
            serviceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        devicePrefs = BluetoothDevicePreference(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup bottom nav
        binding.bottomNavigation.setupWithNavController(navController)

        // Show/hide bottom nav based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dashboardFragments = setOf(
                R.id.mainDashboardFragment,
                R.id.secondaryDashboardFragment,
                R.id.dialpadFragment,
                R.id.contactsFragment,
                R.id.callHistoryFragment,
                R.id.settingsFragment
            )
            if (destination.id in dashboardFragments) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }

        // Start and bind service
        startOneCallService()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, OneCallService::class.java).also { intent ->
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
    }

    private fun startOneCallService() {
        val mode = devicePrefs.getDeviceMode()
        if (mode != null) {
            val serviceIntent = Intent(this, OneCallService::class.java)
            startForegroundService(serviceIntent)
        }
    }

    fun getOneCallService(): OneCallService? = oneCallService

    fun navigateToDashboard() {
        val mode = devicePrefs.getDeviceMode()
        when (mode) {
            DeviceMode.MAIN -> navController.navigate(R.id.mainDashboardFragment)
            DeviceMode.SECONDARY -> navController.navigate(R.id.secondaryDashboardFragment)
            null -> {} // Stay on welcome
        }
    }
}
