package com.ataglance.walletglance.di

import com.ataglance.walletglance.category.data.local.source.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.local.source.getCategoryLocalDataSource
import com.ataglance.walletglance.category.data.remote.dao.CategoryRemoteDao
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSource
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSourceImpl
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepositoryImpl
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.GetLastUsedRecordCategoryUseCase
import com.ataglance.walletglance.category.domain.usecase.GetLastUsedRecordCategoryUseCaseImpl
import com.ataglance.walletglance.category.domain.usecase.SaveCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.SaveCategoriesUseCaseImpl
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import org.koin.dsl.module

val categoryModule = module {

    /* ---------- DAOs ---------- */

    single {
        CategoryRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get()).getCategoryFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<CategoryLocalDataSource> {
        getCategoryLocalDataSource(appDatabase = get())
    }

    single<CategoryRemoteDataSource> {
        CategoryRemoteDataSourceImpl(categoryDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<CategoryRepository> {
        CategoryRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveCategoriesUseCase> {
        SaveCategoriesUseCaseImpl(categoryRepository = get())
    }

    single<GetCategoriesUseCase> {
        GetCategoriesUseCaseImpl(categoryRepository = get())
    }

    single<GetLastUsedRecordCategoryUseCase> {
        GetLastUsedRecordCategoryUseCaseImpl(
            getAccountsUseCase = get(),
            getCategoriesUseCase = get(),
            getRecordStackUseCase = get()
        )
    }

}