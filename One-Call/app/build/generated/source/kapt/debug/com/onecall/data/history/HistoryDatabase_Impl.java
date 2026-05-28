package com.onecall.data.history;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryDatabase_Impl extends HistoryDatabase {
  private volatile HistoryDao _historyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `call_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `caller_name` TEXT, `phone_number` TEXT NOT NULL, `call_type` TEXT NOT NULL, `date_time` INTEGER NOT NULL, `duration_seconds` INTEGER NOT NULL, `attended_by_device` TEXT, `ring_count` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `device_history` (`device_id` TEXT NOT NULL, `device_name` TEXT NOT NULL, `first_connected_at` INTEGER NOT NULL, `last_seen_at` INTEGER NOT NULL, `calls_attended` INTEGER NOT NULL, `calls_made` INTEGER NOT NULL, PRIMARY KEY(`device_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `device_config` (`deviceId` TEXT NOT NULL, `nickname` TEXT NOT NULL, `deviceIcon` TEXT NOT NULL, `ringOnDevice` INTEGER NOT NULL, `allowOutgoing` INTEGER NOT NULL, `autoApproveOutgoing` INTEGER NOT NULL, `ringVolume` INTEGER NOT NULL, `dndStartTime` TEXT NOT NULL, `dndEndTime` TEXT NOT NULL, `isCallsPaused` INTEGER NOT NULL, PRIMARY KEY(`deviceId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fcbc1ef5b4ceb66298c7593821e0bf06')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `call_history`");
        db.execSQL("DROP TABLE IF EXISTS `device_history`");
        db.execSQL("DROP TABLE IF EXISTS `device_config`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCallHistory = new HashMap<String, TableInfo.Column>(8);
        _columnsCallHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("caller_name", new TableInfo.Column("caller_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("phone_number", new TableInfo.Column("phone_number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("call_type", new TableInfo.Column("call_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("date_time", new TableInfo.Column("date_time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("duration_seconds", new TableInfo.Column("duration_seconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("attended_by_device", new TableInfo.Column("attended_by_device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallHistory.put("ring_count", new TableInfo.Column("ring_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallHistory = new TableInfo("call_history", _columnsCallHistory, _foreignKeysCallHistory, _indicesCallHistory);
        final TableInfo _existingCallHistory = TableInfo.read(db, "call_history");
        if (!_infoCallHistory.equals(_existingCallHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "call_history(com.onecall.data.history.CallHistoryEntity).\n"
                  + " Expected:\n" + _infoCallHistory + "\n"
                  + " Found:\n" + _existingCallHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceHistory = new HashMap<String, TableInfo.Column>(6);
        _columnsDeviceHistory.put("device_id", new TableInfo.Column("device_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceHistory.put("device_name", new TableInfo.Column("device_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceHistory.put("first_connected_at", new TableInfo.Column("first_connected_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceHistory.put("last_seen_at", new TableInfo.Column("last_seen_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceHistory.put("calls_attended", new TableInfo.Column("calls_attended", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceHistory.put("calls_made", new TableInfo.Column("calls_made", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDeviceHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceHistory = new TableInfo("device_history", _columnsDeviceHistory, _foreignKeysDeviceHistory, _indicesDeviceHistory);
        final TableInfo _existingDeviceHistory = TableInfo.read(db, "device_history");
        if (!_infoDeviceHistory.equals(_existingDeviceHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "device_history(com.onecall.data.history.DeviceHistoryEntity).\n"
                  + " Expected:\n" + _infoDeviceHistory + "\n"
                  + " Found:\n" + _existingDeviceHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceConfig = new HashMap<String, TableInfo.Column>(10);
        _columnsDeviceConfig.put("deviceId", new TableInfo.Column("deviceId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("nickname", new TableInfo.Column("nickname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("deviceIcon", new TableInfo.Column("deviceIcon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("ringOnDevice", new TableInfo.Column("ringOnDevice", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("allowOutgoing", new TableInfo.Column("allowOutgoing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("autoApproveOutgoing", new TableInfo.Column("autoApproveOutgoing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("ringVolume", new TableInfo.Column("ringVolume", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("dndStartTime", new TableInfo.Column("dndStartTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("dndEndTime", new TableInfo.Column("dndEndTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceConfig.put("isCallsPaused", new TableInfo.Column("isCallsPaused", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceConfig = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDeviceConfig = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceConfig = new TableInfo("device_config", _columnsDeviceConfig, _foreignKeysDeviceConfig, _indicesDeviceConfig);
        final TableInfo _existingDeviceConfig = TableInfo.read(db, "device_config");
        if (!_infoDeviceConfig.equals(_existingDeviceConfig)) {
          return new RoomOpenHelper.ValidationResult(false, "device_config(com.onecall.data.settings.DeviceConfigEntity).\n"
                  + " Expected:\n" + _infoDeviceConfig + "\n"
                  + " Found:\n" + _existingDeviceConfig);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "fcbc1ef5b4ceb66298c7593821e0bf06", "6a9aef4e0d6405353a15fbad4ec48727");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "call_history","device_history","device_config");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `call_history`");
      _db.execSQL("DELETE FROM `device_history`");
      _db.execSQL("DELETE FROM `device_config`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(HistoryDao.class, HistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public HistoryDao historyDao() {
    if (_historyDao != null) {
      return _historyDao;
    } else {
      synchronized(this) {
        if(_historyDao == null) {
          _historyDao = new HistoryDao_Impl(this);
        }
        return _historyDao;
      }
    }
  }
}
