package com.onecall.ui.screens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.onecall.R;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0002+,B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00052\b\u0010\u0018\u001a\u0004\u0018\u00010\u0007H\u0002J\u0018\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u0016H\u0002J\b\u0010\u001e\u001a\u00020\u0016H\u0016J\b\u0010\u001f\u001a\u00020\u0016H\u0016J\u001a\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010%\u001a\u00020\u0016H\u0002J\b\u0010&\u001a\u00020\u0016H\u0002J\b\u0010\'\u001a\u00020\u0016H\u0002J\b\u0010(\u001a\u00020\u0016H\u0002J\f\u0010)\u001a\u00020\u001c*\u00020*H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2 = {"Lcom/onecall/ui/screens/PermissionsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "optionalPermissionSpecs", "", "Lcom/onecall/ui/screens/PermissionsFragment$PermissionSpec;", "optionalPermissionsContainer", "Landroid/widget/LinearLayout;", "permissionRowStates", "", "Lcom/onecall/ui/screens/PermissionsFragment$PermissionRowState;", "requiredPermissionSpecs", "requiredPermissionsContainer", "requiredPermissionsLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "", "warningBannerCard", "Lcom/google/android/material/card/MaterialCardView;", "warningBannerText", "Landroid/widget/TextView;", "addPermissionRow", "", "spec", "container", "bindPermissionRow", "rowState", "granted", "", "buildPermissionRows", "onDestroyView", "onResume", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "openAppSettings", "refreshPermissionState", "requestRequiredPermissions", "showSkipWarningDialog", "isIgnoringBatteryOptimizations", "Landroid/content/Context;", "PermissionRowState", "PermissionSpec", "app_debug"})
public final class PermissionsFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.onecall.ui.screens.PermissionsFragment.PermissionSpec> requiredPermissionSpecs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.onecall.ui.screens.PermissionsFragment.PermissionSpec> optionalPermissionSpecs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.onecall.ui.screens.PermissionsFragment.PermissionRowState> permissionRowStates = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.material.card.MaterialCardView warningBannerCard;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView warningBannerText;
    @org.jetbrains.annotations.Nullable()
    private android.widget.LinearLayout requiredPermissionsContainer;
    @org.jetbrains.annotations.Nullable()
    private android.widget.LinearLayout optionalPermissionsContainer;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> requiredPermissionsLauncher = null;
    
    public PermissionsFragment() {
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
    
    private final void buildPermissionRows() {
    }
    
    private final void addPermissionRow(com.onecall.ui.screens.PermissionsFragment.PermissionSpec spec, android.widget.LinearLayout container) {
    }
    
    private final void refreshPermissionState() {
    }
    
    private final void bindPermissionRow(com.onecall.ui.screens.PermissionsFragment.PermissionRowState rowState, boolean granted) {
    }
    
    private final void requestRequiredPermissions() {
    }
    
    private final void showSkipWarningDialog() {
    }
    
    private final void openAppSettings() {
    }
    
    private final boolean isIgnoringBatteryOptimizations(android.content.Context $this$isIgnoringBatteryOptimizations) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\r\u00a8\u0006\u001d"}, d2 = {"Lcom/onecall/ui/screens/PermissionsFragment$PermissionRowState;", "", "spec", "Lcom/onecall/ui/screens/PermissionsFragment$PermissionSpec;", "card", "Lcom/google/android/material/card/MaterialCardView;", "statusText", "Landroid/widget/TextView;", "optionalHintText", "(Lcom/onecall/ui/screens/PermissionsFragment$PermissionSpec;Lcom/google/android/material/card/MaterialCardView;Landroid/widget/TextView;Landroid/widget/TextView;)V", "getCard", "()Lcom/google/android/material/card/MaterialCardView;", "getOptionalHintText", "()Landroid/widget/TextView;", "getSpec", "()Lcom/onecall/ui/screens/PermissionsFragment$PermissionSpec;", "getStatusText", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    static final class PermissionRowState {
        @org.jetbrains.annotations.NotNull()
        private final com.onecall.ui.screens.PermissionsFragment.PermissionSpec spec = null;
        @org.jetbrains.annotations.NotNull()
        private final com.google.android.material.card.MaterialCardView card = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView statusText = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView optionalHintText = null;
        
        public PermissionRowState(@org.jetbrains.annotations.NotNull()
        com.onecall.ui.screens.PermissionsFragment.PermissionSpec spec, @org.jetbrains.annotations.NotNull()
        com.google.android.material.card.MaterialCardView card, @org.jetbrains.annotations.NotNull()
        android.widget.TextView statusText, @org.jetbrains.annotations.NotNull()
        android.widget.TextView optionalHintText) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.PermissionsFragment.PermissionSpec getSpec() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.material.card.MaterialCardView getCard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getStatusText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getOptionalHintText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.PermissionsFragment.PermissionSpec component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.material.card.MaterialCardView component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.PermissionsFragment.PermissionRowState copy(@org.jetbrains.annotations.NotNull()
        com.onecall.ui.screens.PermissionsFragment.PermissionSpec spec, @org.jetbrains.annotations.NotNull()
        com.google.android.material.card.MaterialCardView card, @org.jetbrains.annotations.NotNull()
        android.widget.TextView statusText, @org.jetbrains.annotations.NotNull()
        android.widget.TextView optionalHintText) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0015\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\u000bH\u00c6\u0003J]\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\u000bH\u00c6\u0001J\u0013\u0010 \u001a\u00020\u00072\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u001d\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000f\u00a8\u0006%"}, d2 = {"Lcom/onecall/ui/screens/PermissionsFragment$PermissionSpec;", "", "permission", "", "title", "description", "required", "", "requestable", "optionalDeniedHint", "statusChecker", "Lkotlin/Function1;", "Landroid/content/Context;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZLjava/lang/String;Lkotlin/jvm/functions/Function1;)V", "getDescription", "()Ljava/lang/String;", "getOptionalDeniedHint", "getPermission", "getRequestable", "()Z", "getRequired", "getStatusChecker", "()Lkotlin/jvm/functions/Function1;", "getTitle", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
    static final class PermissionSpec {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String permission = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String title = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String description = null;
        private final boolean required = false;
        private final boolean requestable = false;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String optionalDeniedHint = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<android.content.Context, java.lang.Boolean> statusChecker = null;
        
        public PermissionSpec(@org.jetbrains.annotations.NotNull()
        java.lang.String permission, @org.jetbrains.annotations.NotNull()
        java.lang.String title, @org.jetbrains.annotations.NotNull()
        java.lang.String description, boolean required, boolean requestable, @org.jetbrains.annotations.Nullable()
        java.lang.String optionalDeniedHint, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super android.content.Context, java.lang.Boolean> statusChecker) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getPermission() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTitle() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDescription() {
            return null;
        }
        
        public final boolean getRequired() {
            return false;
        }
        
        public final boolean getRequestable() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getOptionalDeniedHint() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function1<android.content.Context, java.lang.Boolean> getStatusChecker() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        public final boolean component4() {
            return false;
        }
        
        public final boolean component5() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function1<android.content.Context, java.lang.Boolean> component7() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.ui.screens.PermissionsFragment.PermissionSpec copy(@org.jetbrains.annotations.NotNull()
        java.lang.String permission, @org.jetbrains.annotations.NotNull()
        java.lang.String title, @org.jetbrains.annotations.NotNull()
        java.lang.String description, boolean required, boolean requestable, @org.jetbrains.annotations.Nullable()
        java.lang.String optionalDeniedHint, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super android.content.Context, java.lang.Boolean> statusChecker) {
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
}