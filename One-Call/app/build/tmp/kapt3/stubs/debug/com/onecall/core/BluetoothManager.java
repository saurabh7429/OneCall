package com.onecall.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rJ\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0010\u001a\u00020\rJ\u0010\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0010\u001a\u00020\rJ\u0006\u0010\u0019\u001a\u00020\u0012J\u0006\u0010\u001a\u001a\u00020\nR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/onecall/core/BluetoothManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "adapter", "Landroid/bluetooth/BluetoothAdapter;", "getAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "enableBluetooth", "", "getBondedDevices", "", "Landroid/bluetooth/BluetoothDevice;", "getDeviceName", "", "device", "isBluetoothEnabled", "", "isBluetoothSupported", "isPaired", "makeDiscoverable", "durationSeconds", "", "removeBond", "startDiscovery", "stopDiscovery", "app_debug"})
public final class BluetoothManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private final android.bluetooth.BluetoothAdapter adapter = null;
    
    public BluetoothManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.bluetooth.BluetoothAdapter getAdapter() {
        return null;
    }
    
    public final boolean isBluetoothEnabled() {
        return false;
    }
    
    public final boolean isBluetoothSupported() {
        return false;
    }
    
    public final boolean startDiscovery() {
        return false;
    }
    
    public final void stopDiscovery() {
    }
    
    public final void makeDiscoverable(int durationSeconds) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<android.bluetooth.BluetoothDevice> getBondedDevices() {
        return null;
    }
    
    public final boolean isPaired(@org.jetbrains.annotations.NotNull()
    android.bluetooth.BluetoothDevice device) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceName(@org.jetbrains.annotations.NotNull()
    android.bluetooth.BluetoothDevice device) {
        return null;
    }
    
    public final boolean removeBond(@org.jetbrains.annotations.NotNull()
    android.bluetooth.BluetoothDevice device) {
        return false;
    }
    
    public final void enableBluetooth() {
    }
}