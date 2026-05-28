package com.onecall.data.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_config")
data class DeviceConfigEntity(
    @PrimaryKey
    val deviceId: String,
    val nickname: String,
    val deviceIcon: String, // e.g. PHONE, TABLET, FRIDGE, TV, OTHER
    val ringOnDevice: Boolean,
    val allowOutgoing: Boolean,
    val autoApproveOutgoing: Boolean,
    val ringVolume: Int, // 0 to 100
    val dndStartTime: String, // e.g. "22:00"
    val dndEndTime: String, // e.g. "07:00"
    val isCallsPaused: Boolean,
)
