package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.firstOrNull

class SaveAccountsUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val recordRepository: RecordRepository
) : SaveAccountsUseCase {

    override suspend fun saveDomainModels(accounts: List<Account>) {
        val currentAccounts = accountRepository.getAllAccounts().firstOrNull().orEmpty()

        val entitiesToSave = accounts.map(Account::toDataModel)
        val entitiesToDelete = currentAccounts.excludeItems(entitiesToSave) { it.id }

        if (entitiesToDelete.isEmpty()) {
            accountRepository.deleteAndUpsertAccounts(
                toDelete = entitiesToDelete, toUpsert = entitiesToSave
            )
        } else {
            recordRepository.convertRecordsToTransfers(
                noteValues = entitiesToDelete.map { it.id.toString() }
            )
        }
    }

    override suspend fun saveDataModels(accounts: List<AccountEntity>) {
        accountRepository.upsertAccounts(accounts = accounts)
    }

}