package com.ataglance.walletglance.ui.theme.uielements.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.utils.asStringRes
import com.ataglance.walletglance.ui.theme.screens.settings.budgets.EditBudgetsScreenPreview
import com.ataglance.walletglance.ui.theme.uielements.dividers.TextDivider

@Composable
fun BudgetListsByPeriodComponent(
    budgetsByType: BudgetsByType,
    budgetComponent: @Composable (Budget) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            budgetsByType.daily.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Daily,
                    budgetComponent = budgetComponent
                )
            }
        }
        item {
            budgetsByType.weekly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Weekly,
                    budgetComponent = budgetComponent
                )
            }
        }
        item {
            budgetsByType.monthly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    budgetComponent = budgetComponent
                )
            }
        }
        item {
            budgetsByType.yearly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Yearly,
                    budgetComponent = budgetComponent
                )
            }
        }
    }
}

@Composable
private fun BudgetListByTypeComponent(
    budgetList: List<Budget>,
    repeatingPeriod: RepeatingPeriod,
    budgetComponent: @Composable (Budget) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextDivider(
            modifier = Modifier.fillMaxWidth(.9f),
            textRes = repeatingPeriod.asStringRes()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            budgetList.forEach {
                budgetComponent(it)
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    EditBudgetsScreenPreview()
}