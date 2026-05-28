package com.onecall.ui.calls;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.onecall.R;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004H\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J2\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00042\b\u0010\u0010\u001a\u0004\u0018\u00010\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/onecall/ui/calls/CallTransferNotifications;", "", "()V", "ACTION_ACCEPT", "", "ACTION_REJECT", "CHANNEL_ID", "CHANNEL_NAME", "EXTRA_CALLER_NAME", "EXTRA_CALLER_NUMBER", "EXTRA_FROM_DEVICE_NAME", "EXTRA_NOTIFICATION_ID", "EXTRA_REQUEST_ID", "buildCallerLabel", "context", "Landroid/content/Context;", "callerName", "callerNumber", "ensureChannel", "", "showTransferRequest", "requestId", "fromDeviceName", "app_debug"})
public final class CallTransferNotifications {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_ACCEPT = "com.onecall.ACTION_TRANSFER_ACCEPT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_REJECT = "com.onecall.ACTION_TRANSFER_REJECT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_REQUEST_ID = "extra_request_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_FROM_DEVICE_NAME = "extra_from_device_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CALLER_NAME = "extra_caller_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CALLER_NUMBER = "extra_caller_number";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_NOTIFICATION_ID = "extra_notification_id";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "onecall_transfer";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_NAME = "OneCall Transfer";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.calls.CallTransferNotifications INSTANCE = null;
    
    private CallTransferNotifications() {
        super();
    }
    
    public final void showTransferRequest(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String requestId, @org.jetbrains.annotations.NotNull()
    java.lang.String fromDeviceName, @org.jetbrains.annotations.Nullable()
    java.lang.String callerName, @org.jetbrains.annotations.Nullable()
    java.lang.String callerNumber) {
    }
    
    private final java.lang.String buildCallerLabel(android.content.Context context, java.lang.String callerName, java.lang.String callerNumber) {
        return null;
    }
    
    private final void ensureChannel(android.content.Context context) {
    }
}