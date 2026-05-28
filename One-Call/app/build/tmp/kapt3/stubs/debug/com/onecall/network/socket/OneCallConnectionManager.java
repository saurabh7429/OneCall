package com.onecall.network.socket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.telecom.TelecomManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.onecall.data.CodeLease;
import com.onecall.data.ConnectedDevice;
import com.onecall.data.SecondaryConnectionRecord;
import com.onecall.ui.calls.CallTransferConstants;
import com.onecall.ui.calls.CallTransferNotifications;
import kotlinx.coroutines.Dispatchers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ca\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u001b\b\u00c6\u0002\u0018\u00002\u00020\u0001:\b\u009d\u0001\u009e\u0001\u009f\u0001\u00a0\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010N\u001a\u00020O2\u0006\u0010P\u001a\u00020=H\u0082@\u00a2\u0006\u0002\u0010QJ\u0010\u0010R\u001a\u00020O2\u0006\u0010S\u001a\u00020\u0006H\u0002J\u001e\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020&2\u0006\u0010W\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010XJ\u0006\u0010Y\u001a\u00020\u0006J\b\u0010Z\u001a\u00020=H\u0002J\u0010\u0010[\u001a\u00020\t2\u0006\u0010\\\u001a\u00020\tH\u0002J\u000e\u0010]\u001a\u00020O2\u0006\u0010^\u001a\u00020\tJ\u000e\u0010_\u001a\u00020O2\u0006\u0010V\u001a\u00020&J2\u0010`\u001a\u0004\u0018\u00010a2\u0006\u0010V\u001a\u00020&2\u0006\u0010W\u001a\u00020\t2\u0006\u0010b\u001a\u00020\t2\u0006\u0010c\u001a\u00020\t2\u0006\u0010d\u001a\u00020\tH\u0002J\u0012\u0010e\u001a\u00020\t2\b\u0010\\\u001a\u0004\u0018\u00010\tH\u0002J\b\u0010f\u001a\u00020OH\u0002J\u0016\u0010g\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0006\u0010h\u001a\u000201J\u0010\u0010i\u001a\u0004\u0018\u00010j2\u0006\u0010V\u001a\u00020&J\u000e\u0010k\u001a\u00020\t2\u0006\u0010V\u001a\u00020&J\u000e\u0010l\u001a\u00020\t2\u0006\u0010V\u001a\u00020&J\u000e\u0010m\u001a\u00020\t2\u0006\u0010V\u001a\u00020&J\u000e\u0010n\u001a\u00020\t2\u0006\u0010V\u001a\u00020&J\u0018\u0010o\u001a\u00020O2\u0006\u0010p\u001a\u0002092\u0006\u0010q\u001a\u00020rH\u0002J\b\u0010s\u001a\u00020OH\u0002J\u0016\u0010t\u001a\u00020O2\u0006\u0010p\u001a\u00020\"H\u0082@\u00a2\u0006\u0002\u0010uJ\u001e\u0010v\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\f\u0010w\u001a\b\u0012\u0004\u0012\u00020\t0)H\u0002J\u0016\u0010x\u001a\u00020O2\f\u0010w\u001a\b\u0012\u0004\u0012\u00020\t0)H\u0002J\u0010\u0010y\u001a\u00020O2\u0006\u0010V\u001a\u00020&H\u0002J\u000e\u0010z\u001a\u00020H2\u0006\u0010V\u001a\u00020&J\u000e\u0010{\u001a\u00020H2\u0006\u0010^\u001a\u00020\tJ\u0006\u0010|\u001a\u00020\u0006J\u0010\u0010}\u001a\u00020O2\u0006\u0010V\u001a\u00020&H\u0002J\u0016\u0010~\u001a\u00020O2\u0006\u0010p\u001a\u000209H\u0082@\u00a2\u0006\u0002\u0010\u007fJ\t\u0010\u0080\u0001\u001a\u00020\tH\u0002J\u0011\u0010\u0081\u0001\u001a\u00020\t2\u0006\u0010V\u001a\u00020&H\u0002J\t\u0010\u0082\u0001\u001a\u00020\tH\u0002J\u0011\u0010\u0083\u0001\u001a\u00020\t2\u0006\u0010V\u001a\u00020&H\u0002J*\u0010\u0084\u0001\u001a\u00020O2\u0006\u0010p\u001a\u00020\"2\u0006\u0010^\u001a\u00020\t2\b\u0010\u0085\u0001\u001a\u00030\u0086\u0001H\u0082@\u00a2\u0006\u0003\u0010\u0087\u0001J*\u0010\u0088\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0006\u0010p\u001a\u00020\"2\b\u0010\u0085\u0001\u001a\u00030\u0086\u0001H\u0082@\u00a2\u0006\u0003\u0010\u0089\u0001J\u0011\u0010\u008a\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&H\u0002J\u001a\u0010\u008b\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0007\u0010\u008c\u0001\u001a\u00020jH\u0002J\u000f\u0010\u008d\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&J\t\u0010\u008e\u0001\u001a\u00020OH\u0002J?\u0010\u008f\u0001\u001a\u00020H2\u0006\u0010V\u001a\u00020&2\u0007\u0010\u0090\u0001\u001a\u00020\t2\u0006\u0010b\u001a\u00020\t2\t\u0010\u0091\u0001\u001a\u0004\u0018\u00010\t2\t\u0010\u0092\u0001\u001a\u0004\u0018\u00010\t2\u0007\u0010\u0093\u0001\u001a\u00020\tJ \u0010\u0094\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0006\u0010b\u001a\u00020\t2\u0007\u0010\u0095\u0001\u001a\u00020HJ#\u0010\u0096\u0001\u001a\u00020O2\u0006\u0010b\u001a\u00020\t2\u0007\u0010\u0097\u0001\u001a\u00020\t2\u0007\u0010\u0098\u0001\u001a\u00020\tH\u0002J\u0018\u0010\u0099\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0007\u0010\u009a\u0001\u001a\u00020HJ\t\u0010\u009b\u0001\u001a\u00020OH\u0002J\u0017\u0010\u009c\u0001\u001a\u00020O2\u0006\u0010V\u001a\u00020&2\u0006\u0010h\u001a\u000201R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020 0\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0)0(\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u001a\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0)0.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00106\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00107\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00108\u001a\u0004\u0018\u000109X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010<\u001a\u0004\u0018\u00010=X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u0004\u0018\u00010@X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010A\u001a\u0004\u0018\u00010BX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000RN\u0010E\u001aB\u0012\f\u0012\n G*\u0004\u0018\u00010\t0\t\u0012\f\u0012\n G*\u0004\u0018\u00010H0H G* \u0012\f\u0012\n G*\u0004\u0018\u00010\t0\t\u0012\f\u0012\n G*\u0004\u0018\u00010H0H\u0018\u00010F0FX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020JX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00060(\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010,R\u001c\u0010M\u001a\u0010\u0012\f\u0012\n G*\u0004\u0018\u00010\u00060\u00060.X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u00a1\u0001"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager;", "", "()V", "AUTO_RECONNECT_DELAY_MILLIS", "", "DISCOVERY_PORT", "", "DISCOVERY_TIMEOUT_MILLIS", "KEY_MAIN_CODE", "", "KEY_MAIN_CODE_GENERATED_AT", "KEY_MAIN_CODE_VALIDITY", "KEY_MAIN_DEVICE_ID", "KEY_MAIN_DEVICE_NAME", "KEY_MAIN_TCP_PORT", "KEY_SECONDARY_AUTO_RECONNECT", "KEY_SECONDARY_DEVICE_ID", "KEY_SECONDARY_DEVICE_NAME", "KEY_SECONDARY_LAST_CODE", "KEY_SECONDARY_LAST_CONNECTED_AT", "KEY_SECONDARY_LAST_HOST", "KEY_SECONDARY_LAST_MAIN_DEVICE_ID", "KEY_SECONDARY_LAST_MAIN_NAME", "KEY_SECONDARY_LAST_PORT", "KEY_SECONDARY_LAST_WIFI", "MAX_CONNECTED_DEVICES", "PREFERRED_TCP_PORT", "PREFS_NAME", "acceptJob", "Lkotlinx/coroutines/Job;", "activeConnections", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/onecall/network/socket/OneCallConnectionManager$ActiveConnection;", "activeSecondarySocket", "Ljava/net/Socket;", "activeSecondaryWriter", "Ljava/io/BufferedWriter;", "appContext", "Landroid/content/Context;", "connectedDevices", "Landroidx/lifecycle/LiveData;", "", "Lcom/onecall/data/ConnectedDevice;", "getConnectedDevices", "()Landroidx/lifecycle/LiveData;", "connectedDevicesInternal", "Landroidx/lifecycle/MutableLiveData;", "connectionLock", "currentCodeLease", "Lcom/onecall/data/CodeLease;", "currentMainDeviceId", "currentMainDeviceName", "currentMainTcpPort", "currentSecondaryDeviceId", "currentSecondaryDeviceName", "discoveryJob", "discoverySocket", "Ljava/net/DatagramSocket;", "mainServerReady", "Ljava/util/concurrent/atomic/AtomicBoolean;", "mainServerSocket", "Ljava/net/ServerSocket;", "manualDisconnectRequested", "networkCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "preferences", "Landroid/content/SharedPreferences;", "reconnectInProgress", "reconnectMonitorRegistered", "ringingPausedDeviceIds", "Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", "kotlin.jvm.PlatformType", "", "scope", "Lkotlinx/coroutines/CoroutineScope;", "secondaryDeviceCount", "getSecondaryDeviceCount", "secondaryDeviceCountInternal", "acceptConnectionsLoop", "", "serverSocket", "(Ljava/net/ServerSocket;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "broadcastDeviceCount", "count", "connectSecondary", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "context", "code", "(Landroid/content/Context;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connectedDeviceCount", "createTcpServerSocket", "decodeField", "value", "disconnectDevice", "deviceId", "disconnectSecondary", "discoverMainDevice", "Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome;", "requestId", "secondaryDeviceId", "secondaryDeviceName", "encodeField", "endRealCallOnMain", "ensureMainServerRunning", "codeLease", "getLastSecondaryConnectionRecord", "Lcom/onecall/data/SecondaryConnectionRecord;", "getMainDeviceId", "getMainDeviceName", "getSecondaryDeviceId", "getSecondaryDeviceName", "handleDiscoveryPacket", "socket", "packet", "Ljava/net/DatagramPacket;", "handleEndCallRequest", "handleMainTcpClient", "(Ljava/net/Socket;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleTransferRequest", "parts", "handleTransferResponse", "initialize", "isAutoReconnectEnabled", "isRingingPaused", "lastKnownDeviceCount", "launchAutoReconnect", "listenForDiscoveryLoop", "(Ljava/net/DatagramSocket;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadOrCreateMainDeviceId", "loadOrCreateMainDeviceName", "loadOrCreateSecondaryDeviceId", "loadSecondaryDeviceName", "monitorMainSocket", "reader", "Ljava/io/BufferedReader;", "(Ljava/net/Socket;Ljava/lang/String;Ljava/io/BufferedReader;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "monitorSecondarySocket", "(Landroid/content/Context;Ljava/net/Socket;Ljava/io/BufferedReader;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registerReconnectMonitor", "saveSecondaryConnectionRecord", "record", "sendEndCallToMain", "sendFinishActiveCallBroadcast", "sendTransferRequest", "targetDeviceId", "callerName", "callerNumber", "fromDeviceName", "sendTransferResponse", "accepted", "sendTransferResponseBroadcast", "status", "deviceName", "setAutoReconnectEnabled", "enabled", "updateConnectedDevicesSnapshot", "updateMainCodeLease", "ActiveConnection", "DiscoveryMatch", "DiscoveryOutcome", "SecondaryConnectResult", "app_debug"})
public final class OneCallConnectionManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "onecall_connection_state";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_DEVICE_ID = "main_device_id";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_DEVICE_NAME = "main_device_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_CODE = "main_code";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_CODE_GENERATED_AT = "main_code_generated_at";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_CODE_VALIDITY = "main_code_validity";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAIN_TCP_PORT = "main_tcp_port";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_DEVICE_ID = "secondary_device_id";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_DEVICE_NAME = "secondary_device_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_AUTO_RECONNECT = "secondary_auto_reconnect";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_HOST = "secondary_last_host";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_PORT = "secondary_last_port";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_WIFI = "secondary_last_wifi";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_CONNECTED_AT = "secondary_last_connected_at";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_MAIN_DEVICE_ID = "secondary_last_main_device_id";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_MAIN_NAME = "secondary_last_main_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SECONDARY_LAST_CODE = "secondary_last_code";
    private static final int DISCOVERY_PORT = 5061;
    private static final int PREFERRED_TCP_PORT = 6070;
    private static final int MAX_CONNECTED_DEVICES = 5;
    private static final long DISCOVERY_TIMEOUT_MILLIS = 2500L;
    private static final long AUTO_RECONNECT_DELAY_MILLIS = 1500L;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.MutableLiveData<java.util.List<com.onecall.data.ConnectedDevice>> connectedDevicesInternal = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.LiveData<java.util.List<com.onecall.data.ConnectedDevice>> connectedDevices = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.MutableLiveData<java.lang.Integer> secondaryDeviceCountInternal = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.LiveData<java.lang.Integer> secondaryDeviceCount = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.atomic.AtomicBoolean mainServerReady = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.atomic.AtomicBoolean reconnectMonitorRegistered = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.atomic.AtomicBoolean reconnectInProgress = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.atomic.AtomicBoolean manualDisconnectRequested = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.Object connectionLock = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.onecall.network.socket.OneCallConnectionManager.ActiveConnection> activeConnections = null;
    private static final java.util.concurrent.ConcurrentHashMap.KeySetView<java.lang.String, java.lang.Boolean> ringingPausedDeviceIds = null;
    @org.jetbrains.annotations.Nullable()
    private static android.content.Context appContext;
    @org.jetbrains.annotations.Nullable()
    private static android.content.SharedPreferences preferences;
    @org.jetbrains.annotations.Nullable()
    private static java.net.ServerSocket mainServerSocket;
    @org.jetbrains.annotations.Nullable()
    private static java.net.DatagramSocket discoverySocket;
    @org.jetbrains.annotations.Nullable()
    private static kotlinx.coroutines.Job acceptJob;
    @org.jetbrains.annotations.Nullable()
    private static kotlinx.coroutines.Job discoveryJob;
    @org.jetbrains.annotations.Nullable()
    private static android.net.ConnectivityManager.NetworkCallback networkCallback;
    @org.jetbrains.annotations.Nullable()
    private static com.onecall.data.CodeLease currentCodeLease;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String currentMainDeviceId;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String currentMainDeviceName;
    private static int currentMainTcpPort = 6070;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String currentSecondaryDeviceId;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String currentSecondaryDeviceName;
    @org.jetbrains.annotations.Nullable()
    private static java.net.Socket activeSecondarySocket;
    @org.jetbrains.annotations.Nullable()
    private static java.io.BufferedWriter activeSecondaryWriter;
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.network.socket.OneCallConnectionManager INSTANCE = null;
    
    private OneCallConnectionManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.onecall.data.ConnectedDevice>> getConnectedDevices() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Integer> getSecondaryDeviceCount() {
        return null;
    }
    
    public final int connectedDeviceCount() {
        return 0;
    }
    
    public final int lastKnownDeviceCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMainDeviceId(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSecondaryDeviceId(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMainDeviceName(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSecondaryDeviceName(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final boolean isRingingPaused(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId) {
        return false;
    }
    
    public final boolean sendTransferRequest(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String targetDeviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String requestId, @org.jetbrains.annotations.Nullable()
    java.lang.String callerName, @org.jetbrains.annotations.Nullable()
    java.lang.String callerNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String fromDeviceName) {
        return false;
    }
    
    public final void sendTransferResponse(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String requestId, boolean accepted) {
    }
    
    public final void sendEndCallToMain(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final boolean isAutoReconnectEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void setAutoReconnectEnabled(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean enabled) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.onecall.data.SecondaryConnectionRecord getLastSecondaryConnectionRecord(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final void ensureMainServerRunning(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.onecall.data.CodeLease codeLease) {
    }
    
    public final void updateMainCodeLease(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.onecall.data.CodeLease codeLease) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object connectSecondary(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult> $completion) {
        return null;
    }
    
    public final void disconnectSecondary(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void disconnectDevice(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId) {
    }
    
    private final void initialize(android.content.Context context) {
    }
    
    private final java.net.ServerSocket createTcpServerSocket() {
        return null;
    }
    
    private final java.lang.Object acceptConnectionsLoop(java.net.ServerSocket serverSocket, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object listenForDiscoveryLoop(java.net.DatagramSocket socket, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void handleDiscoveryPacket(java.net.DatagramSocket socket, java.net.DatagramPacket packet) {
    }
    
    private final java.lang.Object handleMainTcpClient(java.net.Socket socket, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object monitorMainSocket(java.net.Socket socket, java.lang.String deviceId, java.io.BufferedReader reader, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object monitorSecondarySocket(android.content.Context context, java.net.Socket socket, java.io.BufferedReader reader, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.onecall.network.socket.OneCallConnectionManager.DiscoveryOutcome discoverMainDevice(android.content.Context context, java.lang.String code, java.lang.String requestId, java.lang.String secondaryDeviceId, java.lang.String secondaryDeviceName) {
        return null;
    }
    
    private final void updateConnectedDevicesSnapshot() {
    }
    
    private final void broadcastDeviceCount(int count) {
    }
    
    private final void handleTransferRequest(android.content.Context context, java.util.List<java.lang.String> parts) {
    }
    
    private final void handleTransferResponse(java.util.List<java.lang.String> parts) {
    }
    
    private final void handleEndCallRequest() {
    }
    
    private final void sendTransferResponseBroadcast(java.lang.String requestId, java.lang.String status, java.lang.String deviceName) {
    }
    
    private final void sendFinishActiveCallBroadcast() {
    }
    
    private final void endRealCallOnMain() {
    }
    
    private final java.lang.String encodeField(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String decodeField(java.lang.String value) {
        return null;
    }
    
    private final void saveSecondaryConnectionRecord(android.content.Context context, com.onecall.data.SecondaryConnectionRecord record) {
    }
    
    private final void registerReconnectMonitor(android.content.Context context) {
    }
    
    private final void launchAutoReconnect(android.content.Context context) {
    }
    
    private final java.lang.String loadOrCreateMainDeviceId() {
        return null;
    }
    
    private final java.lang.String loadOrCreateMainDeviceName(android.content.Context context) {
        return null;
    }
    
    private final java.lang.String loadOrCreateSecondaryDeviceId() {
        return null;
    }
    
    private final java.lang.String loadSecondaryDeviceName(android.content.Context context) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$ActiveConnection;", "", "device", "Lcom/onecall/data/ConnectedDevice;", "socket", "Ljava/net/Socket;", "writer", "Ljava/io/BufferedWriter;", "(Lcom/onecall/data/ConnectedDevice;Ljava/net/Socket;Ljava/io/BufferedWriter;)V", "getDevice", "()Lcom/onecall/data/ConnectedDevice;", "getSocket", "()Ljava/net/Socket;", "getWriter", "()Ljava/io/BufferedWriter;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    static final class ActiveConnection {
        @org.jetbrains.annotations.NotNull()
        private final com.onecall.data.ConnectedDevice device = null;
        @org.jetbrains.annotations.NotNull()
        private final java.net.Socket socket = null;
        @org.jetbrains.annotations.NotNull()
        private final java.io.BufferedWriter writer = null;
        
        public ActiveConnection(@org.jetbrains.annotations.NotNull()
        com.onecall.data.ConnectedDevice device, @org.jetbrains.annotations.NotNull()
        java.net.Socket socket, @org.jetbrains.annotations.NotNull()
        java.io.BufferedWriter writer) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.data.ConnectedDevice getDevice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.net.Socket getSocket() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.BufferedWriter getWriter() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.data.ConnectedDevice component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.net.Socket component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.BufferedWriter component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.network.socket.OneCallConnectionManager.ActiveConnection copy(@org.jetbrains.annotations.NotNull()
        com.onecall.data.ConnectedDevice device, @org.jetbrains.annotations.NotNull()
        java.net.Socket socket, @org.jetbrains.annotations.NotNull()
        java.io.BufferedWriter writer) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00d6\u0001J\t\u0010 \u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006!"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryMatch;", "", "host", "", "tcpPort", "", "mainDeviceId", "mainDeviceName", "codeGeneratedAtMillis", "", "codeValidityMillis", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;JJ)V", "getCodeGeneratedAtMillis", "()J", "getCodeValidityMillis", "getHost", "()Ljava/lang/String;", "getMainDeviceId", "getMainDeviceName", "getTcpPort", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class DiscoveryMatch {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String host = null;
        private final int tcpPort = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String mainDeviceId = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String mainDeviceName = null;
        private final long codeGeneratedAtMillis = 0L;
        private final long codeValidityMillis = 0L;
        
        public DiscoveryMatch(@org.jetbrains.annotations.NotNull()
        java.lang.String host, int tcpPort, @org.jetbrains.annotations.NotNull()
        java.lang.String mainDeviceId, @org.jetbrains.annotations.NotNull()
        java.lang.String mainDeviceName, long codeGeneratedAtMillis, long codeValidityMillis) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getHost() {
            return null;
        }
        
        public final int getTcpPort() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMainDeviceId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMainDeviceName() {
            return null;
        }
        
        public final long getCodeGeneratedAtMillis() {
            return 0L;
        }
        
        public final long getCodeValidityMillis() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        public final long component5() {
            return 0L;
        }
        
        public final long component6() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.network.socket.OneCallConnectionManager.DiscoveryMatch copy(@org.jetbrains.annotations.NotNull()
        java.lang.String host, int tcpPort, @org.jetbrains.annotations.NotNull()
        java.lang.String mainDeviceId, @org.jetbrains.annotations.NotNull()
        java.lang.String mainDeviceName, long codeGeneratedAtMillis, long codeValidityMillis) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b2\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0002\u0005\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome;", "", "()V", "InvalidCode", "Match", "Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome$InvalidCode;", "Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome$Match;", "app_debug"})
    static abstract class DiscoveryOutcome {
        
        private DiscoveryOutcome() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome$InvalidCode;", "Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class InvalidCode extends com.onecall.network.socket.OneCallConnectionManager.DiscoveryOutcome {
            @org.jetbrains.annotations.NotNull()
            public static final com.onecall.network.socket.OneCallConnectionManager.DiscoveryOutcome.InvalidCode INSTANCE = null;
            
            private InvalidCode() {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u00d6\u0003J\t\u0010 \u001a\u00020\u0005H\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\""}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome$Match;", "Lcom/onecall/network/socket/OneCallConnectionManager$DiscoveryOutcome;", "host", "", "tcpPort", "", "mainDeviceId", "mainDeviceName", "codeGeneratedAtMillis", "", "codeValidityMillis", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;JJ)V", "getCodeGeneratedAtMillis", "()J", "getCodeValidityMillis", "getHost", "()Ljava/lang/String;", "getMainDeviceId", "getMainDeviceName", "getTcpPort", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "", "hashCode", "toString", "app_debug"})
        public static final class Match extends com.onecall.network.socket.OneCallConnectionManager.DiscoveryOutcome {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String host = null;
            private final int tcpPort = 0;
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String mainDeviceId = null;
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String mainDeviceName = null;
            private final long codeGeneratedAtMillis = 0L;
            private final long codeValidityMillis = 0L;
            
            public Match(@org.jetbrains.annotations.NotNull()
            java.lang.String host, int tcpPort, @org.jetbrains.annotations.NotNull()
            java.lang.String mainDeviceId, @org.jetbrains.annotations.NotNull()
            java.lang.String mainDeviceName, long codeGeneratedAtMillis, long codeValidityMillis) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getHost() {
                return null;
            }
            
            public final int getTcpPort() {
                return 0;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMainDeviceId() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMainDeviceName() {
                return null;
            }
            
            public final long getCodeGeneratedAtMillis() {
                return 0L;
            }
            
            public final long getCodeValidityMillis() {
                return 0L;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            public final int component2() {
                return 0;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component3() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component4() {
                return null;
            }
            
            public final long component5() {
                return 0L;
            }
            
            public final long component6() {
                return 0L;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.onecall.network.socket.OneCallConnectionManager.DiscoveryOutcome.Match copy(@org.jetbrains.annotations.NotNull()
            java.lang.String host, int tcpPort, @org.jetbrains.annotations.NotNull()
            java.lang.String mainDeviceId, @org.jetbrains.annotations.NotNull()
            java.lang.String mainDeviceName, long codeGeneratedAtMillis, long codeValidityMillis) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "", "()V", "Failed", "SameWifiRequired", "Success", "WrongCode", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$Failed;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$SameWifiRequired;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$Success;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$WrongCode;", "app_debug"})
    public static abstract class SecondaryConnectResult {
        
        private SecondaryConnectResult() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$Failed;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class Failed extends com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            
            public Failed(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult.Failed copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$SameWifiRequired;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class SameWifiRequired extends com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult {
            @org.jetbrains.annotations.NotNull()
            public static final com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult.SameWifiRequired INSTANCE = null;
            
            private SameWifiRequired() {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$Success;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "record", "Lcom/onecall/data/SecondaryConnectionRecord;", "(Lcom/onecall/data/SecondaryConnectionRecord;)V", "getRecord", "()Lcom/onecall/data/SecondaryConnectionRecord;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class Success extends com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult {
            @org.jetbrains.annotations.NotNull()
            private final com.onecall.data.SecondaryConnectionRecord record = null;
            
            public Success(@org.jetbrains.annotations.NotNull()
            com.onecall.data.SecondaryConnectionRecord record) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.onecall.data.SecondaryConnectionRecord getRecord() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.onecall.data.SecondaryConnectionRecord component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult.Success copy(@org.jetbrains.annotations.NotNull()
            com.onecall.data.SecondaryConnectionRecord record) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult$WrongCode;", "Lcom/onecall/network/socket/OneCallConnectionManager$SecondaryConnectResult;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class WrongCode extends com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult {
            @org.jetbrains.annotations.NotNull()
            public static final com.onecall.network.socket.OneCallConnectionManager.SecondaryConnectResult.WrongCode INSTANCE = null;
            
            private WrongCode() {
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
}