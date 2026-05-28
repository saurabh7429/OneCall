package com.onecall.data.history;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.onecall.data.settings.DeviceConfigEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallHistoryEntity> __insertionAdapterOfCallHistoryEntity;

  private final EntityInsertionAdapter<DeviceHistoryEntity> __insertionAdapterOfDeviceHistoryEntity;

  private final EntityInsertionAdapter<DeviceConfigEntity> __insertionAdapterOfDeviceConfigEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCallHistoryById;

  private final SharedSQLiteStatement __preparedStmtOfClearAllCallHistory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDeviceHistoryById;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallHistoryEntity = new EntityInsertionAdapter<CallHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `call_history` (`id`,`caller_name`,`phone_number`,`call_type`,`date_time`,`duration_seconds`,`attended_by_device`,`ring_count`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCallerName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCallerName());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhoneNumber());
        }
        if (entity.getCallType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCallType());
        }
        statement.bindLong(5, entity.getDateTime());
        statement.bindLong(6, entity.getDurationSeconds());
        if (entity.getAttendedByDevice() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAttendedByDevice());
        }
        statement.bindLong(8, entity.getRingCount());
      }
    };
    this.__insertionAdapterOfDeviceHistoryEntity = new EntityInsertionAdapter<DeviceHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `device_history` (`device_id`,`device_name`,`first_connected_at`,`last_seen_at`,`calls_attended`,`calls_made`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeviceHistoryEntity entity) {
        if (entity.getDeviceId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDeviceId());
        }
        if (entity.getDeviceName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDeviceName());
        }
        statement.bindLong(3, entity.getFirstConnectedAt());
        statement.bindLong(4, entity.getLastSeenAt());
        statement.bindLong(5, entity.getCallsAttended());
        statement.bindLong(6, entity.getCallsMade());
      }
    };
    this.__insertionAdapterOfDeviceConfigEntity = new EntityInsertionAdapter<DeviceConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `device_config` (`deviceId`,`nickname`,`deviceIcon`,`ringOnDevice`,`allowOutgoing`,`autoApproveOutgoing`,`ringVolume`,`dndStartTime`,`dndEndTime`,`isCallsPaused`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeviceConfigEntity entity) {
        if (entity.getDeviceId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDeviceId());
        }
        if (entity.getNickname() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNickname());
        }
        if (entity.getDeviceIcon() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDeviceIcon());
        }
        final int _tmp = entity.getRingOnDevice() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getAllowOutgoing() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.getAutoApproveOutgoing() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        statement.bindLong(7, entity.getRingVolume());
        if (entity.getDndStartTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDndStartTime());
        }
        if (entity.getDndEndTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDndEndTime());
        }
        final int _tmp_3 = entity.isCallsPaused() ? 1 : 0;
        statement.bindLong(10, _tmp_3);
      }
    };
    this.__preparedStmtOfDeleteCallHistoryById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM call_history WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllCallHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM call_history";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteDeviceHistoryById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM device_history WHERE device_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCallHistory(final CallHistoryEntity callHistory,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCallHistoryEntity.insert(callHistory);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDeviceHistory(final DeviceHistoryEntity deviceHistory,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDeviceHistoryEntity.insert(deviceHistory);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDeviceConfig(final DeviceConfigEntity config,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDeviceConfigEntity.insert(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCallHistoryById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCallHistoryById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCallHistoryById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllCallHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllCallHistory.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllCallHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDeviceHistoryById(final String deviceId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDeviceHistoryById.acquire();
        int _argIndex = 1;
        if (deviceId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, deviceId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteDeviceHistoryById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CallHistoryEntity>> getAllCallHistory() {
    final String _sql = "SELECT * FROM call_history ORDER BY date_time DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_history"}, new Callable<List<CallHistoryEntity>>() {
      @Override
      @NonNull
      public List<CallHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCallerName = CursorUtil.getColumnIndexOrThrow(_cursor, "caller_name");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "call_type");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "date_time");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_seconds");
          final int _cursorIndexOfAttendedByDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "attended_by_device");
          final int _cursorIndexOfRingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ring_count");
          final List<CallHistoryEntity> _result = new ArrayList<CallHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCallerName;
            if (_cursor.isNull(_cursorIndexOfCallerName)) {
              _tmpCallerName = null;
            } else {
              _tmpCallerName = _cursor.getString(_cursorIndexOfCallerName);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpCallType;
            if (_cursor.isNull(_cursorIndexOfCallType)) {
              _tmpCallType = null;
            } else {
              _tmpCallType = _cursor.getString(_cursorIndexOfCallType);
            }
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpAttendedByDevice;
            if (_cursor.isNull(_cursorIndexOfAttendedByDevice)) {
              _tmpAttendedByDevice = null;
            } else {
              _tmpAttendedByDevice = _cursor.getString(_cursorIndexOfAttendedByDevice);
            }
            final int _tmpRingCount;
            _tmpRingCount = _cursor.getInt(_cursorIndexOfRingCount);
            _item = new CallHistoryEntity(_tmpId,_tmpCallerName,_tmpPhoneNumber,_tmpCallType,_tmpDateTime,_tmpDurationSeconds,_tmpAttendedByDevice,_tmpRingCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<DeviceHistoryEntity>> getAllDeviceHistory() {
    final String _sql = "SELECT * FROM device_history ORDER BY last_seen_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"device_history"}, new Callable<List<DeviceHistoryEntity>>() {
      @Override
      @NonNull
      public List<DeviceHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfFirstConnectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "first_connected_at");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_seen_at");
          final int _cursorIndexOfCallsAttended = CursorUtil.getColumnIndexOrThrow(_cursor, "calls_attended");
          final int _cursorIndexOfCallsMade = CursorUtil.getColumnIndexOrThrow(_cursor, "calls_made");
          final List<DeviceHistoryEntity> _result = new ArrayList<DeviceHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DeviceHistoryEntity _item;
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            final long _tmpFirstConnectedAt;
            _tmpFirstConnectedAt = _cursor.getLong(_cursorIndexOfFirstConnectedAt);
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            final int _tmpCallsAttended;
            _tmpCallsAttended = _cursor.getInt(_cursorIndexOfCallsAttended);
            final int _tmpCallsMade;
            _tmpCallsMade = _cursor.getInt(_cursorIndexOfCallsMade);
            _item = new DeviceHistoryEntity(_tmpDeviceId,_tmpDeviceName,_tmpFirstConnectedAt,_tmpLastSeenAt,_tmpCallsAttended,_tmpCallsMade);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDeviceHistoryById(final String deviceId,
      final Continuation<? super DeviceHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM device_history WHERE device_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (deviceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, deviceId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DeviceHistoryEntity>() {
      @Override
      @Nullable
      public DeviceHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfFirstConnectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "first_connected_at");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_seen_at");
          final int _cursorIndexOfCallsAttended = CursorUtil.getColumnIndexOrThrow(_cursor, "calls_attended");
          final int _cursorIndexOfCallsMade = CursorUtil.getColumnIndexOrThrow(_cursor, "calls_made");
          final DeviceHistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            final long _tmpFirstConnectedAt;
            _tmpFirstConnectedAt = _cursor.getLong(_cursorIndexOfFirstConnectedAt);
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            final int _tmpCallsAttended;
            _tmpCallsAttended = _cursor.getInt(_cursorIndexOfCallsAttended);
            final int _tmpCallsMade;
            _tmpCallsMade = _cursor.getInt(_cursorIndexOfCallsMade);
            _result = new DeviceHistoryEntity(_tmpDeviceId,_tmpDeviceName,_tmpFirstConnectedAt,_tmpLastSeenAt,_tmpCallsAttended,_tmpCallsMade);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<DeviceConfigEntity> getDeviceConfigFlow(final String deviceId) {
    final String _sql = "SELECT * FROM device_config WHERE deviceId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (deviceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, deviceId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"device_config"}, new Callable<DeviceConfigEntity>() {
      @Override
      @Nullable
      public DeviceConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "nickname");
          final int _cursorIndexOfDeviceIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceIcon");
          final int _cursorIndexOfRingOnDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "ringOnDevice");
          final int _cursorIndexOfAllowOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "allowOutgoing");
          final int _cursorIndexOfAutoApproveOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "autoApproveOutgoing");
          final int _cursorIndexOfRingVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "ringVolume");
          final int _cursorIndexOfDndStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndStartTime");
          final int _cursorIndexOfDndEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndEndTime");
          final int _cursorIndexOfIsCallsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallsPaused");
          final DeviceConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpNickname;
            if (_cursor.isNull(_cursorIndexOfNickname)) {
              _tmpNickname = null;
            } else {
              _tmpNickname = _cursor.getString(_cursorIndexOfNickname);
            }
            final String _tmpDeviceIcon;
            if (_cursor.isNull(_cursorIndexOfDeviceIcon)) {
              _tmpDeviceIcon = null;
            } else {
              _tmpDeviceIcon = _cursor.getString(_cursorIndexOfDeviceIcon);
            }
            final boolean _tmpRingOnDevice;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRingOnDevice);
            _tmpRingOnDevice = _tmp != 0;
            final boolean _tmpAllowOutgoing;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAllowOutgoing);
            _tmpAllowOutgoing = _tmp_1 != 0;
            final boolean _tmpAutoApproveOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAutoApproveOutgoing);
            _tmpAutoApproveOutgoing = _tmp_2 != 0;
            final int _tmpRingVolume;
            _tmpRingVolume = _cursor.getInt(_cursorIndexOfRingVolume);
            final String _tmpDndStartTime;
            if (_cursor.isNull(_cursorIndexOfDndStartTime)) {
              _tmpDndStartTime = null;
            } else {
              _tmpDndStartTime = _cursor.getString(_cursorIndexOfDndStartTime);
            }
            final String _tmpDndEndTime;
            if (_cursor.isNull(_cursorIndexOfDndEndTime)) {
              _tmpDndEndTime = null;
            } else {
              _tmpDndEndTime = _cursor.getString(_cursorIndexOfDndEndTime);
            }
            final boolean _tmpIsCallsPaused;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsCallsPaused);
            _tmpIsCallsPaused = _tmp_3 != 0;
            _result = new DeviceConfigEntity(_tmpDeviceId,_tmpNickname,_tmpDeviceIcon,_tmpRingOnDevice,_tmpAllowOutgoing,_tmpAutoApproveOutgoing,_tmpRingVolume,_tmpDndStartTime,_tmpDndEndTime,_tmpIsCallsPaused);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDeviceConfig(final String deviceId,
      final Continuation<? super DeviceConfigEntity> $completion) {
    final String _sql = "SELECT * FROM device_config WHERE deviceId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (deviceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, deviceId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DeviceConfigEntity>() {
      @Override
      @Nullable
      public DeviceConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "nickname");
          final int _cursorIndexOfDeviceIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceIcon");
          final int _cursorIndexOfRingOnDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "ringOnDevice");
          final int _cursorIndexOfAllowOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "allowOutgoing");
          final int _cursorIndexOfAutoApproveOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "autoApproveOutgoing");
          final int _cursorIndexOfRingVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "ringVolume");
          final int _cursorIndexOfDndStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndStartTime");
          final int _cursorIndexOfDndEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndEndTime");
          final int _cursorIndexOfIsCallsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallsPaused");
          final DeviceConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpNickname;
            if (_cursor.isNull(_cursorIndexOfNickname)) {
              _tmpNickname = null;
            } else {
              _tmpNickname = _cursor.getString(_cursorIndexOfNickname);
            }
            final String _tmpDeviceIcon;
            if (_cursor.isNull(_cursorIndexOfDeviceIcon)) {
              _tmpDeviceIcon = null;
            } else {
              _tmpDeviceIcon = _cursor.getString(_cursorIndexOfDeviceIcon);
            }
            final boolean _tmpRingOnDevice;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRingOnDevice);
            _tmpRingOnDevice = _tmp != 0;
            final boolean _tmpAllowOutgoing;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAllowOutgoing);
            _tmpAllowOutgoing = _tmp_1 != 0;
            final boolean _tmpAutoApproveOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAutoApproveOutgoing);
            _tmpAutoApproveOutgoing = _tmp_2 != 0;
            final int _tmpRingVolume;
            _tmpRingVolume = _cursor.getInt(_cursorIndexOfRingVolume);
            final String _tmpDndStartTime;
            if (_cursor.isNull(_cursorIndexOfDndStartTime)) {
              _tmpDndStartTime = null;
            } else {
              _tmpDndStartTime = _cursor.getString(_cursorIndexOfDndStartTime);
            }
            final String _tmpDndEndTime;
            if (_cursor.isNull(_cursorIndexOfDndEndTime)) {
              _tmpDndEndTime = null;
            } else {
              _tmpDndEndTime = _cursor.getString(_cursorIndexOfDndEndTime);
            }
            final boolean _tmpIsCallsPaused;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsCallsPaused);
            _tmpIsCallsPaused = _tmp_3 != 0;
            _result = new DeviceConfigEntity(_tmpDeviceId,_tmpNickname,_tmpDeviceIcon,_tmpRingOnDevice,_tmpAllowOutgoing,_tmpAutoApproveOutgoing,_tmpRingVolume,_tmpDndStartTime,_tmpDndEndTime,_tmpIsCallsPaused);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DeviceConfigEntity>> getAllDeviceConfigs() {
    final String _sql = "SELECT * FROM device_config";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"device_config"}, new Callable<List<DeviceConfigEntity>>() {
      @Override
      @NonNull
      public List<DeviceConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "nickname");
          final int _cursorIndexOfDeviceIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceIcon");
          final int _cursorIndexOfRingOnDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "ringOnDevice");
          final int _cursorIndexOfAllowOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "allowOutgoing");
          final int _cursorIndexOfAutoApproveOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "autoApproveOutgoing");
          final int _cursorIndexOfRingVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "ringVolume");
          final int _cursorIndexOfDndStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndStartTime");
          final int _cursorIndexOfDndEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dndEndTime");
          final int _cursorIndexOfIsCallsPaused = CursorUtil.getColumnIndexOrThrow(_cursor, "isCallsPaused");
          final List<DeviceConfigEntity> _result = new ArrayList<DeviceConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DeviceConfigEntity _item;
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpNickname;
            if (_cursor.isNull(_cursorIndexOfNickname)) {
              _tmpNickname = null;
            } else {
              _tmpNickname = _cursor.getString(_cursorIndexOfNickname);
            }
            final String _tmpDeviceIcon;
            if (_cursor.isNull(_cursorIndexOfDeviceIcon)) {
              _tmpDeviceIcon = null;
            } else {
              _tmpDeviceIcon = _cursor.getString(_cursorIndexOfDeviceIcon);
            }
            final boolean _tmpRingOnDevice;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRingOnDevice);
            _tmpRingOnDevice = _tmp != 0;
            final boolean _tmpAllowOutgoing;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAllowOutgoing);
            _tmpAllowOutgoing = _tmp_1 != 0;
            final boolean _tmpAutoApproveOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAutoApproveOutgoing);
            _tmpAutoApproveOutgoing = _tmp_2 != 0;
            final int _tmpRingVolume;
            _tmpRingVolume = _cursor.getInt(_cursorIndexOfRingVolume);
            final String _tmpDndStartTime;
            if (_cursor.isNull(_cursorIndexOfDndStartTime)) {
              _tmpDndStartTime = null;
            } else {
              _tmpDndStartTime = _cursor.getString(_cursorIndexOfDndStartTime);
            }
            final String _tmpDndEndTime;
            if (_cursor.isNull(_cursorIndexOfDndEndTime)) {
              _tmpDndEndTime = null;
            } else {
              _tmpDndEndTime = _cursor.getString(_cursorIndexOfDndEndTime);
            }
            final boolean _tmpIsCallsPaused;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsCallsPaused);
            _tmpIsCallsPaused = _tmp_3 != 0;
            _item = new DeviceConfigEntity(_tmpDeviceId,_tmpNickname,_tmpDeviceIcon,_tmpRingOnDevice,_tmpAllowOutgoing,_tmpAutoApproveOutgoing,_tmpRingVolume,_tmpDndStartTime,_tmpDndEndTime,_tmpIsCallsPaused);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
