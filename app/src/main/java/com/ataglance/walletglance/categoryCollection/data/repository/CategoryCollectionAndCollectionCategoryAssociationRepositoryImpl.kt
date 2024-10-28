package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionCategoryAssociationLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionCategoryAssociationRemoteDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionRemoteDataSource

class CategoryCollectionAndCollectionCategoryAssociationRepositoryImpl(
    override val entityLocalSource: CategoryCollectionLocalDataSource,
    override val entityRemoteSource: CategoryCollectionRemoteDataSource?,
    override val associationLocalSource: CategoryCollectionCategoryAssociationLocalDataSource,
    override val associationRemoteSource: CategoryCollectionCategoryAssociationRemoteDataSource?
) : CategoryCollectionAndCollectionCategoryAssociationRepository