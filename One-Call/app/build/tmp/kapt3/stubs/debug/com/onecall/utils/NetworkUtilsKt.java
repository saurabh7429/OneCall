package com.onecall.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a\f\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0001*\u00020\u0002\u001a\f\u0010\u0004\u001a\u0004\u0018\u00010\u0001*\u00020\u0002\u001a\u0010\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006*\u00020\u0002\u001a\n\u0010\b\u001a\u00020\t*\u00020\u0002\u00a8\u0006\n"}, d2 = {"getCurrentWifiName", "", "Landroid/content/Context;", "getDeviceDisplayName", "getLocalIpAddress", "getWifiBroadcastAddresses", "", "Ljava/net/InetAddress;", "isOnWifi", "", "app_debug"})
public final class NetworkUtilsKt {
    
    public static final boolean isOnWifi(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$isOnWifi) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.String getCurrentWifiName(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$getCurrentWifiName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<java.net.InetAddress> getWifiBroadcastAddresses(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$getWifiBroadcastAddresses) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.String getLocalIpAddress(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$getLocalIpAddress) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getDeviceDisplayName(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$getDeviceDisplayName) {
        return null;
    }
}