package com.ataglance.walletglance.categoryCollection.data.local.source

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync

interface CategoryCollectionLocalDataSource {

    suspend fun getCategoryCollectionUpdateTime(): Long?

    suspend fun saveCategoryCollectionUpdateTime(timestamp: Long)

    suspend fun deleteAllCategoryCollections(timestamp: Long)

    suspend fun synchroniseCategoryCollections(
        collectionsToSync: EntitiesToSync<CategoryCollectionEntity>,
        timestamp: Long
    )

    suspend fun getAllCategoryCollections(): List<CategoryCollectionEntity>


    suspend fun getCollectionCategoryAssociationUpdateTime(): Long?

    suspend fun saveCollectionCategoryAssociationUpdateTime(timestamp: Long)

    suspend fun synchroniseCollectionCategoryAssociations(
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryAssociation>,
        timestamp: Long
    )

    suspend fun getAllCollectionCategoryAssociations(): List<CategoryCollectionCategoryAssociation>


    suspend fun synchroniseCollectionsAndAssociations(
        collectionsToSync: EntitiesToSync<CategoryCollectionEntity>,
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryAssociation>,
        timestamp: Long
    )

}