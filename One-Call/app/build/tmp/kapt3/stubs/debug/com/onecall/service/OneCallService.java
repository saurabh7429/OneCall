package com.onecall.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0012\u0018\u0000 ^2\u00020\u0001:\u0002^_B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u000eJ\u0006\u00100\u001a\u00020.J\u000e\u00101\u001a\u0002022\u0006\u00103\u001a\u00020\u000eJ\b\u00104\u001a\u00020.H\u0002J\b\u00105\u001a\u00020.H\u0002J\b\u00106\u001a\u00020.H\u0002J\b\u00107\u001a\u00020.H\u0002J\b\u00108\u001a\u00020.H\u0002J\u000e\u00109\u001a\u00020.2\u0006\u0010:\u001a\u00020&J\u0006\u0010;\u001a\u00020.J\u0006\u0010<\u001a\u00020.J\u0010\u0010=\u001a\u00020.2\u0006\u0010>\u001a\u00020?H\u0002J\b\u0010@\u001a\u00020.H\u0002J\u0006\u0010A\u001a\u00020&J\u001c\u0010B\u001a\u00020.2\b\u0010C\u001a\u0004\u0018\u00010\u000e2\b\u0010D\u001a\u0004\u0018\u00010\u000eH\u0002J\u001c\u0010E\u001a\u00020.2\b\u0010C\u001a\u0004\u0018\u00010\u000e2\b\u0010D\u001a\u0004\u0018\u00010\u000eH\u0002J\u0012\u0010F\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u00010\u000eH\u0002J\u0012\u0010G\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010JH\u0016J\b\u0010K\u001a\u00020.H\u0016J\b\u0010L\u001a\u00020.H\u0016J\"\u0010M\u001a\u00020N2\b\u0010I\u001a\u0004\u0018\u00010J2\u0006\u0010O\u001a\u00020N2\u0006\u0010P\u001a\u00020NH\u0016J\b\u0010Q\u001a\u00020.H\u0002J\u000e\u0010R\u001a\u00020.2\u0006\u0010/\u001a\u00020\u000eJ\u000e\u0010S\u001a\u00020.2\u0006\u0010T\u001a\u00020&J\u0018\u0010U\u001a\u00020.2\u0006\u0010>\u001a\u00020?2\u0006\u0010V\u001a\u00020&H\u0002J\u000e\u0010W\u001a\u00020.2\u0006\u0010X\u001a\u00020\u000eJ\u001a\u0010Y\u001a\u00020.2\u0006\u0010Z\u001a\u00020\u00122\b\u0010[\u001a\u0004\u0018\u00010\u000eH\u0002J\u000e\u0010\\\u001a\u00020.2\u0006\u0010]\u001a\u00020\u000eR\u0012\u0010\u0003\u001a\u00060\u0004R\u00020\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\b\u0010\r\u001a\u0004\u0018\u00010\u000e@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0012@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0011\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0011\"\u0004\b\u001c\u0010\u0019R\"\u0010\u001e\u001a\u0004\u0018\u00010\u001d2\b\u0010\r\u001a\u0004\u0018\u00010\u001d@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u000e\u0010!\u001a\u00020\"X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\'\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006`"}, d2 = {"Lcom/onecall/service/OneCallService;", "Landroid/app/Service;", "()V", "binder", "Lcom/onecall/service/OneCallService$LocalBinder;", "btManager", "Lcom/onecall/core/BluetoothManager;", "btStateReceiver", "Landroid/content/BroadcastReceiver;", "callBridge", "Lcom/onecall/core/BluetoothCallBridge;", "callStartTime", "", "<set-?>", "", "connectedDeviceName", "getConnectedDeviceName", "()Ljava/lang/String;", "Lcom/onecall/model/ConnectionState;", "connectionState", "getConnectionState", "()Lcom/onecall/model/ConnectionState;", "currentCallerName", "getCurrentCallerName", "setCurrentCallerName", "(Ljava/lang/String;)V", "currentCallerNumber", "getCurrentCallerNumber", "setCurrentCallerNumber", "Lcom/onecall/model/DeviceMode;", "currentMode", "getCurrentMode", "()Lcom/onecall/model/DeviceMode;", "devicePrefs", "Lcom/onecall/core/BluetoothDevicePreference;", "hfpController", "Lcom/onecall/core/BluetoothHfpController;", "isCallActive", "", "repository", "Lcom/onecall/data/repository/CallHistoryRepository;", "rfcommService", "Lcom/onecall/core/RfcommSignalingService;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "approveOutgoingCall", "", "number", "blockOutgoingCall", "buildNotification", "Landroid/app/Notification;", "contentText", "createNotificationChannel", "dismissIncomingCall", "dismissIncomingCallSilently", "endActiveCall", "endCallViaTelecom", "handleAcceptCall", "isSecondary", "handleDeclineCall", "handleEndCall", "handleRfcommMessage", "message", "Lcom/onecall/model/RfcommMessage;", "initializeService", "isConnected", "launchActiveCall", "callerName", "callerNumber", "launchIncomingCall", "makeCall", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "reinitialize", "requestOutgoingCall", "respondToTransfer", "accepted", "saveHistoryEntry", "isPermanent", "sendTransferRequest", "targetDeviceName", "updateConnectionState", "state", "deviceName", "updateNotification", "text", "Companion", "LocalBinder", "app_debug"})
public final class OneCallService extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OneCallService";
    private static final int NOTIFICATION_ID = 1001;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "onecall_service";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_ACCEPT_CALL = "com.onecall.ACCEPT_CALL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_DECLINE_CALL = "com.onecall.DECLINE_CALL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_END_CALL = "com.onecall.END_CALL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_TOGGLE_STOP_CALLS = "com.onecall.TOGGLE_STOP_CALLS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROADCAST_INCOMING_CALL = "com.onecall.INCOMING_CALL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROADCAST_CALL_ENDED = "com.onecall.CALL_ENDED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROADCAST_CONNECTION_CHANGED = "com.onecall.CONNECTION_CHANGED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROADCAST_OUTGOING_REQUEST = "com.onecall.OUTGOING_REQUEST";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROADCAST_TRANSFER_RESPONSE = "com.onecall.TRANSFER_RESPONSE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CALLER_NAME = "caller_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CALLER_NUMBER = "caller_number";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CONNECTION_STATE = "connection_state";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_DEVICE_NAME = "device_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_APPROVED = "approved";
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.onecall.service.OneCallService instance;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.service.OneCallService.LocalBinder binder = null;
    private com.onecall.core.BluetoothDevicePreference devicePrefs;
    private com.onecall.core.BluetoothManager btManager;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.core.BluetoothHfpController hfpController;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.core.RfcommSignalingService rfcommService;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.core.BluetoothCallBridge callBridge;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.data.repository.CallHistoryRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private com.onecall.model.ConnectionState connectionState = com.onecall.model.ConnectionState.DISCONNECTED;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String connectedDeviceName;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.model.DeviceMode currentMode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentCallerName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currentCallerNumber;
    private long callStartTime = 0L;
    private boolean isCallActive = false;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver btStateReceiver = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.service.OneCallService.Companion Companion = null;
    
    public OneCallService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.onecall.model.ConnectionState getConnectionState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getConnectedDeviceName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.onecall.model.DeviceMode getCurrentMode() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCurrentCallerName() {
        return null;
    }
    
    public final void setCurrentCallerName(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCurrentCallerNumber() {
        return null;
    }
    
    public final void setCurrentCallerNumber(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void initializeService() {
    }
    
    private final void reinitialize() {
    }
    
    private final void handleRfcommMessage(com.onecall.model.RfcommMessage message) {
    }
    
    public final void handleAcceptCall(boolean isSecondary) {
    }
    
    public final void handleDeclineCall() {
    }
    
    public final void handleEndCall() {
    }
    
    private final void launchIncomingCall(java.lang.String callerName, java.lang.String callerNumber) {
    }
    
    private final void launchActiveCall(java.lang.String callerName, java.lang.String callerNumber) {
    }
    
    private final void dismissIncomingCall() {
    }
    
    private final void dismissIncomingCallSilently() {
    }
    
    private final void endActiveCall() {
    }
    
    public final void sendTransferRequest(@org.jetbrains.annotations.NotNull()
    java.lang.String targetDeviceName) {
    }
    
    public final void respondToTransfer(boolean accepted) {
    }
    
    public final void requestOutgoingCall(@org.jetbrains.annotations.NotNull()
    java.lang.String number) {
    }
    
    public final void approveOutgoingCall(@org.jetbrains.annotations.NotNull()
    java.lang.String number) {
    }
    
    public final void blockOutgoingCall() {
    }
    
    private final void makeCall(java.lang.String number) {
    }
    
    private final void endCallViaTelecom() {
    }
    
    private final void saveHistoryEntry(com.onecall.model.RfcommMessage message, boolean isPermanent) {
    }
    
    private final void updateConnectionState(com.onecall.model.ConnectionState state, java.lang.String deviceName) {
    }
    
    private final void createNotificationChannel() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification buildNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String contentText) {
        return null;
    }
    
    public final void updateNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final boolean isConnected() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\u00a8\u0006\u001c"}, d2 = {"Lcom/onecall/service/OneCallService$Companion;", "", "()V", "ACTION_ACCEPT_CALL", "", "ACTION_DECLINE_CALL", "ACTION_END_CALL", "ACTION_TOGGLE_STOP_CALLS", "BROADCAST_CALL_ENDED", "BROADCAST_CONNECTION_CHANGED", "BROADCAST_INCOMING_CALL", "BROADCAST_OUTGOING_REQUEST", "BROADCAST_TRANSFER_RESPONSE", "CHANNEL_ID", "EXTRA_APPROVED", "EXTRA_CALLER_NAME", "EXTRA_CALLER_NUMBER", "EXTRA_CONNECTION_STATE", "EXTRA_DEVICE_NAME", "NOTIFICATION_ID", "", "TAG", "instance", "Lcom/onecall/service/OneCallService;", "getInstance", "()Lcom/onecall/service/OneCallService;", "setInstance", "(Lcom/onecall/service/OneCallService;)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.onecall.service.OneCallService getInstance() {
            return null;
        }
        
        public final void setInstance(@org.jetbrains.annotations.Nullable()
        com.onecall.service.OneCallService p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/onecall/service/OneCallService$LocalBinder;", "Landroid/os/Binder;", "(Lcom/onecall/service/OneCallService;)V", "getService", "Lcom/onecall/service/OneCallService;", "app_debug"})
    public final class LocalBinder extends android.os.Binder {
        
        public LocalBinder() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.service.OneCallService getService() {
            return null;
        }
    }
}