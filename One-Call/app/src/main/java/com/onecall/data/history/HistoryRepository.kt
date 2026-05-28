package com.onecall.data.history

import android.content.Context
import com.onecall.data.DeviceRoleStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface HistoryRepository {
    fun getCallHistory(): Flow<List<CallHistoryEntity>>
    suspend fun addCallHistory(entry: CallHistoryEntity)
    suspend fun deleteCallHistory(id: Long)
    suspend fun clearCallHistory()

    fun getDeviceHistory(): Flow<List<DeviceHistoryEntity>>
    suspend fun addOrUpdateDeviceHistory(entry: DeviceHistoryEntity)
    suspend fun getDeviceHistoryById(deviceId: String): DeviceHistoryEntity?
    suspend fun deleteDeviceHistory(deviceId: String)

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null

        fun getInstance(context: Context): HistoryRepository {
            return instance ?: synchronized(this) {
                val isMain = DeviceRoleStore.isMain(context)
                val newInstance = if (isMain) {
                    MainHistoryRepositoryImpl(HistoryDatabase.getDatabase(context).historyDao())
                } else {
                    SecondaryHistoryRepositoryImpl()
                }
                instance = newInstance
                newInstance
            }
        }
        
        fun clearInstance() {
            instance = null
        }
    }
}

class MainHistoryRepositoryImpl(private val dao: HistoryDao) : HistoryRepository {
    override fun getCallHistory(): Flow<List<CallHistoryEntity>> = dao.getAllCallHistory()
    override suspend fun addCallHistory(entry: CallHistoryEntity) {
        dao.insertCallHistory(entry)
        
        // Update device history stats
        val deviceName = entry.attendedByDevice ?: return
        val devices = dao.getAllDeviceHistory().firstOrNull() ?: return
        val device = devices.find { it.deviceName == deviceName }
        if (device != null) {
            val updated = if (entry.callType == "OUTGOING") {
                device.copy(callsMade = device.callsMade + 1)
            } else if (entry.callType == "INCOMING") {
                device.copy(callsAttended = device.callsAttended + 1)
            } else {
                device
            }
            dao.insertDeviceHistory(updated)
        }
    }
    override suspend fun deleteCallHistory(id: Long) = dao.deleteCallHistoryById(id)
    override suspend fun clearCallHistory() = dao.clearAllCallHistory()

    override fun getDeviceHistory(): Flow<List<DeviceHistoryEntity>> = dao.getAllDeviceHistory()
    override suspend fun addOrUpdateDeviceHistory(entry: DeviceHistoryEntity) = dao.insertDeviceHistory(entry)
    override suspend fun getDeviceHistoryById(deviceId: String) = dao.getDeviceHistoryById(deviceId)
    override suspend fun deleteDeviceHistory(deviceId: String) = dao.deleteDeviceHistoryById(deviceId)
}

class SecondaryHistoryRepositoryImpl : HistoryRepository {
    private val callHistoryState = MutableStateFlow<List<CallHistoryEntity>>(emptyList())
    // Secondary device doesn't track device history, but we mock it to fulfill interface
    private val deviceHistoryState = MutableStateFlow<List<DeviceHistoryEntity>>(emptyList())

    private var nextCallId = 1L

    override fun getCallHistory(): Flow<List<CallHistoryEntity>> = callHistoryState.asStateFlow()

    override suspend fun addCallHistory(entry: CallHistoryEntity) {
        val entryWithId = entry.copy(id = nextCallId++)
        callHistoryState.update { current -> 
            val newList = current.toMutableList()
            newList.add(0, entryWithId) // Add to top
            newList 
        }
    }

    override suspend fun deleteCallHistory(id: Long) {
        callHistoryState.update { current -> current.filter { it.id != id } }
    }

    override suspend fun clearCallHistory() {
        callHistoryState.value = emptyList()
    }

    override fun getDeviceHistory(): Flow<List<DeviceHistoryEntity>> = deviceHistoryState.asStateFlow()
    override suspend fun addOrUpdateDeviceHistory(entry: DeviceHistoryEntity) {}
    override suspend fun getDeviceHistoryById(deviceId: String): DeviceHistoryEntity? = null
    override suspend fun deleteDeviceHistory(deviceId: String) {}
}
