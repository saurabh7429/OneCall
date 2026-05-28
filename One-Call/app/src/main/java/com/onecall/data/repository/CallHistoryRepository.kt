package com.onecall.data.repository

import com.onecall.data.db.CallHistoryDao
import com.onecall.data.db.entities.CallHistoryEntity
import kotlinx.coroutines.flow.Flow

class CallHistoryRepository(private val dao: CallHistoryDao) {

    val allPermanentHistory: Flow<List<CallHistoryEntity>> = dao.getAllPermanentHistory()
    val sessionHistory: Flow<List<CallHistoryEntity>> = dao.getSessionHistory()

    fun getHistoryByType(type: String): Flow<List<CallHistoryEntity>> =
        dao.getHistoryByType(type)

    suspend fun insert(entry: CallHistoryEntity) = dao.insert(entry)

    suspend fun clearPermanentHistory() = dao.clearPermanentHistory()

    suspend fun clearSessionHistory() = dao.clearSessionHistory()
}
