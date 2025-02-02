package com.ataglance.walletglance.recordCreation.domain.record

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.DateTimeState

data class RecordDraftGeneral(
    val isNew: Boolean,
    val recordNum: Int,
    val account: Account?,
    val type: CategoryType = CategoryType.Expense,
    val dateTimeState: DateTimeState = DateTimeState(),
    val preferences: RecordDraftPreferences = RecordDraftPreferences()
) {

    fun savingIsAllowed(): Boolean {
        return account != null
    }

}
