package com.onecall.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import com.onecall.data.history.HistoryDao;
import com.onecall.data.history.HistoryDatabase;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0011\u0018\u0000 42\u00020\u0001:\u00014B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010 J\u0016\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001e0\"2\u0006\u0010\u001f\u001a\u00020\u0014J\u0016\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010&J\u000e\u0010\'\u001a\u00020$2\u0006\u0010(\u001a\u00020\bJ\u000e\u0010)\u001a\u00020$2\u0006\u0010(\u001a\u00020\bJ\u000e\u0010*\u001a\u00020$2\u0006\u0010+\u001a\u00020\u000eJ\u000e\u0010,\u001a\u00020$2\u0006\u0010-\u001a\u00020\u000eJ\u000e\u0010.\u001a\u00020$2\u0006\u0010/\u001a\u00020\u0014J\u000e\u00100\u001a\u00020$2\u0006\u00101\u001a\u00020\u0014J\u000e\u00102\u001a\u00020$2\u0006\u00103\u001a\u00020\bR\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0013\u001a\u00020\u00148F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u00148F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\u0019\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\nR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcom/onecall/data/settings/SettingsRepository;", "", "context", "Landroid/content/Context;", "historyDao", "Lcom/onecall/data/history/HistoryDao;", "(Landroid/content/Context;Lcom/onecall/data/history/HistoryDao;)V", "autoApproveOutgoing", "", "getAutoApproveOutgoing", "()Z", "autoReconnect", "getAutoReconnect", "codeExpiryTimeMinutes", "", "getCodeExpiryTimeMinutes", "()I", "maxDevices", "getMaxDevices", "myDeviceIcon", "", "getMyDeviceIcon", "()Ljava/lang/String;", "myDeviceName", "getMyDeviceName", "notifyOutgoingOnMain", "getNotifyOutgoingOnMain", "prefs", "Landroid/content/SharedPreferences;", "getDeviceConfig", "Lcom/onecall/data/settings/DeviceConfigEntity;", "deviceId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDeviceConfigFlow", "Lkotlinx/coroutines/flow/Flow;", "saveDeviceConfig", "", "config", "(Lcom/onecall/data/settings/DeviceConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setAutoApproveOutgoing", "auto", "setAutoReconnect", "setCodeExpiryTimeMinutes", "minutes", "setMaxDevices", "max", "setMyDeviceIcon", "icon", "setMyDeviceName", "name", "setNotifyOutgoingOnMain", "notify", "Companion", "app_debug"})
public final class SettingsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.data.history.HistoryDao historyDao = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.onecall.data.settings.SettingsRepository INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.data.settings.SettingsRepository.Companion Companion = null;
    
    private SettingsRepository(android.content.Context context, com.onecall.data.history.HistoryDao historyDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMyDeviceName() {
        return null;
    }
    
    public final void setMyDeviceName(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMyDeviceIcon() {
        return null;
    }
    
    public final void setMyDeviceIcon(@org.jetbrains.annotations.NotNull()
    java.lang.String icon) {
    }
    
    public final int getMaxDevices() {
        return 0;
    }
    
    public final void setMaxDevices(int max) {
    }
    
    public final boolean getAutoReconnect() {
        return false;
    }
    
    public final void setAutoReconnect(boolean auto) {
    }
    
    public final int getCodeExpiryTimeMinutes() {
        return 0;
    }
    
    public final void setCodeExpiryTimeMinutes(int minutes) {
    }
    
    public final boolean getAutoApproveOutgoing() {
        return false;
    }
    
    public final void setAutoApproveOutgoing(boolean auto) {
    }
    
    public final boolean getNotifyOutgoingOnMain() {
        return false;
    }
    
    public final void setNotifyOutgoingOnMain(boolean notify) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.onecall.data.settings.DeviceConfigEntity> getDeviceConfigFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getDeviceConfig(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.data.settings.DeviceConfigEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveDeviceConfig(@org.jetbrains.annotations.NotNull()
    com.onecall.data.settings.DeviceConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/onecall/data/settings/SettingsRepository$Companion;", "", "()V", "INSTANCE", "Lcom/onecall/data/settings/SettingsRepository;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.data.settings.SettingsRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}