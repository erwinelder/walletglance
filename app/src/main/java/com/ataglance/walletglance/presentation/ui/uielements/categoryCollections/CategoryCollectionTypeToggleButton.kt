package com.ataglance.walletglance.presentation.ui.uielements.categoryCollections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.presentation.ui.uielements.buttons.TypeToggleButton

@Composable
fun CategoryCollectionTypeToggleButton(
    currentType: CategoryCollectionType,
    onClick: () -> Unit
) {
    val textRes = when (currentType) {
        CategoryCollectionType.Expense -> R.string.expenses
        CategoryCollectionType.Income -> R.string.income_plural
        CategoryCollectionType.Mixed -> R.string.mixed
    }

    TypeToggleButton(
        onClick = onClick,
        text = stringResource(textRes)
    )
}