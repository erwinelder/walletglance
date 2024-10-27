package com.ataglance.walletglance.core.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.first

abstract class BaseLocalDataSource<T>(
    private val dao: BaseDao<T>,
    private val updateTimeDao: TableUpdateTimeDao,
    private val tableName: TableName
) {

    suspend fun updateLastModifiedTime(timestamp: Long) {
        updateTimeDao.updateTime(tableName.name, timestamp)
    }

    suspend fun getLastModifierTime(): Long {
        return updateTimeDao.getUpdateTime(tableName.name).first()
    }

    @Transaction
    suspend fun upsertEntities(entityList: List<T>, timestamp: Long) {
        dao.upsertEntities(entityList)
        updateLastModifiedTime(timestamp)
    }

    @Transaction
    suspend fun deleteAndUpsertEntities(
        entitiesToDelete: List<T>,
        entitiesToUpsert: List<T>,
        timestamp: Long
    ) {
        if (entitiesToDelete.isNotEmpty()) {
            dao.deleteEntities(entitiesToDelete)
        }
        if (entitiesToUpsert.isNotEmpty()) {
            dao.upsertEntities(entitiesToUpsert)
        }
        updateLastModifiedTime(timestamp)
    }

}