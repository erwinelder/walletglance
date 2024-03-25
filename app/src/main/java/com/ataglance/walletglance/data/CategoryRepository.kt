package com.ataglance.walletglance.data

import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val dao: CategoryDao
) {

    suspend fun upsertCategories(categoryList: List<Category>) {
        dao.insertOrReplaceCategories(categoryList)
    }

    suspend fun deleteAllCategories() {
        dao.deleteAllCategories()
    }

    suspend fun deleteAndUpsertCategories(
        idListToDelete: List<Int>,
        categoryListToUpsert: List<Category>
    ) {
        dao.deleteAndUpsertCategories(idListToDelete, categoryListToUpsert)
    }

    fun getCategories(): Flow<List<Category>> {
        return dao.getAllCategories()
    }
}