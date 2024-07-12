package com.ataglance.walletglance.ui.viewmodels.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.records.MakeRecordStatus
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.ui.utils.copyWithCategoryWithSubcategory
import com.ataglance.walletglance.ui.utils.toCategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class MakeRecordViewModel(
    categoryWithSubcategory: CategoryWithSubcategory?,
    makeRecordUiState: MakeRecordUiState,
    makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MakeRecordUiState> = MutableStateFlow(makeRecordUiState)
    val uiState: StateFlow<MakeRecordUiState> = _uiState.asStateFlow()

    fun selectNewDate(selectedDateMillis: Long) {
        _uiState.update {
            it.copy(
                dateTimeState = uiState.value.dateTimeState.getNewDate(selectedDateMillis)
            )
        }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                dateTimeState = uiState.value.dateTimeState.getNewTime(hour, minute)
            )
        }
    }

    fun changeRecordType(
        type: RecordType,
        categoriesWithSubcategories: CategoriesWithSubcategories
    ) {
        _uiState.update { it.copy(type = type) }
        changeAllUnitCategoriesByType(type, categoriesWithSubcategories)
    }


    private val _recordUnitList: MutableStateFlow<List<MakeRecordUnitUiState>> = MutableStateFlow(
        makeRecordUnitList ?: listOf(
                MakeRecordUnitUiState(
                    lazyListKey = 0,
                    index = 0,
                    categoryWithSubcategory = categoryWithSubcategory,
                    note = "",
                    collapsed = false
                )
            )
    )
    val recordUnitList: StateFlow<List<MakeRecordUnitUiState>> = _recordUnitList.asStateFlow()

    private fun changeAllUnitCategoriesByType(
        type: RecordType,
        categoriesWithSubcategories: CategoriesWithSubcategories
    ) {
        val categoryWithSubcategory = categoriesWithSubcategories
            .getLastCategoryWithSubcategoryByType(type.toCategoryType())
        _recordUnitList.update {
            recordUnitList.value.copyWithCategoryWithSubcategory(categoryWithSubcategory)
        }
    }

    fun chooseAccount(account: Account) {
        _uiState.update { it.copy(account = account) }
    }

    fun changeAccount(currentAccount: Account, accountList: List<Account>) {
        if (accountList.size < 2) return

        for (i in accountList.indices) {
            if (accountList[i].id == currentAccount.id ) {
                (accountList.getOrNull(i + 1) ?: accountList.getOrNull(i - 1))
                    ?.let { account ->
                        _uiState.update { it.copy(account = account) }
                    } ?: return
                break
            }
        }
    }

    fun changeNoteValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        newList[index] = newList[index].copy(note = value)
        _recordUnitList.update { newList }
    }

    fun changeClickedUnitIndex(index: Int) {
        _uiState.update { it.copy(clickedUnitIndex = index) }
    }

    fun chooseCategory(categoryWithSubcategory: CategoryWithSubcategory) {
        val newList = recordUnitList.value.toMutableList()
        newList[uiState.value.clickedUnitIndex] = newList[uiState.value.clickedUnitIndex].copy(
            categoryWithSubcategory = categoryWithSubcategory
        )
        _recordUnitList.update { newList }
    }

    fun changeAmountValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        val newValue = value.takeIf {
            Regex("^(?:[0-9]\\d{0,9}(?:[.]\\d{0,2})?)?\$").matches(it)
        } ?: return
        newList[index] = newList[index].copy(amount = newValue)
        _recordUnitList.update { newList }
    }

    fun changeQuantityValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        val newValue = value.takeIf {
            Regex("^\\d*\$").matches(it)
        } ?: return
        newList[index] = newList[index].copy(quantity = newValue)
        _recordUnitList.update { newList }
    }

    fun changeCollapsedValue(index: Int, collapsed: Boolean) {
        val newList = recordUnitList.value.toMutableList()

        if (!collapsed) {
            for (i in 0..newList.lastIndex) {
                if (i == index) {
                    newList[i] = newList[i].copy(collapsed = false)
                } else {
                    newList[i] = newList[i].copy(collapsed = true)
                }
            }
        } else {
            newList[index] = newList[index].copy(collapsed = true)
        }

        _recordUnitList.update { newList }
    }

    fun addNewRecordUnit() {
        val newList = recordUnitList.value.toMutableList()
        for (i in 0..newList.lastIndex) {
            newList[i] = newList[i].copy(collapsed = true)
        }
        newList.add(
            MakeRecordUnitUiState(
                lazyListKey = newList.maxOfOrNull { it.lazyListKey }?.let { it + 1 } ?: 0,
                index = newList.lastOrNull()?.let { it.index + 1 } ?: 0,
                categoryWithSubcategory = newList.lastOrNull()?.categoryWithSubcategory,
                collapsed = false
            )
        )
        _recordUnitList.update { newList }
    }

    fun swapRecordUnits(firstIndex: Int, secondIndex: Int) {
        if (firstIndex !in 0..<recordUnitList.value.size || secondIndex !in 0..<recordUnitList.value.size) {
            return
        }

        val firstUnit = recordUnitList.value[firstIndex]
        val secondUnit = recordUnitList.value[secondIndex]

        val newList = mutableListOf<MakeRecordUnitUiState>()

        recordUnitList.value.forEach { unit ->
            if (unit.index != firstUnit.index && unit.index != secondUnit.index) {
                newList.add(unit)
            } else if (unit.index == firstUnit.index) {
                newList.add(secondUnit.copy(index = unit.index))
            } else {
                newList.add(firstUnit.copy(index = unit.index))
            }
        }

        _recordUnitList.update { newList }
    }

    fun deleteRecordUnit(index: Int) {
        if (recordUnitList.value.isEmpty()) {
            return
        }

        val newList = mutableListOf<MakeRecordUnitUiState>()
        var stepAfterDeleting = 0

        recordUnitList.value.forEach { unit ->
            if (unit.index != index) {
                newList.add(unit.copy(index = unit.index - stepAfterDeleting))
            } else {
                stepAfterDeleting = 1
            }
        }

        _recordUnitList.update { newList }
    }

}

