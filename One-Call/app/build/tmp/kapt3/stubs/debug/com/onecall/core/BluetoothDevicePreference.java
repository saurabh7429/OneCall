package com.onecall.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0014\u0018\u0000 #2\u00020\u0001:\u0001#B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\u000bJ\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010J\b\u0010\u0012\u001a\u0004\u0018\u00010\u0010J\u0006\u0010\u0013\u001a\u00020\u000bJ\u0006\u0010\u0014\u001a\u00020\u000bJ\u0006\u0010\u0015\u001a\u00020\u000bJ\u0016\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000bJ\u000e\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000bJ\u000e\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000bJ\u000e\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\u000bJ\u000e\u0010\u001f\u001a\u00020\b2\u0006\u0010 \u001a\u00020\u000eJ\u000e\u0010!\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0010J\u000e\u0010\"\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/onecall/core/BluetoothDevicePreference;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "clearDeviceMode", "", "clearPairedDevice", "getAutoApproveOutgoing", "", "getAutoReconnect", "getDeviceMode", "Lcom/onecall/model/DeviceMode;", "getDeviceNickname", "", "getPairedDeviceAddress", "getPairedDeviceName", "isAllowOutgoing", "isCallsStopped", "isRingEnabled", "savePairedDeviceAddress", "address", "name", "setAllowOutgoing", "enabled", "setAutoApproveOutgoing", "setAutoReconnect", "setCallsStopped", "stopped", "setDeviceMode", "mode", "setDeviceNickname", "setRingEnabled", "Companion", "app_debug"})
public final class BluetoothDevicePreference {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_DEVICE_MODE = "device_mode";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PAIRED_ADDRESS = "paired_address";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PAIRED_NAME = "paired_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_RECONNECT = "auto_reconnect";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_APPROVE_OUTGOING = "auto_approve_outgoing";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_NICKNAME = "device_nickname";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_RING_ENABLED = "ring_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ALLOW_OUTGOING = "allow_outgoing";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CALLS_STOPPED = "calls_stopped";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.core.BluetoothDevicePreference.Companion Companion = null;
    
    public BluetoothDevicePreference(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.onecall.model.DeviceMode getDeviceMode() {
        return null;
    }
    
    public final void setDeviceMode(@org.jetbrains.annotations.NotNull()
    com.onecall.model.DeviceMode mode) {
    }
    
    public final void clearDeviceMode() {
    }
    
    public final void savePairedDeviceAddress(@org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPairedDeviceAddress() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPairedDeviceName() {
        return null;
    }
    
    public final void clearPairedDevice() {
    }
    
    public final boolean getAutoReconnect() {
        return false;
    }
    
    public final void setAutoReconnect(boolean enabled) {
    }
    
    public final boolean getAutoApproveOutgoing() {
        return false;
    }
    
    public final void setAutoApproveOutgoing(boolean enabled) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceNickname() {
        return null;
    }
    
    public final void setDeviceNickname(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final boolean isRingEnabled() {
        return false;
    }
    
    public final void setRingEnabled(boolean enabled) {
    }
    
    public final boolean isAllowOutgoing() {
        return false;
    }
    
    public final void setAllowOutgoing(boolean enabled) {
    }
    
    public final boolean isCallsStopped() {
        return false;
    }
    
    public final void setCallsStopped(boolean stopped) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/onecall/core/BluetoothDevicePreference$Companion;", "", "()V", "KEY_ALLOW_OUTGOING", "", "KEY_AUTO_APPROVE_OUTGOING", "KEY_AUTO_RECONNECT", "KEY_CALLS_STOPPED", "KEY_DEVICE_MODE", "KEY_NICKNAME", "KEY_PAIRED_ADDRESS", "KEY_PAIRED_NAME", "KEY_RING_ENABLED", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}