package com.onecall.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.onecall.R;
import com.onecall.network.socket.OneCallConnectionManager;
import com.onecall.network.sip.SecondarySipClient;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0015H\u0002J\n\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0002J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001d\u001a\u00020\u0015H\u0016J\u001a\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\n2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u0010\u0010\"\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u0018H\u0002J\b\u0010$\u001a\u00020\u0015H\u0002J\u0010\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u0018H\u0002J\b\u0010\'\u001a\u00020\u0015H\u0002J\b\u0010(\u001a\u00020\u0015H\u0002J\b\u0010)\u001a\u00020\u0015H\u0002J\f\u0010*\u001a\u00020\u001b*\u00020\u001bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/onecall/ui/screens/SecondarySetupFragment;", "Landroidx/fragment/app/Fragment;", "()V", "codeEntryCard", "Lcom/google/android/material/card/MaterialCardView;", "codeInputsContainer", "Landroid/widget/LinearLayout;", "connectButton", "Lcom/google/android/material/button/MaterialButton;", "connectingContainer", "Landroid/view/View;", "connectingText", "Landroid/widget/TextView;", "digitInputs", "", "Lcom/onecall/ui/screens/DigitCodeEditText;", "isConnected", "", "isConnecting", "statusText", "buildDigitInputs", "", "clearErrorState", "collectCode", "", "moveToNextInput", "index", "", "moveToPreviousInput", "onDestroyView", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "setConnectedState", "mainDeviceName", "setConnectingState", "setErrorState", "message", "setInfoState", "startConnectionAttempt", "updateConnectButtonState", "dpToPx", "app_debug"})
public final class SecondarySetupFragment extends androidx.fragment.app.Fragment {
    private com.google.android.material.card.MaterialCardView codeEntryCard;
    private android.widget.LinearLayout codeInputsContainer;
    private android.widget.TextView statusText;
    private android.view.View connectingContainer;
    private android.widget.TextView connectingText;
    private com.google.android.material.button.MaterialButton connectButton;
    private java.util.List<com.onecall.ui.screens.DigitCodeEditText> digitInputs;
    private boolean isConnecting = false;
    private boolean isConnected = false;
    
    public SecondarySetupFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void buildDigitInputs() {
    }
    
    private final void startConnectionAttempt() {
    }
    
    private final void setInfoState() {
    }
    
    private final void setConnectingState() {
    }
    
    private final void setErrorState(java.lang.String message) {
    }
    
    private final void setConnectedState(java.lang.String mainDeviceName) {
    }
    
    private final void clearErrorState() {
    }
    
    private final void updateConnectButtonState() {
    }
    
    private final java.lang.String collectCode() {
        return null;
    }
    
    private final void moveToNextInput(int index) {
    }
    
    private final void moveToPreviousInput(int index) {
    }
    
    private final int dpToPx(int $this$dpToPx) {
        return 0;
    }
}