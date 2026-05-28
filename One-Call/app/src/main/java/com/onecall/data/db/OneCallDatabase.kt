package com.onecall.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onecall.data.db.entities.CallHistoryEntity

@Database(entities = [CallHistoryEntity::class], version = 1, exportSchema = false)
abstract class OneCallDatabase : RoomDatabase() {

    abstract fun callHistoryDao(): CallHistoryDao

    companion object {
        @Volatile private var INSTANCE: OneCallDatabase? = null

        fun getDatabase(context: Context): OneCallDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    OneCallDatabase::class.java,
                    "onecall_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
