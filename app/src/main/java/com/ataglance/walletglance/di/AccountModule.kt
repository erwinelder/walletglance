package com.ataglance.walletglance.di

import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.local.source.getAccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.dao.AccountRemoteDao
import com.ataglance.walletglance.account.data.remote.dao.getAccountRemoteDao
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSourceImpl
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.account.domain.usecase.GetAllAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.GetAllAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCaseImpl
import org.koin.dsl.module

val accountModule = module {

    /* ---------- DAOs ---------- */

    single<AccountRemoteDao> {
        getAccountRemoteDao(firestore = get())
    }

    /* ---------- Data Sources ---------- */

    single<AccountLocalDataSource> {
        getAccountLocalDataSource(appDatabase = get())
    }

    single<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(accountDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<AccountRepository> {
        AccountRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveAccountsUseCase> {
        SaveAccountsUseCaseImpl(accountRepository = get(), recordRepository = )
    }

    single<GetAllAccountsUseCase> {
        GetAllAccountsUseCaseImpl(accountRepository = get())
    }

}