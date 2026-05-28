package com.onecall.data.history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eH\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000f0\u000eH\'J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000f0\u000eH\'J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00122\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000e2\u0006\u0010\n\u001a\u00020\u000bH\'J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00142\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010 \u00a8\u0006!"}, d2 = {"Lcom/onecall/data/history/HistoryDao;", "", "clearAllCallHistory", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCallHistoryById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteDeviceHistoryById", "deviceId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCallHistory", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/onecall/data/history/CallHistoryEntity;", "getAllDeviceConfigs", "Lcom/onecall/data/settings/DeviceConfigEntity;", "getAllDeviceHistory", "Lcom/onecall/data/history/DeviceHistoryEntity;", "getDeviceConfig", "getDeviceConfigFlow", "getDeviceHistoryById", "insertCallHistory", "callHistory", "(Lcom/onecall/data/history/CallHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertDeviceConfig", "config", "(Lcom/onecall/data/settings/DeviceConfigEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertDeviceHistory", "deviceHistory", "(Lcom/onecall/data/history/DeviceHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface HistoryDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCallHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.CallHistoryEntity callHistory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM call_history ORDER BY date_time DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.CallHistoryEntity>> getAllCallHistory();
    
    @androidx.room.Query(value = "DELETE FROM call_history WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCallHistoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM call_history")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAllCallHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertDeviceHistory(@org.jetbrains.annotations.NotNull()
    com.onecall.data.history.DeviceHistoryEntity deviceHistory, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM device_history ORDER BY last_seen_at DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.history.DeviceHistoryEntity>> getAllDeviceHistory();
    
    @androidx.room.Query(value = "SELECT * FROM device_history WHERE device_id = :deviceId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDeviceHistoryById(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.data.history.DeviceHistoryEntity> $completion);
    
    @androidx.room.Query(value = "DELETE FROM device_history WHERE device_id = :deviceId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteDeviceHistoryById(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertDeviceConfig(@org.jetbrains.annotations.NotNull()
    com.onecall.data.settings.DeviceConfigEntity config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM device_config WHERE deviceId = :deviceId LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.onecall.data.settings.DeviceConfigEntity> getDeviceConfigFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId);
    
    @androidx.room.Query(value = "SELECT * FROM device_config WHERE deviceId = :deviceId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDeviceConfig(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.onecall.data.settings.DeviceConfigEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM device_config")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.onecall.data.settings.DeviceConfigEntity>> getAllDeviceConfigs();
}