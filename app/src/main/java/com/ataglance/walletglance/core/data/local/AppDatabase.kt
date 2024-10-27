package com.ataglance.walletglance.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ataglance.walletglance.account.data.local.AccountDao
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.budget.data.local.BudgetAccountAssociationDao
import com.ataglance.walletglance.budget.data.local.BudgetDao
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.category.data.local.CategoryDao
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionCategoryAssociationDao
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionDao
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.model.TableUpdateTime
import com.ataglance.walletglance.navigation.data.local.NavigationButtonDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetDao
import com.ataglance.walletglance.personalization.data.local.WidgetDao
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import com.ataglance.walletglance.record.data.local.RecordDao
import com.ataglance.walletglance.record.data.model.RecordEntity

@Database(
    entities = [
        TableUpdateTime::class,
        AccountEntity::class,
        CategoryEntity::class,
        CategoryCollectionEntity::class,
        CategoryCollectionCategoryAssociation::class,
        RecordEntity::class,
        BudgetEntity::class,
        BudgetAccountAssociation::class,
        NavigationButtonEntity::class,
        WidgetEntity::class,
        BudgetOnWidgetEntity::class
    ],
    version = 11,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val tableUpdateTimeDao: TableUpdateTimeDao
    abstract val accountDao: AccountDao
    abstract val categoryDao: CategoryDao
    abstract val categoryCollectionDao: CategoryCollectionDao
    abstract val categoryCollectionCategoryAssociationDao: CategoryCollectionCategoryAssociationDao
    abstract val recordDao: RecordDao
    abstract val budgetDao: BudgetDao
    abstract val budgetAccountAssociationDao: BudgetAccountAssociationDao
    abstract val navigationButtonDao: NavigationButtonDao
    abstract val widgetDao: WidgetDao
    abstract val budgetOnWidgetDao: BudgetOnWidgetDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {

                Room.databaseBuilder(
                    context, AppDatabase::class.java, "app_data"
                )

                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6,
                        MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11
                    )

                    /*.addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("App database", "Created")

                            super.onCreate(db)

                            val daoAccount = Instance?.accountDao
                            if (daoAccount != null) {
                                AccountViewModel(
                                    repository = AccountRepository(daoAccount),
                                    dao = daoAccount
                                )
                                    .addFirstAccount()
                            }
                        }
                    })*/

                    .build()
                    .also { Instance = it }

            }
        }
    }
}
