package com.onecall.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_history")
data class DeviceHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "device_id")
    val deviceId: String,
    @ColumnInfo(name = "device_name")
    val deviceName: String,
    @ColumnInfo(name = "first_connected_at")
    val firstConnectedAt: Long,
    @ColumnInfo(name = "last_seen_at")
    val lastSeenAt: Long,
    @ColumnInfo(name = "calls_attended")
    val callsAttended: Int,
    @ColumnInfo(name = "calls_made")
    val callsMade: Int
)
