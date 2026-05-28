package com.onecall.data.settings

import android.content.Context
import android.content.SharedPreferences
import com.onecall.data.history.HistoryDao
import com.onecall.data.history.HistoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository private constructor(
    private val context: Context,
    private val historyDao: HistoryDao
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("onecall_settings", Context.MODE_PRIVATE)

    // Global Settings
    val myDeviceName: String
        get() = prefs.getString("my_device_name", null) ?: android.os.Build.MODEL
    
    fun setMyDeviceName(name: String) = prefs.edit().putString("my_device_name", name).apply()

    val myDeviceIcon: String
        get() = prefs.getString("my_device_icon", "PHONE")!!

    fun setMyDeviceIcon(icon: String) = prefs.edit().putString("my_device_icon", icon).apply()

    // Network & Calls (Main Only)
    val maxDevices: Int
        get() = prefs.getInt("max_devices", 5)

    fun setMaxDevices(max: Int) = prefs.edit().putInt("max_devices", max).apply()

    val autoReconnect: Boolean
        get() = prefs.getBoolean("auto_reconnect", true)

    fun setAutoReconnect(auto: Boolean) = prefs.edit().putBoolean("auto_reconnect", auto).apply()

    val codeExpiryTimeMinutes: Int
        get() = prefs.getInt("code_expiry_minutes", 10)

    fun setCodeExpiryTimeMinutes(minutes: Int) = prefs.edit().putInt("code_expiry_minutes", minutes).apply()

    val autoApproveOutgoing: Boolean
        get() = prefs.getBoolean("auto_approve_outgoing", false)

    fun setAutoApproveOutgoing(auto: Boolean) = prefs.edit().putBoolean("auto_approve_outgoing", auto).apply()

    val notifyOutgoingOnMain: Boolean
        get() = prefs.getBoolean("notify_outgoing_main", true)

    fun setNotifyOutgoingOnMain(notify: Boolean) = prefs.edit().putBoolean("notify_outgoing_main", notify).apply()

    // Per-Device Settings (Main Only - Stored in Room)
    fun getDeviceConfigFlow(deviceId: String): Flow<DeviceConfigEntity?> {
        return historyDao.getDeviceConfigFlow(deviceId)
    }

    suspend fun getDeviceConfig(deviceId: String): DeviceConfigEntity {
        val existing = historyDao.getDeviceConfig(deviceId)
        if (existing != null) return existing
        
        // Return default config if not found
        return DeviceConfigEntity(
            deviceId = deviceId,
            nickname = "Secondary Device",
            deviceIcon = "PHONE",
            ringOnDevice = true,
            allowOutgoing = true,
            autoApproveOutgoing = false,
            ringVolume = 100,
            dndStartTime = "00:00",
            dndEndTime = "00:00",
            isCallsPaused = false
        )
    }

    suspend fun saveDeviceConfig(config: DeviceConfigEntity) {
        historyDao.insertDeviceConfig(config)
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsRepository(
                    context.applicationContext,
                    HistoryDatabase.getDatabase(context.applicationContext).historyDao()
                )
                INSTANCE = instance
                instance
            }
        }
    }
}
