package com.onecall.data

data class SecondaryConnectionRecord(
    val codeLease: CodeLease,
    val mainDeviceId: String,
    val mainDeviceName: String,
    val mainHost: String,
    val mainPort: Int,
    val secondaryDeviceId: String,
    val secondaryDeviceName: String,
    val connectedAtMillis: Long,
    val wifiName: String?,
)