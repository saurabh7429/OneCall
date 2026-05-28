package com.onecall.ui.screens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.google.android.material.button.MaterialButton;
import com.onecall.data.CodeLease;
import com.onecall.data.ConnectedDevice;
import com.onecall.network.socket.OneCallConnectionManager;
import com.onecall.service.SipServerService;
import com.onecall.R;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 =2\u00020\u0001:\u0002<=B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u000fH\u0002J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\b\u0010\u001b\u001a\u00020\u0017H\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u0017H\u0002J\b\u0010!\u001a\u00020\u001dH\u0002J\b\u0010\"\u001a\u00020\u0017H\u0002J\b\u0010#\u001a\u00020\u0017H\u0016J\b\u0010$\u001a\u00020\u0017H\u0016J\u001a\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\u0010\u0010*\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\rH\u0002J\n\u0010,\u001a\u0004\u0018\u00010\rH\u0002J\u0018\u0010-\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0018\u0010/\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u001d2\u0006\u00100\u001a\u00020\u000fH\u0002J\u0016\u00101\u001a\u00020\u00172\f\u00102\u001a\b\u0012\u0004\u0012\u00020403H\u0002J\u0010\u00105\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u001dH\u0002J\b\u00106\u001a\u00020\u0017H\u0002J\b\u00107\u001a\u00020\u0017H\u0002J\u0010\u00108\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u00109\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\rH\u0002J\f\u0010:\u001a\u00020;*\u00020;H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006>"}, d2 = {"Lcom/onecall/ui/screens/MainSetupFragment;", "Landroidx/fragment/app/Fragment;", "()V", "codeDigitsContainer", "Landroid/widget/LinearLayout;", "codeTimer", "Landroid/os/CountDownTimer;", "connectedDevicesContainer", "connectedDevicesEmptyText", "Landroid/widget/TextView;", "connectedDevicesHeadingText", "countdownText", "currentCodeState", "Lcom/onecall/ui/screens/MainSetupFragment$CodeState;", "expiredInThisSession", "", "expiredText", "mainCopyCodeButton", "Landroid/widget/ImageButton;", "regenerateButton", "Lcom/google/android/material/button/MaterialButton;", "shareButton", "applyStrikeThrough", "", "textView", "enabled", "cancelTimer", "copyCurrentCode", "formatRemainingTime", "", "remainingMillis", "", "generateAndShowNewCode", "generateRandomCode", "observeConnectedDevices", "onDestroyView", "onResume", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "persistCodeState", "codeState", "readStoredCodeState", "renderActiveCode", "code", "renderCodeDigits", "expired", "renderConnectedDevices", "devices", "", "Lcom/onecall/data/ConnectedDevice;", "renderExpiredCode", "restoreCodeState", "shareCurrentCode", "startTimer", "syncMainServer", "dpToPx", "", "CodeState", "Companion", "app_debug"})
public final class MainSetupFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private android.os.CountDownTimer codeTimer;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.ui.screens.MainSetupFragment.CodeState currentCodeState;
    private boolean expiredInThisSession = false;
    private android.widget.LinearLayout codeDigitsContainer;
    private android.widget.TextView countdownText;
    private android.widget.TextView expiredText;
    private com.google.android.material.button.MaterialButton regenerateButton;
    private com.google.android.material.button.MaterialButton shareButton;
    private android.widget.ImageButton mainCopyCodeButton;
    private android.widget.TextView connectedDevicesHeadingText;
    private android.widget.TextView connectedDevicesEmptyText;
    private android.widget.LinearLayout connectedDevicesContainer;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "main_setup_code_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CODE = "generated_code";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_GENERATED_AT = "generated_at_millis";
    private static final int CODE_LENGTH = 6;
    private static final long CODE_VALIDITY_MILLIS = 600000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.screens.MainSetupFragment.Companion Companion = null;
    
    public MainSetupFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void restoreCodeState() {
    }
    
    private final void generateAndShowNewCode() {
    }
    
    private final void renderActiveCode(java.lang.String code, long remainingMillis) {
    }
    
    private final void renderExpiredCode(java.lang.String code) {
    }
    
    private final void syncMainServer(com.onecall.ui.screens.MainSetupFragment.CodeState codeState) {
    }
    
    private final void observeConnectedDevices() {
    }
    
    private final void renderConnectedDevices(java.util.List<com.onecall.data.ConnectedDevice> devices) {
    }
    
    private final void renderCodeDigits(java.lang.String code, boolean expired) {
    }
    
    private final void applyStrikeThrough(android.widget.TextView textView, boolean enabled) {
    }
    
    private final void startTimer(long remainingMillis) {
    }
    
    private final void cancelTimer() {
    }
    
    private final void shareCurrentCode() {
    }
    
    private final void copyCurrentCode() {
    }
    
    private final com.onecall.ui.screens.MainSetupFragment.CodeState readStoredCodeState() {
        return null;
    }
    
    private final void persistCodeState(com.onecall.ui.screens.MainSetupFragment.CodeState codeState) {
    }
    
    private final java.lang.String generateRandomCode() {
        return null;
    }
    
    private final java.lang.String formatRemainingTime(long remainingMillis) {
        return null;
    }
    
    private final int dpToPx(int $this$dpToPx) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/onecall/ui/screens/MainSetupFragment$CodeState;", "", "code", "", "generatedAtMillis", "", "(Ljava/lang/String;J)V", "getCode", "()Ljava/lang/String;", "expiresAtMillis", "getExpiresAtMillis", "()J", "getGeneratedAtMillis", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class CodeState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String code = null;
        private final long generatedAtMillis = 0L;
        
        public CodeState(@org.jetbrains.annotations.NotNull()
        java.lang.String code, long generatedAtMillis) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCode() {
            return null;
        }
        
        public final long getGeneratedAtMillis() {
            return 0L;
        }
        
        public final long getExpiresAtMillis() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final long component2() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.MainSetupFragment.CodeState copy(@org.jetbrains.annotations.NotNull()
        java.lang.String code, long generatedAtMillis) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/onecall/ui/screens/MainSetupFragment$Companion;", "", "()V", "CODE_LENGTH", "", "CODE_VALIDITY_MILLIS", "", "KEY_CODE", "", "KEY_GENERATED_AT", "PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}