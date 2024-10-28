package com.ataglance.walletglance.personalization.data.remote

import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.mapper.toBudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.mapper.toMap
import com.google.firebase.firestore.FirebaseFirestore

class BudgetOnWidgetRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<BudgetOnWidgetEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "budgetsOnWidgets",
    tableName = TableName.BudgetOnWidget,
    getDocumentRef = { this.document(it.budgetId.toString()) },
    dataToEntityMapper = Map<String, Any?>::toBudgetOnWidgetEntity,
    entityToDataMapper = BudgetOnWidgetEntity::toMap
)