package com.onecall.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cBS\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\u0002\u0010\u000eJ\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u0018J\u0006\u0010\u0019\u001a\u00020\tJ\b\u0010\u001a\u001a\u00020\tH\u0002J\u0006\u0010\u001b\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/onecall/core/BluetoothHfpController;", "", "context", "Landroid/content/Context;", "mode", "Lcom/onecall/model/DeviceMode;", "onConnected", "Lkotlin/Function1;", "Landroid/bluetooth/BluetoothDevice;", "", "onDisconnected", "Lkotlin/Function0;", "onAudioConnected", "onAudioDisconnected", "(Landroid/content/Context;Lcom/onecall/model/DeviceMode;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "headsetProxy", "Landroid/bluetooth/BluetoothHeadset;", "hfpReceiver", "Landroid/content/BroadcastReceiver;", "isReceiverRegistered", "", "serviceListener", "Landroid/bluetooth/BluetoothProfile$ServiceListener;", "getConnectedDevices", "", "initialize", "registerReceiver", "release", "Companion", "app_debug"})
public final class BluetoothHfpController {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.model.DeviceMode mode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<android.bluetooth.BluetoothDevice, kotlin.Unit> onConnected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onDisconnected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onAudioConnected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onAudioDisconnected = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "BluetoothHfpController";
    private static final int PROFILE_HFP_CLIENT = 16;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothHeadset headsetProxy;
    private boolean isReceiverRegistered = false;
    @org.jetbrains.annotations.NotNull()
    private final android.bluetooth.BluetoothProfile.ServiceListener serviceListener = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver hfpReceiver = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.core.BluetoothHfpController.Companion Companion = null;
    
    public BluetoothHfpController(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.onecall.model.DeviceMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super android.bluetooth.BluetoothDevice, kotlin.Unit> onConnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDisconnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAudioConnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAudioDisconnected) {
        super();
    }
    
    public final void initialize() {
    }
    
    private final void registerReceiver() {
    }
    
    public final void release() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/onecall/core/BluetoothHfpController$Companion;", "", "()V", "PROFILE_HFP_CLIENT", "", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}