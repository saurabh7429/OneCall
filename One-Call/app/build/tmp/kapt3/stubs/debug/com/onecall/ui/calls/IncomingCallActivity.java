package com.onecall.ui.calls;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.onecall.R;
import com.onecall.data.DeviceRoleStore;
import com.onecall.network.socket.OneCallConnectionManager;
import com.onecall.network.sip.SecondarySipClient;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 %2\u00020\u0001:\u0001%B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0018\u001a\u00020\u0016H\u0002J\u0012\u0010\u0019\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0016H\u0014J\b\u0010\u001d\u001a\u00020\u0016H\u0014J\b\u0010\u001e\u001a\u00020\u0016H\u0014J\b\u0010\u001f\u001a\u00020\u0016H\u0002J\b\u0010 \u001a\u00020\u0016H\u0002J\u0014\u0010!\u001a\u0004\u0018\u00010\u000e2\b\u0010\"\u001a\u0004\u0018\u00010\u000eH\u0002J\b\u0010#\u001a\u00020\u0016H\u0002J\b\u0010$\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/onecall/ui/calls/IncomingCallActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "callAccepted", "", "callDeclined", "callerNameText", "Landroid/widget/TextView;", "callerNumberText", "finishReceiver", "Landroid/content/BroadcastReceiver;", "initialRingingCount", "", "phoneNumber", "", "ringingCountText", "ringtone", "Landroid/media/Ringtone;", "role", "vibrator", "Landroid/os/Vibrator;", "acceptRingingCall", "", "handleAccept", "handleDecline", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "recordMissedCall", "rejectRingingCall", "resolveCallerName", "number", "startRinging", "stopRinging", "Companion", "app_debug"})
public final class IncomingCallActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.widget.TextView callerNameText;
    private android.widget.TextView callerNumberText;
    private android.widget.TextView ringingCountText;
    @org.jetbrains.annotations.Nullable()
    private android.media.Ringtone ringtone;
    @org.jetbrains.annotations.Nullable()
    private android.os.Vibrator vibrator;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String role = "SECONDARY";
    @org.jetbrains.annotations.Nullable()
    private java.lang.String phoneNumber;
    private boolean callAccepted = false;
    private boolean callDeclined = false;
    private int initialRingingCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver finishReceiver = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_ROLE = "extra_role";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_PHONE_NUMBER = "extra_phone";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_RINGING_COUNT = "extra_ringing_count";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ACTION_FINISH_INCOMING_CALL = "com.onecall.ACTION_FINISH_INCOMING_CALL";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.calls.IncomingCallActivity.Companion Companion = null;
    
    public IncomingCallActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onStart() {
    }
    
    @java.lang.Override()
    protected void onStop() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void recordMissedCall() {
    }
    
    private final void handleAccept() {
    }
    
    private final void handleDecline() {
    }
    
    private final java.lang.String resolveCallerName(java.lang.String number) {
        return null;
    }
    
    private final void startRinging() {
    }
    
    private final void stopRinging() {
    }
    
    private final void acceptRingingCall() {
    }
    
    private final void rejectRingingCall() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ(\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/onecall/ui/calls/IncomingCallActivity$Companion;", "", "()V", "ACTION_FINISH_INCOMING_CALL", "", "EXTRA_PHONE_NUMBER", "EXTRA_RINGING_COUNT", "EXTRA_ROLE", "sendFinish", "", "context", "Landroid/content/Context;", "startIncoming", "role", "phoneNumber", "ringingCount", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void startIncoming(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String role, @org.jetbrains.annotations.Nullable()
        java.lang.String phoneNumber, int ringingCount) {
        }
        
        public final void sendFinish(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}