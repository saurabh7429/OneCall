package com.onecall.ui.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onecall.R;
import kotlinx.coroutines.Dispatchers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\t0\u0018H\u0002J\u0010\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u000fH\u0002J\b\u0010\u001b\u001a\u00020\u0016H\u0002J\u001a\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u000b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/onecall/ui/screens/ContactsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/onecall/ui/screens/ContactsAdapter;", "btnGrantPermission", "Landroid/widget/Button;", "contactsList", "", "Lcom/onecall/ui/screens/Contact;", "layoutPermissionDenied", "Landroid/view/View;", "progressContacts", "requestPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "rvContacts", "Landroidx/recyclerview/widget/RecyclerView;", "searchContacts", "Landroidx/appcompat/widget/SearchView;", "checkPermissionAndLoad", "", "fetchDeviceContacts", "", "filterContacts", "query", "loadContacts", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "showPermissionDenied", "app_debug"})
public final class ContactsFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvContacts;
    private android.view.View layoutPermissionDenied;
    private android.widget.Button btnGrantPermission;
    private androidx.appcompat.widget.SearchView searchContacts;
    private android.view.View progressContacts;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.onecall.ui.screens.Contact> contactsList = null;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.ui.screens.ContactsAdapter adapter = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPermissionLauncher = null;
    
    public ContactsFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void checkPermissionAndLoad() {
    }
    
    private final void showPermissionDenied() {
    }
    
    private final void loadContacts() {
    }
    
    private final java.util.List<com.onecall.ui.screens.Contact> fetchDeviceContacts() {
        return null;
    }
    
    private final void filterContacts(java.lang.String query) {
    }
}