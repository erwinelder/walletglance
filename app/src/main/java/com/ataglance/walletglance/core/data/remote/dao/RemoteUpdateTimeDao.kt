package com.ataglance.walletglance.core.data.remote.dao

import com.ataglance.walletglance.core.data.remote.mapper.toMap
import com.ataglance.walletglance.core.data.remote.model.RemoteUpdateTime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.getField
import kotlinx.coroutines.tasks.await

class RemoteUpdateTimeDao(
    private val firestore: FirebaseFirestore
) {

    suspend fun updateTime(entity: RemoteUpdateTime, userId: String) {
        firestore.collection("user_data")
            .document(userId)
            .collection("UpdateTime")
            .document(entity.tableName)
            .set(entity.toMap(), SetOptions.merge()).await()
    }

    suspend fun saveUpdateTime(tableName: String, timestamp: Long, userId: String) {
        updateTime(
            entity = RemoteUpdateTime(tableName = tableName, timestamp = timestamp),
            userId = userId
        )
    }

    suspend fun getUpdateTime(tableName: String, userId: String): Long? {
        return firestore.collection("user_data")
            .document(userId)
            .collection("UpdateTime")
            .document(tableName)
            .get().await()
            .getField("timestamp")
    }

}