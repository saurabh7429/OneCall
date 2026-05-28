package com.onecall.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_history")
data class CallHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "caller_name")
    val callerName: String?,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "call_type")
    val callType: String, // INCOMING, OUTGOING, MISSED
    @ColumnInfo(name = "date_time")
    val dateTime: Long,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,
    @ColumnInfo(name = "attended_by_device")
    val attendedByDevice: String?,
    @ColumnInfo(name = "ring_count")
    val ringCount: Int
)
