package com.onecall.data;

import android.content.Context;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/onecall/data/DeviceRoleStore;", "", "()V", "KEY_ROLE", "", "PREFS_NAME", "ROLE_MAIN", "ROLE_SECONDARY", "getRole", "context", "Landroid/content/Context;", "isMain", "", "setRole", "", "role", "app_debug"})
public final class DeviceRoleStore {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "onecall_device_role";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ROLE = "device_role";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ROLE_MAIN = "MAIN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ROLE_SECONDARY = "SECONDARY";
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.data.DeviceRoleStore INSTANCE = null;
    
    private DeviceRoleStore() {
        super();
    }
    
    public final void setRole(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String role) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRole(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final boolean isMain(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
}