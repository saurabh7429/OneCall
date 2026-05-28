package com.onecall.ui.call;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\nH\u0002J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u0016H\u0014J\b\u0010\u001a\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/onecall/ui/call/ActiveCallActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "audioManager", "Landroid/media/AudioManager;", "binding", "Lcom/onecall/databinding/ActivityActiveCallBinding;", "callEndedReceiver", "Landroid/content/BroadcastReceiver;", "elapsedSeconds", "", "isMuted", "", "isSpeaker", "timerHandler", "Landroid/os/Handler;", "timerRunnable", "Ljava/lang/Runnable;", "formatTime", "", "seconds", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "showTransferDialog", "app_debug"})
public final class ActiveCallActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.onecall.databinding.ActivityActiveCallBinding binding;
    private android.media.AudioManager audioManager;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler timerHandler = null;
    private int elapsedSeconds = 0;
    private boolean isMuted = false;
    private boolean isSpeaker = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable timerRunnable = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver callEndedReceiver = null;
    
    public ActiveCallActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void showTransferDialog() {
    }
    
    private final java.lang.String formatTime(int seconds) {
        return null;
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}