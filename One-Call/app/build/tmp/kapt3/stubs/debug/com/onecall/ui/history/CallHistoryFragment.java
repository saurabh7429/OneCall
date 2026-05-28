package com.onecall.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.onecall.R;
import com.onecall.data.history.CallHistoryEntity;
import com.onecall.data.history.HistoryRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u001a\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0007H\u0002J\u0010\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0007H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/onecall/ui/history/CallHistoryFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/onecall/ui/history/CallHistoryAdapter;", "allHistoryList", "", "Lcom/onecall/data/history/CallHistoryEntity;", "btnClearAll", "Landroid/view/View;", "chipGroupFilter", "Lcom/google/android/material/chip/ChipGroup;", "repository", "Lcom/onecall/data/history/HistoryRepository;", "rvCallHistory", "Landroidx/recyclerview/widget/RecyclerView;", "tvEmptyHistory", "Landroid/widget/TextView;", "applyFilter", "", "checkedId", "", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "showCallbackDialog", "entry", "showDeleteDialog", "app_debug"})
public final class CallHistoryFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvCallHistory;
    private android.widget.TextView tvEmptyHistory;
    private android.view.View btnClearAll;
    private com.google.android.material.chip.ChipGroup chipGroupFilter;
    private com.onecall.data.history.HistoryRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.ui.history.CallHistoryAdapter adapter = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.onecall.data.history.CallHistoryEntity> allHistoryList;
    
    public CallHistoryFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void applyFilter(int checkedId) {
    }
    
    private final void showCallbackDialog(com.onecall.data.history.CallHistoryEntity entry) {
    }
    
    private final void showDeleteDialog(com.onecall.data.history.CallHistoryEntity entry) {
    }
}