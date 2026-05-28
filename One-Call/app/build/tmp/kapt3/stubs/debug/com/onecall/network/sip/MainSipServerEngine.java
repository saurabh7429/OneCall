package com.onecall.network.sip;

import android.content.Context;
import android.telecom.TelecomManager;
import android.util.Log;
import com.onecall.data.ConnectedDevice;
import local.server.Proxy;
import local.server.ServerProfile;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.call.CallListener;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.message.SipResponses;
import org.zoolu.sip.provider.SipProvider;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 (2\u00020\u0001:\u0002()B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0007J\u0010\u0010\u0015\u001a\u00020\u00132\b\u0010\u0016\u001a\u0004\u0018\u00010\u0007J\u0012\u0010\u0017\u001a\u00020\u00072\b\u0010\u0016\u001a\u0004\u0018\u00010\u0007H\u0002J\b\u0010\u0018\u001a\u00020\u0007H\u0002J\b\u0010\u0019\u001a\u00020\u0007H\u0002J\u0010\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\bH\u0002J\u0006\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0007J\b\u0010\u001f\u001a\u00020\u0013H\u0002J\u0006\u0010 \u001a\u00020\nJ\b\u0010!\u001a\u00020\u0013H\u0002J\u0006\u0010\"\u001a\u00020\u0013J\b\u0010#\u001a\u00020\u0013H\u0002J\u0014\u0010$\u001a\u00020\u00132\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\'0&R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0018\u00010\u000fR\u00020\u0000X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/onecall/network/sip/MainSipServerEngine;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "activeCalls", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lorg/zoolu/sip/call/Call;", "callAccepted", "", "localIp", "pendingOutgoingRequests", "Lorg/zoolu/sip/message/Message;", "proxy", "Lcom/onecall/network/sip/MainSipServerEngine$OneCallSipProxy;", "sipProvider", "Lorg/zoolu/sip/provider/SipProvider;", "approvePendingOutgoingCall", "", "number", "broadcastIncomingCall", "phoneNumber", "buildCallerSipAddress", "buildMainSipAddress", "buildMainSipContact", "cancelOtherCalls", "acceptedCall", "getRegisteredSecondaryCount", "", "rejectPendingOutgoingCall", "rejectRealCall", "start", "startRtpBridge", "stop", "stopRtpBridge", "updateConnectedDevices", "devices", "", "Lcom/onecall/data/ConnectedDevice;", "Companion", "OneCallSipProxy", "app_debug"})
public final class MainSipServerEngine {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private org.zoolu.sip.provider.SipProvider sipProvider;
    @org.jetbrains.annotations.Nullable()
    private com.onecall.network.sip.MainSipServerEngine.OneCallSipProxy proxy;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String localIp;
    @org.jetbrains.annotations.NotNull()
    private java.util.concurrent.ConcurrentHashMap<java.lang.String, org.zoolu.sip.call.Call> activeCalls;
    private boolean callAccepted = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, org.zoolu.sip.message.Message> pendingOutgoingRequests = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OneCallSipServer";
    private static final int SIP_PORT = 5060;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.network.sip.MainSipServerEngine.Companion Companion = null;
    
    public MainSipServerEngine(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final boolean start() {
        return false;
    }
    
    public final void stop() {
    }
    
    public final void broadcastIncomingCall(@org.jetbrains.annotations.Nullable()
    java.lang.String phoneNumber) {
    }
    
    public final int getRegisteredSecondaryCount() {
        return 0;
    }
    
    public final void updateConnectedDevices(@org.jetbrains.annotations.NotNull()
    java.util.List<com.onecall.data.ConnectedDevice> devices) {
    }
    
    private final void cancelOtherCalls(org.zoolu.sip.call.Call acceptedCall) {
    }
    
    private final void startRtpBridge() {
    }
    
    private final void stopRtpBridge() {
    }
    
    private final void rejectRealCall() {
    }
    
    private final java.lang.String buildMainSipAddress() {
        return null;
    }
    
    private final java.lang.String buildMainSipContact() {
        return null;
    }
    
    private final java.lang.String buildCallerSipAddress(java.lang.String phoneNumber) {
        return null;
    }
    
    public final void approvePendingOutgoingCall(@org.jetbrains.annotations.NotNull()
    java.lang.String number) {
    }
    
    public final void rejectPendingOutgoingCall(@org.jetbrains.annotations.NotNull()
    java.lang.String number) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/onecall/network/sip/MainSipServerEngine$Companion;", "", "()V", "SIP_PORT", "", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/onecall/network/sip/MainSipServerEngine$OneCallSipProxy;", "Llocal/server/Proxy;", "provider", "Lorg/zoolu/sip/provider/SipProvider;", "profile", "Llocal/server/ServerProfile;", "(Lcom/onecall/network/sip/MainSipServerEngine;Lorg/zoolu/sip/provider/SipProvider;Llocal/server/ServerProfile;)V", "getRegisteredContactUrls", "", "", "processRequestToLocalUser", "", "msg", "Lorg/zoolu/sip/message/Message;", "app_debug"})
    final class OneCallSipProxy extends local.server.Proxy {
        
        public OneCallSipProxy(@org.jetbrains.annotations.NotNull()
        org.zoolu.sip.provider.SipProvider provider, @org.jetbrains.annotations.NotNull()
        local.server.ServerProfile profile) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getRegisteredContactUrls() {
            return null;
        }
        
        @java.lang.Override()
        public void processRequestToLocalUser(@org.jetbrains.annotations.NotNull()
        org.zoolu.sip.message.Message msg) {
        }
    }
}