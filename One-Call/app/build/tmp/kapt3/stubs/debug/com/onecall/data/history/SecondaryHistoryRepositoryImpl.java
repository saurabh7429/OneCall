package com.onecall.data.history;

import android.content.Context;
import com.onecall.data.DeviceRoleStore;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u0019J\u0014\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u001bH\u0016J\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u001bH\u0016J\u0018\u0010\u001d\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u0019R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/onecall/data/history/SecondaryHistoryRepositoryImpl;", "Lcom/onecall/data/history/HistoryRepository;", "()V", "callHistoryState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/onecall/data/history/CallHistoryEntity;", "deviceHistoryState", "Lcom/onecall/data/history/DeviceHistoryEntity;", "nextCallId", "", "addCallHistory", "", "entry", "(Lcom/onecall/data/history/CallHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addOrUpdateDeviceHistory", "(Lcom/onecall/data/history/DeviceHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCallHistory", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCallHistory", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteDeviceHistory", "deviceId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCallHistory", "Lkotlinx/coroutines/flow/Flow;", "getDeviceHistory", "getDeviceHistoryById", "app_debug"})
public final class SecondaryHistoryRepositoryImpl implements com.onecall.data.history.HistoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.onecall.data.history.CallHistoryEntity>> callHistoryState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.onecall.data.history.DeviceHistoryEntity>> deviceHistoryState = null;
    private long nextCallId = 1L;
    
    public SecondaryHistoryRepositoryImpl() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.CallHistoryEntity>> getCallHistory() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addCallHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.CallHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteCallHistory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object clearCallHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.DeviceHistoryEntity>> getDeviceHistory() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addOrUpdateDeviceHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.DeviceHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getDeviceHistoryById(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.data.history.DeviceHistoryEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteDeviceHistory(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}