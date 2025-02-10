package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single {
        FirebaseFirestore.getInstance()
    }

    /* ---------- DAOs ---------- */

    single {
        RemoteUpdateTimeDao(firestore = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        AppViewModel(
            settingsRepository = get(),

            saveAccountsUseCase = get(),
            getAccountsUseCase = get(),

            saveCategoriesUseCase = get(),
            getAllCategoriesUseCase = get(),

            saveCategoryCollectionsUseCase = get(),
            getCategoryCollectionsUseCase = get(),

            recordRepository = get(),
            saveRecordUseCase = get(),
            deleteRecordUseCase = get(),
            saveTransferUseCase = get(),
            deleteTransferUseCase = get(),
            getLastRecordNumUseCase = get(),
            getTodayTotalExpensesForAccountUseCase = get(),
            getRecordStacksInDateRangeUseCase = get(),

            saveBudgetsUseCase = get(),
            getBudgetsUseCase = get(),

            generalRepository = get()
        )
    }

    viewModel {
        NavigationViewModel(navigationButtonRepository = get())
    }

    viewModel {
        PersonalizationViewModel(widgetRepository = get(), budgetOnWidgetRepository = get())
    }

}