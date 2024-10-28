package com.ataglance.walletglance.account.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class AccountLocalDataSource(
    private val accountDao: AccountDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<AccountEntity>(
    dao = accountDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Account
) {

    @Transaction
    suspend fun deleteAllAccounts(timestamp: Long) {
        accountDao.deleteAllAccounts()
        updateLastModifiedTime(timestamp)
    }

}