package com.onecall.network.sip;

import android.content.Context;
import android.util.Log;
import com.onecall.data.SecondaryConnectionRecord;
import com.onecall.data.DeviceRoleStore;
import com.onecall.network.socket.OneCallConnectionManager;
import com.onecall.ui.calls.CallActiveActivity;
import com.onecall.ui.calls.IncomingCallActivity;
import local.ua.UserAgent;
import local.ua.UserAgentListener;
import local.ua.UserAgentProfile;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.provider.RegisterAgent;
import org.zoolu.sip.provider.RegisterAgentListener;
import org.zoolu.sip.provider.SipProvider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u0010\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J \u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0016J\u0010\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J\u0010\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000fH\u0016J(\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u0007H\u0016J(\u0010#\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u0007H\u0016J\u0016\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u0007J\u0016\u0010\'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020*J\u0006\u0010+\u001a\u00020\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/onecall/network/sip/SecondarySipClient;", "Lorg/zoolu/sip/provider/RegisterAgentListener;", "Llocal/ua/UserAgentListener;", "()V", "SIP_CLIENT_PORT", "", "TAG", "", "appContext", "Landroid/content/Context;", "registerAgent", "Lorg/zoolu/sip/provider/RegisterAgent;", "sipProvider", "Lorg/zoolu/sip/provider/SipProvider;", "userAgent", "Llocal/ua/UserAgent;", "acceptIncomingCall", "", "declineIncomingCall", "onUaCallAccepted", "ua", "onUaCallCancelled", "onUaCallClosed", "onUaCallFailed", "onUaCallIncoming", "callee", "Lorg/zoolu/sip/address/NameAddress;", "caller", "onUaCallRinging", "onUaCallTrasferred", "onUaRegistrationFailure", "ra", "target", "contact", "result", "onUaRegistrationSuccess", "placeOutgoingCall", "number", "mainHost", "start", "context", "record", "Lcom/onecall/data/SecondaryConnectionRecord;", "stop", "app_debug"})
public final class SecondarySipClient implements org.zoolu.sip.provider.RegisterAgentListener, local.ua.UserAgentListener {
    @org.jetbrains.annotations.Nullable()
    private static org.zoolu.sip.provider.SipProvider sipProvider;
    @org.jetbrains.annotations.Nullable()
    private static org.zoolu.sip.provider.RegisterAgent registerAgent;
    @org.jetbrains.annotations.Nullable()
    private static local.ua.UserAgent userAgent;
    @org.jetbrains.annotations.Nullable()
    private static android.content.Context appContext;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OneCallSipClient";
    private static final int SIP_CLIENT_PORT = 5072;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.network.sip.SecondarySipClient INSTANCE = null;
    
    private SecondarySipClient() {
        super();
    }
    
    public final void start(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.onecall.data.SecondaryConnectionRecord record) {
    }
    
    public final void stop() {
    }
    
    public final void acceptIncomingCall() {
    }
    
    public final void declineIncomingCall() {
    }
    
    public final void placeOutgoingCall(@org.jetbrains.annotations.NotNull()
    java.lang.String number, @org.jetbrains.annotations.NotNull()
    java.lang.String mainHost) {
    }
    
    @java.lang.Override()
    public void onUaRegistrationSuccess(@org.jetbrains.annotations.NotNull()
    org.zoolu.sip.provider.RegisterAgent ra, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress target, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress contact, @org.jetbrains.annotations.NotNull()
    java.lang.String result) {
    }
    
    @java.lang.Override()
    public void onUaRegistrationFailure(@org.jetbrains.annotations.NotNull()
    org.zoolu.sip.provider.RegisterAgent ra, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress target, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress contact, @org.jetbrains.annotations.NotNull()
    java.lang.String result) {
    }
    
    @java.lang.Override()
    public void onUaCallIncoming(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress callee, @org.jetbrains.annotations.NotNull()
    org.zoolu.sip.address.NameAddress caller) {
    }
    
    @java.lang.Override()
    public void onUaCallCancelled(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
    
    @java.lang.Override()
    public void onUaCallRinging(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
    
    @java.lang.Override()
    public void onUaCallAccepted(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
    
    @java.lang.Override()
    public void onUaCallTrasferred(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
    
    @java.lang.Override()
    public void onUaCallFailed(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
    
    @java.lang.Override()
    public void onUaCallClosed(@org.jetbrains.annotations.NotNull()
    local.ua.UserAgent ua) {
    }
}