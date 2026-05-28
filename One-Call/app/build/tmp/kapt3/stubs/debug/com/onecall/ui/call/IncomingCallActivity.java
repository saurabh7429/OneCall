package com.onecall.ui.call;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J\b\u0010\r\u001a\u00020\nH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/onecall/ui/call/IncomingCallActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "audioManager", "Landroid/media/AudioManager;", "binding", "Lcom/onecall/databinding/ActivityIncomingCallBinding;", "dismissReceiver", "Landroid/content/BroadcastReceiver;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "Companion", "app_debug"})
public final class IncomingCallActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_DISMISS = "com.onecall.DISMISS_INCOMING_CALL";
    private com.onecall.databinding.ActivityIncomingCallBinding binding;
    private android.media.AudioManager audioManager;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver dismissReceiver = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.call.IncomingCallActivity.Companion Companion = null;
    
    public IncomingCallActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/onecall/ui/call/IncomingCallActivity$Companion;", "", "()V", "ACTION_DISMISS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}