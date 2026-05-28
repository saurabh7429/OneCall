package com.onecall.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.core.content.getSystemService
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.inv

fun Context.isOnWifi(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>() ?: return false
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}

fun Context.getCurrentWifiName(): String? {
    val wifiManager = applicationContext.getSystemService(WifiManager::class.java) ?: return null
    val ssid = wifiManager.connectionInfo?.ssid ?: return null
    val cleaned = ssid.trim('"')
    return cleaned.takeIf { cleaned.isNotBlank() && cleaned != "<unknown ssid>" }
}

fun Context.getWifiBroadcastAddresses(): List<InetAddress> {
    val addresses = mutableListOf<InetAddress>()
    addresses.add(InetAddress.getByName("255.255.255.255"))

    val wifiManager = applicationContext.getSystemService(WifiManager::class.java) ?: return addresses
    val dhcpInfo = wifiManager.dhcpInfo

    if (dhcpInfo.ipAddress == 0 || dhcpInfo.netmask == 0) {
        return addresses
    }

    val broadcastInt = (dhcpInfo.ipAddress and dhcpInfo.netmask) or dhcpInfo.netmask.inv()
    val buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(broadcastInt)
    val broadcastBytes = buffer.array()

    runCatching { addresses.add(InetAddress.getByAddress(broadcastBytes)) }
    return addresses.distinctBy { it.hostAddress }
}

fun Context.getLocalIpAddress(): String? {
    val wifiManager = applicationContext.getSystemService(WifiManager::class.java) ?: return null
    val ipInt = wifiManager.connectionInfo?.ipAddress ?: 0
    if (ipInt == 0) return null

    val buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(ipInt)
    return runCatching { InetAddress.getByAddress(buffer.array()).hostAddress }.getOrNull()
}

fun Context.getDeviceDisplayName(): String {
    val manufacturer = android.os.Build.MANUFACTURER.orEmpty().trim()
    val model = android.os.Build.MODEL.orEmpty().trim()
    return when {
        manufacturer.isNotBlank() && model.isNotBlank() -> "$manufacturer $model"
        model.isNotBlank() -> model
        else -> "OneCall Device"
    }
}