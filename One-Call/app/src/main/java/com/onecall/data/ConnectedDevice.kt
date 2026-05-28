package com.onecall.data

data class ConnectedDevice(
    val deviceId: String,
    val displayName: String,
    val ipAddress: String,
    val tcpPort: Int,
    val connectedAtMillis: Long,
)