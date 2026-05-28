package com.onecall.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.onecall.R;
import com.onecall.data.settings.DeviceConfigEntity;
import com.onecall.data.settings.SettingsRepository;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u001a\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u0018\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J\u0018\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001aH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/onecall/ui/screens/DeviceSettingsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "currentConfig", "Lcom/onecall/data/settings/DeviceConfigEntity;", "deviceId", "", "iconTypes", "", "[Ljava/lang/String;", "settingsRepository", "Lcom/onecall/data/settings/SettingsRepository;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Landroid/view/View;", "showTimePicker", "button", "Lcom/google/android/material/button/MaterialButton;", "prefix", "updateStopCallsButton", "btn", "isPaused", "", "Companion", "app_debug"})
public final class DeviceSettingsFragment extends androidx.fragment.app.Fragment {
    private java.lang.String deviceId;
    private com.onecall.data.settings.SettingsRepository settingsRepository;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.data.settings.DeviceConfigEntity currentConfig;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String[] iconTypes = {"PHONE", "TABLET", "FRIDGE", "TV", "OTHER"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ARG_DEVICE_ID = "device_id";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.ui.screens.DeviceSettingsFragment.Companion Companion = null;
    
    public DeviceSettingsFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void updateStopCallsButton(com.google.android.material.button.MaterialButton btn, boolean isPaused) {
    }
    
    private final void showTimePicker(com.google.android.material.button.MaterialButton button, java.lang.String prefix) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/onecall/ui/screens/DeviceSettingsFragment$Companion;", "", "()V", "ARG_DEVICE_ID", "", "newInstance", "Lcom/onecall/ui/screens/DeviceSettingsFragment;", "deviceId", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.DeviceSettingsFragment newInstance(@org.jetbrains.annotations.NotNull()
        java.lang.String deviceId) {
            return null;
        }
    }
}