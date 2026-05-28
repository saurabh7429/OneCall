package com.onecall.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u0004\u0018\u00010\nJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0012\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\u0011H\u0014J\b\u0010\u0016\u001a\u00020\u0011H\u0014J\b\u0010\u0017\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/onecall/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/onecall/databinding/ActivityMainBinding;", "devicePrefs", "Lcom/onecall/core/BluetoothDevicePreference;", "navController", "Landroidx/navigation/NavController;", "oneCallService", "Lcom/onecall/service/OneCallService;", "serviceBound", "", "serviceConnection", "Landroid/content/ServiceConnection;", "getOneCallService", "navigateToDashboard", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onStart", "onStop", "startOneCallService", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.onecall.databinding.ActivityMainBinding binding;
    private androidx.navigation.NavController navController;
    private com.onecall.core.BluetoothDevicePreference devicePrefs;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.service.OneCallService oneCallService;
    private boolean serviceBound = false;
    @org.jetbrains.annotations.NotNull()
    private final android.content.ServiceConnection serviceConnection = null;
    
    public MainActivity() {
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
    
    private final void startOneCallService() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.onecall.service.OneCallService getOneCallService() {
        return null;
    }
    
    public final void navigateToDashboard() {
    }
}