package com.onecall.data.db

import androidx.room.*
import com.onecall.data.db.entities.CallHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallHistoryDao {

    @Query("SELECT * FROM call_history WHERE isPermanent = 1 ORDER BY timestamp DESC")
    fun getAllPermanentHistory(): Flow<List<CallHistoryEntity>>

    @Query("SELECT * FROM call_history WHERE isPermanent = 1 AND callType = :type ORDER BY timestamp DESC")
    fun getHistoryByType(type: String): Flow<List<CallHistoryEntity>>

    @Query("SELECT * FROM call_history WHERE isPermanent = 0 ORDER BY timestamp DESC")
    fun getSessionHistory(): Flow<List<CallHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CallHistoryEntity)

    @Query("DELETE FROM call_history WHERE isPermanent = 1")
    suspend fun clearPermanentHistory()

    @Query("DELETE FROM call_history WHERE isPermanent = 0")
    suspend fun clearSessionHistory()
}