class MakeRecordViewModelFactory(
    private val categoryWithSubcategory: CategoryWithSubcategory?,
    private val makeRecordUiState: MakeRecordUiState,
    private val makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakeRecordViewModel(
            categoryWithSubcategory = categoryWithSubcategory,
            makeRecordUiState = makeRecordUiState,
            makeRecordUnitList = makeRecordUnitList,
        ) as T
    }
}

data class MakeRecordUiState(
    val recordStatus: MakeRecordStatus,
    val recordNum: Int,
    val account: Account?,
    val type: RecordType = RecordType.Expense,
    val clickedUnitIndex: Int = 0,
    val dateTimeState: DateTimeState = DateTimeState()
) {

    fun toRecordList(unitList: List<MakeRecordUnitUiState>): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (account != null && unit.categoryWithSubcategory != null) {
                recordList.add(
                    Record(
                        recordNum = recordNum,
                        date = dateTimeState.dateLong,
                        type = if (type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                Locale.US,
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.categoryWithSubcategory.category.id,
                        subcategoryId = unit.categoryWithSubcategory.subcategory?.id,
                        accountId = account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

    fun toRecordListWithOldIds(
        unitList: List<MakeRecordUnitUiState>,
        recordStack: RecordStack
    ): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (account != null && unit.categoryWithSubcategory != null) {
                recordList.add(
                    Record(
                        id = recordStack.stack[unit.index].id,
                        recordNum = recordStack.recordNum,
                        date = dateTimeState.dateLong,
                        type = if (type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                Locale.US,
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.categoryWithSubcategory.category.id,
                        subcategoryId = unit.categoryWithSubcategory.subcategory?.id,
                        accountId = account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

}

data class MakeRecordUnitUiState(
    val lazyListKey: Int,
    val index: Int,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String = "",
    val amount: String = "",
    val quantity: String = "",
    val collapsed: Boolean = true
) {

    fun getSubcategoryOrCategory(): Category? {
        return categoryWithSubcategory?.getSubcategoryOrCategory()
    }

    fun getFormattedAmountWithSpaces(): String {
        var numberString = "%.2f".format(
            Locale.US,
            amount.takeIf { it.isNotBlank() }?.toDouble() ?: return "------"
        )
        var formattedNumber = numberString.let {
            it.substring(startIndex = it.length - 3)
        }
        numberString = numberString.let {
            it.substring(0, it.length - 3)
        }
        var digitCount = 0

        for (i in numberString.length - 1 downTo 0) {
            formattedNumber = numberString[i] + formattedNumber
            digitCount++
            if (digitCount % 3 == 0 && i != 0) {
                formattedNumber = " $formattedNumber"
            }
        }

        return formattedNumber
    }

}