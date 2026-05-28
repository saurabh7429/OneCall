package com.onecall.core;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0011\u001a\u0004\u0018\u00010\fJ\u0014\u0010\u0012\u001a\u0004\u0018\u00010\f2\b\u0010\u0013\u001a\u0004\u0018\u00010\fH\u0002J\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015R\u0014\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/onecall/core/BluetoothCallBridge;", "", "context", "Landroid/content/Context;", "rfcomm", "Lcom/onecall/core/RfcommSignalingService;", "(Landroid/content/Context;Lcom/onecall/core/RfcommSignalingService;)V", "callStateListener", "Landroid/telephony/PhoneStateListener;", "getCallStateListener$annotations", "()V", "currentRingingNumber", "", "isListening", "", "telephonyManager", "Landroid/telephony/TelephonyManager;", "getCurrentRingingNumber", "resolveContactName", "number", "startListening", "", "stopListening", "Companion", "app_debug"})
public final class BluetoothCallBridge {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.core.RfcommSignalingService rfcomm = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "BluetoothCallBridge";
    @org.jetbrains.annotations.NotNull()
    private final android.telephony.TelephonyManager telephonyManager = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentRingingNumber;
    private boolean isListening = false;
    @org.jetbrains.annotations.NotNull()
    private final android.telephony.PhoneStateListener callStateListener = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.core.BluetoothCallBridge.Companion Companion = null;
    
    public BluetoothCallBridge(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.onecall.core.RfcommSignalingService rfcomm) {
        super();
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    @java.lang.Deprecated()
    private static void getCallStateListener$annotations() {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    public final void startListening() {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    public final void stopListening() {
    }
    
    private final java.lang.String resolveContactName(java.lang.String number) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCurrentRingingNumber() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/onecall/core/BluetoothCallBridge$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}