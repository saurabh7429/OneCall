package com.onecall.core

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import com.onecall.model.DeviceMode

class BluetoothDevicePreference(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("onecall_prefs", Context.MODE_PRIVATE)

    // Device Mode
    fun getDeviceMode(): DeviceMode? =
        DeviceMode.fromString(prefs.getString(KEY_DEVICE_MODE, null))

    fun setDeviceMode(mode: DeviceMode) =
        prefs.edit().putString(KEY_DEVICE_MODE, mode.name).apply()

    fun clearDeviceMode() =
        prefs.edit().remove(KEY_DEVICE_MODE).apply()

    // Paired Device
    fun savePairedDeviceAddress(address: String, name: String) {
        prefs.edit()
            .putString(KEY_PAIRED_ADDRESS, address)
            .putString(KEY_PAIRED_NAME, name)
            .apply()
    }

    fun getPairedDeviceAddress(): String? = prefs.getString(KEY_PAIRED_ADDRESS, null)

    fun getPairedDeviceName(): String? = prefs.getString(KEY_PAIRED_NAME, null)

    fun clearPairedDevice() {
        prefs.edit()
            .remove(KEY_PAIRED_ADDRESS)
            .remove(KEY_PAIRED_NAME)
            .apply()
    }

    // Settings
    fun getAutoReconnect(): Boolean = prefs.getBoolean(KEY_AUTO_RECONNECT, true)
    fun setAutoReconnect(enabled: Boolean) = prefs.edit().putBoolean(KEY_AUTO_RECONNECT, enabled).apply()

    fun getAutoApproveOutgoing(): Boolean = prefs.getBoolean(KEY_AUTO_APPROVE_OUTGOING, false)
    fun setAutoApproveOutgoing(enabled: Boolean) = prefs.edit().putBoolean(KEY_AUTO_APPROVE_OUTGOING, enabled).apply()

    fun getDeviceNickname(): String = prefs.getString(KEY_NICKNAME, "My Device") ?: "My Device"
    fun setDeviceNickname(name: String) = prefs.edit().putString(KEY_NICKNAME, name).apply()

    fun isRingEnabled(): Boolean = prefs.getBoolean(KEY_RING_ENABLED, true)
    fun setRingEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_RING_ENABLED, enabled).apply()

    fun isAllowOutgoing(): Boolean = prefs.getBoolean(KEY_ALLOW_OUTGOING, true)
    fun setAllowOutgoing(enabled: Boolean) = prefs.edit().putBoolean(KEY_ALLOW_OUTGOING, enabled).apply()

    fun isCallsStopped(): Boolean = prefs.getBoolean(KEY_CALLS_STOPPED, false)
    fun setCallsStopped(stopped: Boolean) = prefs.edit().putBoolean(KEY_CALLS_STOPPED, stopped).apply()

    companion object {
        private const val KEY_DEVICE_MODE = "device_mode"
        private const val KEY_PAIRED_ADDRESS = "paired_address"
        private const val KEY_PAIRED_NAME = "paired_name"
        private const val KEY_AUTO_RECONNECT = "auto_reconnect"
        private const val KEY_AUTO_APPROVE_OUTGOING = "auto_approve_outgoing"
        private const val KEY_NICKNAME = "device_nickname"
        private const val KEY_RING_ENABLED = "ring_enabled"
        private const val KEY_ALLOW_OUTGOING = "allow_outgoing"
        private const val KEY_CALLS_STOPPED = "calls_stopped"
    }
}
