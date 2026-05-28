package com.onecall.data.history;

import android.content.Context;
import com.onecall.data.DeviceRoleStore;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\bf\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000eH\u00a6@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00160\u0015H&J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00160\u0015H&J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0011\u001a\u00020\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u001a"}, d2 = {"Lcom/onecall/data/history/HistoryRepository;", "", "addCallHistory", "", "entry", "Lcom/onecall/data/history/CallHistoryEntity;", "(Lcom/onecall/data/history/CallHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addOrUpdateDeviceHistory", "Lcom/onecall/data/history/DeviceHistoryEntity;", "(Lcom/onecall/data/history/DeviceHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCallHistory", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCallHistory", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteDeviceHistory", "deviceId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCallHistory", "Lkotlinx/coroutines/flow/Flow;", "", "getDeviceHistory", "getDeviceHistoryById", "Companion", "app_debug"})
public abstract interface HistoryRepository {
    @org.jetbrains.annotations.NotNull()
    public static final com.onecall.data.history.HistoryRepository.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.CallHistoryEntity>> getCallHistory();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addCallHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.CallHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCallHistory(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearCallHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.DeviceHistoryEntity>> getDeviceHistory();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addOrUpdateDeviceHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.DeviceHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDeviceHistoryById(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.data.history.DeviceHistoryEntity> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteDeviceHistory(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/onecall/data/history/HistoryRepository$Companion;", "", "()V", "instance", "Lcom/onecall/data/history/HistoryRepository;", "clearInstance", "", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        @kotlin.jvm.Volatile()
        @org.jetbrains.annotations.Nullable()
        private static volatile com.onecall.data.history.HistoryRepository instance;
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.onecall.data.history.HistoryRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        public final void clearInstance() {
        }
    }
}