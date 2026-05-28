package com.onecall.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build

class BluetoothManager(private val context: Context) {

    val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothEnabled(): Boolean = adapter?.isEnabled == true

    fun isBluetoothSupported(): Boolean = adapter != null

    fun startDiscovery(): Boolean {
        if (!isBluetoothEnabled()) return false
        return adapter?.startDiscovery() ?: false
    }

    fun stopDiscovery() {
        adapter?.cancelDiscovery()
    }

    fun makeDiscoverable(durationSeconds: Int = 300) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, durationSeconds)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun getBondedDevices(): Set<BluetoothDevice> =
        try { adapter?.bondedDevices ?: emptySet() } catch (e: SecurityException) { emptySet() }

    fun isPaired(device: BluetoothDevice): Boolean =
        try { device.bondState == BluetoothDevice.BOND_BONDED } catch (e: SecurityException) { false }

    fun getDeviceName(device: BluetoothDevice): String {
        return try {
            device.name ?: device.address
        } catch (e: SecurityException) {
            device.address
        }
    }

    fun removeBond(device: BluetoothDevice): Boolean {
        return try {
            val method = device.javaClass.getMethod("removeBond")
            method.invoke(device) as? Boolean ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun enableBluetooth() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
