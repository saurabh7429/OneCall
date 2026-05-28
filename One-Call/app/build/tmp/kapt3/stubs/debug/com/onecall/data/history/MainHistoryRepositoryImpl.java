package com.onecall.data.history;

import android.content.Context;
import com.onecall.data.DeviceRoleStore;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00190\u0018H\u0016J\u0014\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00190\u0018H\u0016J\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/onecall/data/history/MainHistoryRepositoryImpl;", "Lcom/onecall/data/history/HistoryRepository;", "dao", "Lcom/onecall/data/history/HistoryDao;", "(Lcom/onecall/data/history/HistoryDao;)V", "addCallHistory", "", "entry", "Lcom/onecall/data/history/CallHistoryEntity;", "(Lcom/onecall/data/history/CallHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addOrUpdateDeviceHistory", "Lcom/onecall/data/history/DeviceHistoryEntity;", "(Lcom/onecall/data/history/DeviceHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCallHistory", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCallHistory", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteDeviceHistory", "deviceId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCallHistory", "Lkotlinx/coroutines/flow/Flow;", "", "getDeviceHistory", "getDeviceHistoryById", "app_debug"})
public final class MainHistoryRepositoryImpl implements com.onecall.data.history.HistoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.onecall.data.history.HistoryDao dao = null;
    
    public MainHistoryRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.HistoryDao dao) {
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