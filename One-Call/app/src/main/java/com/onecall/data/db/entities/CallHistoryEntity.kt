package com.onecall.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_history")
data class CallHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val callerNumber: String,
    val callerName: String?,
    val callType: String,       // "INCOMING" / "OUTGOING" / "MISSED"
    val durationSeconds: Long,
    val timestamp: Long,
    val deviceSource: String,   // "MAIN" / "SECONDARY"
    val isPermanent: Boolean    // true = main device, false = session only
)
