package com.onecall.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 +2\u00020\u0001:\u0001+B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\u001a\u001a\u00020\u0007H\u0002J\u000e\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u000fJ\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\u0010\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\rH\u0002J\u0006\u0010!\u001a\u00020\u0013J\u0010\u0010\"\u001a\u00020\u00072\u0006\u0010#\u001a\u00020$H\u0002J\u000e\u0010%\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u0006J\u0006\u0010\'\u001a\u00020\u0007J\u0006\u0010(\u001a\u00020\u0007J\u0016\u0010)\u001a\u00020\u00072\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00070\tH\u0002R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/onecall/core/RfcommSignalingService;", "", "mode", "Lcom/onecall/model/DeviceMode;", "onMessage", "Lkotlin/Function1;", "Lcom/onecall/model/RfcommMessage;", "", "onConnected", "Lkotlin/Function0;", "onDisconnected", "(Lcom/onecall/model/DeviceMode;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "clientSocket", "Landroid/bluetooth/BluetoothSocket;", "connectedDevice", "Landroid/bluetooth/BluetoothDevice;", "gson", "Lcom/google/gson/Gson;", "isRunning", "", "outputStream", "Ljava/io/OutputStream;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "serverSocket", "Landroid/bluetooth/BluetoothServerSocket;", "closeSocket", "connectToServer", "device", "getConnectedDeviceName", "", "handleConnection", "socket", "isConnected", "listenForMessages", "inputStream", "Ljava/io/InputStream;", "sendMessage", "message", "startServer", "stop", "withMainThread", "block", "Companion", "app_debug"})
public final class RfcommSignalingService {
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.model.DeviceMode mode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.onecall.model.RfcommMessage, kotlin.Unit> onMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onConnected = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onDisconnected = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "RfcommSignalingService";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.UUID SERVICE_UUID = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SERVICE_NAME = "OneCall";
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothServerSocket serverSocket;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothSocket clientSocket;
    @org.jetbrains.annotations.Nullable()
    private java.io.OutputStream outputStream;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private boolean isRunning = false;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothDevice connectedDevice;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.core.RfcommSignalingService.Companion Companion = null;
    
    public RfcommSignalingService(@org.jetbrains.annotations.NotNull()
    com.onecall.model.DeviceMode mode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.onecall.model.RfcommMessage, kotlin.Unit> onMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDisconnected) {
        super();
    }
    
    public final void startServer() {
    }
    
    public final void connectToServer(@org.jetbrains.annotations.NotNull()
    android.bluetooth.BluetoothDevice device) {
    }
    
    private final void handleConnection(android.bluetooth.BluetoothSocket socket) {
    }
    
    public final void sendMessage(@org.jetbrains.annotations.NotNull()
    com.onecall.model.RfcommMessage message) {
    }
    
    private final void listenForMessages(java.io.InputStream inputStream) {
    }
    
    private final void closeSocket() {
    }
    
    private final void withMainThread(kotlin.jvm.functions.Function0<kotlin.Unit> block) {
    }
    
    public final void stop() {
    }
    
    public final boolean isConnected() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getConnectedDeviceName() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/onecall/core/RfcommSignalingService$Companion;", "", "()V", "SERVICE_NAME", "", "SERVICE_UUID", "Ljava/util/UUID;", "getSERVICE_UUID", "()Ljava/util/UUID;", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.UUID getSERVICE_UUID() {
            return null;
        }
    }
}