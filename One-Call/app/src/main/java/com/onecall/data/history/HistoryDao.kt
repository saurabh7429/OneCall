package com.onecall.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    
    // Call History Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallHistory(callHistory: CallHistoryEntity)

    @Query("SELECT * FROM call_history ORDER BY date_time DESC")
    fun getAllCallHistory(): Flow<List<CallHistoryEntity>>

    @Query("DELETE FROM call_history WHERE id = :id")
    suspend fun deleteCallHistoryById(id: Long)

    @Query("DELETE FROM call_history")
    suspend fun clearAllCallHistory()

    // Device History Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceHistory(deviceHistory: DeviceHistoryEntity)

    @Query("SELECT * FROM device_history ORDER BY last_seen_at DESC")
    fun getAllDeviceHistory(): Flow<List<DeviceHistoryEntity>>

    @Query("SELECT * FROM device_history WHERE device_id = :deviceId LIMIT 1")
    suspend fun getDeviceHistoryById(deviceId: String): DeviceHistoryEntity?

    @Query("DELETE FROM device_history WHERE device_id = :deviceId")
    suspend fun deleteDeviceHistoryById(deviceId: String)

    // Device Config Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceConfig(config: com.onecall.data.settings.DeviceConfigEntity)

    @Query("SELECT * FROM device_config WHERE deviceId = :deviceId LIMIT 1")
    fun getDeviceConfigFlow(deviceId: String): Flow<com.onecall.data.settings.DeviceConfigEntity?>

    @Query("SELECT * FROM device_config WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getDeviceConfig(deviceId: String): com.onecall.data.settings.DeviceConfigEntity?
    
    @Query("SELECT * FROM device_config")
    fun getAllDeviceConfigs(): Flow<List<com.onecall.data.settings.DeviceConfigEntity>>
}
