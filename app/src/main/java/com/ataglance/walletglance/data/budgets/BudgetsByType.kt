package com.ataglance.walletglance.data.budgets

import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.utils.addUsedAmountsByRecords
import com.ataglance.walletglance.data.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.data.utils.findById
import com.ataglance.walletglance.data.utils.getMaxIdOrZero
import com.ataglance.walletglance.data.utils.subtractUsedAmountsByRecords
import com.ataglance.walletglance.domain.entities.Record

data class BudgetsByType(
    val daily: List<Budget> = emptyList(),
    val weekly: List<Budget> = emptyList(),
    val monthly: List<Budget> = emptyList(),
    val yearly: List<Budget> = emptyList()
) {

    fun areEmpty(): Boolean {
        return daily.isEmpty() && weekly.isEmpty() && monthly.isEmpty() && yearly.isEmpty()
    }

    fun findById(id: Int): Budget? {
        return daily.findById(id)
            ?: weekly.findById(id)
            ?: monthly.findById(id)
            ?: yearly.findById(id)
    }

    fun concatenate(): List<Budget> {
        return daily + weekly + monthly + yearly
    }

    fun getByType(type: RepeatingPeriod): List<Budget> {
        return when (type) {
            RepeatingPeriod.Daily -> daily
            RepeatingPeriod.Weekly -> weekly
            RepeatingPeriod.Monthly -> monthly
            RepeatingPeriod.Yearly -> yearly
        }
    }

    private fun replaceListByType(list: List<Budget>, type: RepeatingPeriod): BudgetsByType {
        return when (type) {
            RepeatingPeriod.Daily -> this.copy(daily = list)
            RepeatingPeriod.Weekly -> this.copy(daily = list)
            RepeatingPeriod.Monthly -> this.copy(daily = list)
            RepeatingPeriod.Yearly -> this.copy(daily = list)
        }
    }

    fun addBudget(budget: Budget): BudgetsByType {
        val newId = concatenate().getMaxIdOrZero() + 1

        val newList = (getByType(budget.repeatingPeriod) + listOf(budget.copy(id = newId)))
            .sortedBy { it.priorityNum }

        return replaceListByType(newList, budget.repeatingPeriod)
    }

    fun deleteBudget(id: Int, repeatingPeriod: RepeatingPeriod): BudgetsByType {
        return when (repeatingPeriod) {
            RepeatingPeriod.Daily -> this.copy(daily = daily.filter { it.id != id })
            RepeatingPeriod.Weekly -> this.copy(weekly = weekly.filter { it.id != id })
            RepeatingPeriod.Monthly -> this.copy(monthly = monthly.filter { it.id != id })
            RepeatingPeriod.Yearly -> this.copy(yearly = yearly.filter { it.id != id })
        }
    }

    fun getMaxDateRange(): LongDateRange? {
        return (yearly.takeIf { it.isNotEmpty() }
            ?: monthly.takeIf { it.isNotEmpty() }
            ?: weekly.takeIf { it.isNotEmpty() }
            ?: daily.takeIf { it.isNotEmpty() })
                ?.first()?.dateRange
    }

    fun fillUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        var recordsInDateRange = recordList.filterByBudgetsDateRange(yearly)
        val filledYearlyBudgets = recordsInDateRange?.let { yearly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(monthly)
        val filledMonthlyBudgets = recordsInDateRange?.let { monthly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(weekly)
        val filledWeeklyBudgets = recordsInDateRange?.let { weekly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(daily)
        val filledDailyBudgets = recordsInDateRange?.let { daily.fillUsedAmountsByRecords(it) }

        return this.copy(
            daily = filledDailyBudgets ?: emptyList(),
            weekly = filledWeeklyBudgets ?: emptyList(),
            monthly = filledMonthlyBudgets ?: emptyList(),
            yearly = filledYearlyBudgets ?: emptyList()
        )
    }

    fun addUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        if (recordList.isEmpty()) return this

        return applyRecordAmountsToBudgets(
            recordList = recordList,
            applyFunction = List<Budget>::addUsedAmountsByRecords
        )
    }

    fun subtractUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        if (recordList.isEmpty()) return this

        return applyRecordAmountsToBudgets(
            recordList = recordList,
            applyFunction = List<Budget>::subtractUsedAmountsByRecords
        )
    }

    private fun applyRecordAmountsToBudgets(
        recordList: List<Record>,
        applyFunction: List<Budget>.(List<Record>) -> List<Budget>
    ): BudgetsByType {
        var recordsInDateRange = recordList.filterByBudgetsDateRange(yearly)
            ?.takeIf { it.isNotEmpty() }
        val filledYearlyBudgets = recordsInDateRange?.let { yearly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(monthly)
            ?.takeIf { it.isNotEmpty() }
        val filledMonthlyBudgets = recordsInDateRange?.let { monthly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(weekly)
            ?.takeIf { it.isNotEmpty() }
        val filledWeeklyBudgets = recordsInDateRange?.let { weekly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(daily)
            ?.takeIf { it.isNotEmpty() }
        val filledDailyBudgets = recordsInDateRange?.let { daily.applyFunction(it) }

        return this.copy(
            daily = filledDailyBudgets ?: daily,
            weekly = filledWeeklyBudgets ?: weekly,
            monthly = filledMonthlyBudgets ?: monthly,
            yearly = filledYearlyBudgets ?: yearly
        )
    }

    private fun List<Record>.filterByBudgetsDateRange(budgetList: List<Budget>): List<Record>? {
        return budgetList.firstOrNull()?.dateRange?.let { dateRange ->
            this.filter { dateRange.containsDate(it.date) }
        }
    }

}
