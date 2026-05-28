package com.onecall.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.onecall.data.db.entities.CallHistoryEntity;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CallHistoryDao_Impl implements CallHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallHistoryEntity> __insertionAdapterOfCallHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearPermanentHistory;

  private final SharedSQLiteStatement __preparedStmtOfClearSessionHistory;

  public CallHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallHistoryEntity = new EntityInsertionAdapter<CallHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `call_history` (`id`,`callerNumber`,`callerName`,`callType`,`durationSeconds`,`timestamp`,`deviceSource`,`isPermanent`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCallerNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCallerNumber());
        }
        if (entity.getCallerName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCallerName());
        }
        if (entity.getCallType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCallType());
        }
        statement.bindLong(5, entity.getDurationSeconds());
        statement.bindLong(6, entity.getTimestamp());
        if (entity.getDeviceSource() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDeviceSource());
        }
        final int _tmp = entity.isPermanent() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__preparedStmtOfClearPermanentHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM call_history WHERE isPermanent = 1";
        return _query;
      }
    };
    this.__preparedStmtOfClearSessionHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM call_history WHERE isPermanent = 0";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CallHistoryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCallHistoryEntity.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearPermanentHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPermanentHistory.acquire();
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
          __preparedStmtOfClearPermanentHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearSessionHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSessionHistory.acquire();
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
          __preparedStmtOfClearSessionHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CallHistoryEntity>> getAllPermanentHistory() {
    final String _sql = "SELECT * FROM call_history WHERE isPermanent = 1 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_history"}, new Callable<List<CallHistoryEntity>>() {
      @Override
      @NonNull
      public List<CallHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCallerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "callerNumber");
          final int _cursorIndexOfCallerName = CursorUtil.getColumnIndexOrThrow(_cursor, "callerName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeviceSource = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceSource");
          final int _cursorIndexOfIsPermanent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPermanent");
          final List<CallHistoryEntity> _result = new ArrayList<CallHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCallerNumber;
            if (_cursor.isNull(_cursorIndexOfCallerNumber)) {
              _tmpCallerNumber = null;
            } else {
              _tmpCallerNumber = _cursor.getString(_cursorIndexOfCallerNumber);
            }
            final String _tmpCallerName;
            if (_cursor.isNull(_cursorIndexOfCallerName)) {
              _tmpCallerName = null;
            } else {
              _tmpCallerName = _cursor.getString(_cursorIndexOfCallerName);
            }
            final String _tmpCallType;
            if (_cursor.isNull(_cursorIndexOfCallType)) {
              _tmpCallType = null;
            } else {
              _tmpCallType = _cursor.getString(_cursorIndexOfCallType);
            }
            final long _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDeviceSource;
            if (_cursor.isNull(_cursorIndexOfDeviceSource)) {
              _tmpDeviceSource = null;
            } else {
              _tmpDeviceSource = _cursor.getString(_cursorIndexOfDeviceSource);
            }
            final boolean _tmpIsPermanent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPermanent);
            _tmpIsPermanent = _tmp != 0;
            _item = new CallHistoryEntity(_tmpId,_tmpCallerNumber,_tmpCallerName,_tmpCallType,_tmpDurationSeconds,_tmpTimestamp,_tmpDeviceSource,_tmpIsPermanent);
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
  public Flow<List<CallHistoryEntity>> getHistoryByType(final String type) {
    final String _sql = "SELECT * FROM call_history WHERE isPermanent = 1 AND callType = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_history"}, new Callable<List<CallHistoryEntity>>() {
      @Override
      @NonNull
      public List<CallHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCallerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "callerNumber");
          final int _cursorIndexOfCallerName = CursorUtil.getColumnIndexOrThrow(_cursor, "callerName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeviceSource = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceSource");
          final int _cursorIndexOfIsPermanent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPermanent");
          final List<CallHistoryEntity> _result = new ArrayList<CallHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCallerNumber;
            if (_cursor.isNull(_cursorIndexOfCallerNumber)) {
              _tmpCallerNumber = null;
            } else {
              _tmpCallerNumber = _cursor.getString(_cursorIndexOfCallerNumber);
            }
            final String _tmpCallerName;
            if (_cursor.isNull(_cursorIndexOfCallerName)) {
              _tmpCallerName = null;
            } else {
              _tmpCallerName = _cursor.getString(_cursorIndexOfCallerName);
            }
            final String _tmpCallType;
            if (_cursor.isNull(_cursorIndexOfCallType)) {
              _tmpCallType = null;
            } else {
              _tmpCallType = _cursor.getString(_cursorIndexOfCallType);
            }
            final long _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDeviceSource;
            if (_cursor.isNull(_cursorIndexOfDeviceSource)) {
              _tmpDeviceSource = null;
            } else {
              _tmpDeviceSource = _cursor.getString(_cursorIndexOfDeviceSource);
            }
            final boolean _tmpIsPermanent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPermanent);
            _tmpIsPermanent = _tmp != 0;
            _item = new CallHistoryEntity(_tmpId,_tmpCallerNumber,_tmpCallerName,_tmpCallType,_tmpDurationSeconds,_tmpTimestamp,_tmpDeviceSource,_tmpIsPermanent);
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
  public Flow<List<CallHistoryEntity>> getSessionHistory() {
    final String _sql = "SELECT * FROM call_history WHERE isPermanent = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_history"}, new Callable<List<CallHistoryEntity>>() {
      @Override
      @NonNull
      public List<CallHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCallerNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "callerNumber");
          final int _cursorIndexOfCallerName = CursorUtil.getColumnIndexOrThrow(_cursor, "callerName");
          final int _cursorIndexOfCallType = CursorUtil.getColumnIndexOrThrow(_cursor, "callType");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeviceSource = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceSource");
          final int _cursorIndexOfIsPermanent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPermanent");
          final List<CallHistoryEntity> _result = new ArrayList<CallHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCallerNumber;
            if (_cursor.isNull(_cursorIndexOfCallerNumber)) {
              _tmpCallerNumber = null;
            } else {
              _tmpCallerNumber = _cursor.getString(_cursorIndexOfCallerNumber);
            }
            final String _tmpCallerName;
            if (_cursor.isNull(_cursorIndexOfCallerName)) {
              _tmpCallerName = null;
            } else {
              _tmpCallerName = _cursor.getString(_cursorIndexOfCallerName);
            }
            final String _tmpCallType;
            if (_cursor.isNull(_cursorIndexOfCallType)) {
              _tmpCallType = null;
            } else {
              _tmpCallType = _cursor.getString(_cursorIndexOfCallType);
            }
            final long _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpDeviceSource;
            if (_cursor.isNull(_cursorIndexOfDeviceSource)) {
              _tmpDeviceSource = null;
            } else {
              _tmpDeviceSource = _cursor.getString(_cursorIndexOfDeviceSource);
            }
            final boolean _tmpIsPermanent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPermanent);
            _tmpIsPermanent = _tmp != 0;
            _item = new CallHistoryEntity(_tmpId,_tmpCallerNumber,_tmpCallerName,_tmpCallType,_tmpDurationSeconds,_tmpTimestamp,_tmpDeviceSource,_tmpIsPermanent);
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
