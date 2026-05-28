package com.onecall.ui.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.telecom.TelecomManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onecall.R;
import com.onecall.data.ConnectedDevice;
import com.onecall.data.DeviceRoleStore;
import com.onecall.network.socket.OneCallConnectionManager;
import com.onecall.network.sip.SecondarySipClient;
import java.util.Locale;
import java.util.UUID;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u0000 =2\u00020\u0001:\u0001=B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\'\u001a\u00020(H\u0002J\u0010\u0010)\u001a\u00020(2\u0006\u0010*\u001a\u00020+H\u0002J\u0012\u0010,\u001a\u00020(2\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J\b\u0010/\u001a\u00020(H\u0014J\b\u00100\u001a\u00020(H\u0014J\b\u00101\u001a\u00020(H\u0014J\b\u00102\u001a\u00020(H\u0002J\u0010\u00103\u001a\u00020(2\u0006\u00104\u001a\u00020\u000fH\u0002J\b\u00105\u001a\u00020(H\u0002J\b\u00106\u001a\u00020(H\u0002J\b\u00107\u001a\u00020(H\u0002J\b\u00108\u001a\u00020(H\u0002J\b\u00109\u001a\u00020(H\u0002J\b\u0010:\u001a\u00020(H\u0002J\b\u0010;\u001a\u00020(H\u0002J\b\u0010<\u001a\u00020(H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006>"}, d2 = {"Lcom/onecall/ui/calls/CallActiveActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "audioManager", "Landroid/media/AudioManager;", "callEventsReceiver", "Landroid/content/BroadcastReceiver;", "callStartElapsed", "", "callTimerText", "Landroid/widget/TextView;", "callerName", "", "connectedDevices", "", "Lcom/onecall/data/ConnectedDevice;", "currentDeviceName", "deviceIndicatorText", "handler", "Landroid/os/Handler;", "isMuted", "", "isSpeakerOn", "muteButton", "Landroid/widget/ImageButton;", "pendingTransferRequestId", "pendingTransferTargetName", "phoneNumber", "role", "speakerButton", "timerRunnable", "Ljava/lang/Runnable;", "timerRunning", "transferButton", "transferDevicesContainer", "Landroid/widget/LinearLayout;", "transferDialog", "Lcom/google/android/material/bottomsheet/BottomSheetDialog;", "transferEmptyText", "endCall", "", "handleTransferResponse", "intent", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "renderTransferDevices", "requestTransfer", "device", "showTransferSheet", "startTimer", "stopTimer", "toggleMute", "toggleSpeaker", "updateMuteUi", "updateSpeakerUi", "updateTimer", "Companion", "app_debug"})
public final class CallActiveActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String role = "SECONDARY";
    @org.jetbrains.annotations.Nullable()
    private java.lang.String phoneNumber;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String callerName = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentDeviceName = "";
    private android.widget.TextView callTimerText;
    private android.widget.TextView deviceIndicatorText;
    private android.widget.ImageButton muteButton;
    private android.widget.ImageButton speakerButton;
    private android.widget.ImageButton transferButton;
    private android.media.AudioManager audioManager;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    private long callStartElapsed = 0L;
    private boolean timerRunning = false;
    private boolean isMuted = false;
    private boolean isSpeakerOn = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pendingTransferRequestId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pendingTransferTargetName;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.onecall.data.ConnectedDevice> connectedDevices;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.material.bottomsheet.BottomSheetDialog transferDialog;
    @org.jetbrains.annotations.Nullable()
    private android.widget.LinearLayout transferDevicesContainer;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView transferEmptyText;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver callEventsReceiver = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable timerRunnable = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_ROLE = "extra_role";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_CALLER_NAME = "extra_caller_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_PHONE_NUMBER = "extra_phone";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_CALL_START = "extra_call_start";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EXTRA_IS_OUTGOING = "extra_is_outgoing";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.calls.CallActiveActivity.Companion Companion = null;
    
    public CallActiveActivity() {
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
    
    private final void startTimer() {
    }
    
    private final void stopTimer() {
    }
    
    private final void updateTimer() {
    }
    
    private final void toggleMute() {
    }
    
    private final void toggleSpeaker() {
    }
    
    private final void updateMuteUi() {
    }
    
    private final void updateSpeakerUi() {
    }
    
    private final void showTransferSheet() {
    }
    
    private final void renderTransferDevices() {
    }
    
    private final void requestTransfer(com.onecall.data.ConnectedDevice device) {
    }
    
    private final void handleTransferResponse(android.content.Intent intent) {
    }
    
    private final void endCall() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ(\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\b\u0010\u0010\u001a\u0004\u0018\u00010\u0004J\u001e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/onecall/ui/calls/CallActiveActivity$Companion;", "", "()V", "EXTRA_CALLER_NAME", "", "EXTRA_CALL_START", "EXTRA_IS_OUTGOING", "EXTRA_PHONE_NUMBER", "EXTRA_ROLE", "sendFinish", "", "context", "Landroid/content/Context;", "start", "role", "callerName", "phoneNumber", "startOutgoing", "contactName", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void start(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String role, @org.jetbrains.annotations.NotNull()
        java.lang.String callerName, @org.jetbrains.annotations.Nullable()
        java.lang.String phoneNumber) {
        }
        
        public final void startOutgoing(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
        java.lang.String contactName) {
        }
        
        public final void sendFinish(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}