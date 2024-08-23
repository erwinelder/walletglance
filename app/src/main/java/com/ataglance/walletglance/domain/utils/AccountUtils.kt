package com.ataglance.walletglance.domain.utils

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.color.AccountColorWithName
import com.ataglance.walletglance.data.accounts.color.AccountColors
import com.ataglance.walletglance.data.accounts.color.AccountPossibleColors
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.color.ColorWithName
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.mappers.toAccountList
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.data.utils.findById
import java.util.Locale


fun AccountColors.toAccountColorWithName(): AccountColorWithName {
    return AccountColorWithName(this.name, this.color, this.colorOn)
}


fun AccountColors.toColorWithName(theme: AppTheme?): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).lighter)
}


fun List<AccountEntity>.toAccountList(): List<Account> {
    val possibleColors = AccountPossibleColors()
    return this.toAccountList(accountColorProvider = possibleColors::getByName)
}


fun List<Account>.getIdsThatAreNotInList(list: List<AccountEntity>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
}


fun List<Account>.findById(id: Int): Account? {
    return this.find { it.id == id }
}


fun List<Account>.findByOrderNum(orderNum: Int): Account? {
    return this.find { it.orderNum == orderNum }
}


fun List<Account>.getOtherFrom(account: Account): Account {
    for (i in this.indices) {
        if (this[i].id == account.id) {
            return if (this.lastIndex > i) {
                this[i + 1]
            } else if (i > 0) {
                this[i - 1]
            } else {
                account
            }
        }
    }
    return account
}


fun List<Account>.mergeWith(list: List<Account>): List<Account> {
    val mergedList = this.toMutableList()

    list.forEach { accountFromSecondaryList ->
        accountFromSecondaryList
            .takeIf { mergedList.findById(it.id) == null }
            ?.let { mergedList.add(it) }
    }

    return mergedList
}


fun Pair<Account, Account>.returnAmountToFirstBalanceAndUpdateSecondBalance(
    prevAmount: Double,
    newAmount: Double,
    recordType: RecordType
): Pair<Account, Account> {
    return this.first.copy(
        balance = "%.2f".format(
            Locale.US,
            this.first.balance +
                    if (recordType == RecordType.Expense) prevAmount else -prevAmount
        ).toDouble()
    ) to this.second.copy(
        balance = "%.2f".format(
            Locale.US,
            this.second.balance +
                    if (recordType == RecordType.Expense) -newAmount else newAmount
        ).toDouble()
    )
}
