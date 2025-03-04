package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.components.ActiveAccountCard
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.components.CategoriesStatisticsWidget
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.animation.StartAnimatedContainer
import com.ataglance.walletglance.core.presentation.animation.WidgetStartAnimatedContainer
import com.ataglance.walletglance.core.presentation.components.containers.AppMainTopBar
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.widgets.ChosenBudgetsWidget
import com.ataglance.walletglance.core.presentation.components.widgets.ExpensesIncomeWidget
import com.ataglance.walletglance.core.presentation.components.widgets.GreetingsMessage
import com.ataglance.walletglance.core.utils.bottom
import com.ataglance.walletglance.core.utils.getCurrentDateLong
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.plus
import com.ataglance.walletglance.core.utils.top
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.mapper.toRecordStacks
import com.ataglance.walletglance.record.presentation.components.RecentRecordsWidget

@Composable
fun HomeScreen(
    scaffoldPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsAndActiveOne: AccountsAndActiveOne,
    onTopBarAccountClick: (Int) -> Unit,
    dateRangeWithEnum: DateRangeWithEnum,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    isCustomDateRangeWindowOpened: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    widgetNames: List<WidgetName>,
    onChangeHideActiveAccountBalance: () -> Unit,
    onWidgetSettingsButtonClick: (WidgetName) -> Unit,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit,
    widgetSettingsBottomSheets: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            StartAnimatedContainer(visible = isAppThemeSetUp) {
                AppMainTopBar(
                    accountList = accountsAndActiveOne.accounts,
                    currentDateRangeEnum = dateRangeWithEnum.enum,
                    onDateRangeChange = onDateRangeChange,
                    isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
                    onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                    onAccountClick = onTopBarAccountClick
                )
            }
        },
        containerColor = Color.Transparent
    ) { scaffoldHomeScreenPadding ->
        CompactLayout(
            scaffoldPadding = scaffoldPadding + scaffoldHomeScreenPadding,
            isAppThemeSetUp = isAppThemeSetUp,
            accountsAndActiveOne = accountsAndActiveOne,
            dateRangeWithEnum = dateRangeWithEnum,
            onChangeHideActiveAccountBalance = onChangeHideActiveAccountBalance,
            widgetNames = widgetNames,
            onWidgetSettingsButtonClick = onWidgetSettingsButtonClick,
            onNavigateToScreenMovingTowardsLeft = onNavigateToScreenMovingTowardsLeft,
            widgetSettingsBottomSheets = widgetSettingsBottomSheets
        )
    }
}

@Composable
private fun CompactLayout(
    scaffoldPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsAndActiveOne: AccountsAndActiveOne,
    dateRangeWithEnum: DateRangeWithEnum,
    onChangeHideActiveAccountBalance: () -> Unit,
    widgetNames: List<WidgetName>,
    onWidgetSettingsButtonClick: (WidgetName) -> Unit,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit,
    widgetSettingsBottomSheets: @Composable BoxScope.() -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = scaffoldPadding.top + dimensionResource(R.dimen.screen_vertical_padding),
                bottom = scaffoldPadding.bottom + dimensionResource(R.dimen.screen_vertical_padding)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                StartAnimatedContainer(visible = isAppThemeSetUp, delayMillis = 50) {
                    GreetingsMessage()
                }
            }
            item {
                StartAnimatedContainer(
                    visible = isAppThemeSetUp && accountsAndActiveOne.activeAccount != null,
                    delayMillis = 100
                ) {
                    ActiveAccountCard(
                        activeAccount = accountsAndActiveOne.activeAccount,
                        onChangeHideActiveAccountBalance = onChangeHideActiveAccountBalance
                    )
                }
            }
            itemsIndexed(items = widgetNames) { index, widgetName ->
                WidgetStartAnimatedContainer(visible = isAppThemeSetUp, index = index) {
                    when (widgetName) {
                        WidgetName.ChosenBudgets -> {
                            ChosenBudgetsWidget(
                                onSettingsButtonClick = {
                                    onWidgetSettingsButtonClick(WidgetName.ChosenBudgets)
                                },
                                onNavigateToBudgetsScreen = {
                                    onNavigateToScreenMovingTowardsLeft(MainScreens.Budgets)
                                },
                                onNavigateToBudgetStatisticsScreen = { id ->
                                    onNavigateToScreenMovingTowardsLeft(
                                        MainScreens.BudgetStatistics(id)
                                    )
                                }
                            )
                        }
                        WidgetName.TotalForPeriod -> {
                            ExpensesIncomeWidget(
                                activeAccount = accountsAndActiveOne.activeAccount,
                                dateRangeWithEnum = dateRangeWithEnum
                            )
                        }
                        WidgetName.RecentRecords -> {
                            RecentRecordsWidget(
                                accountsAndActiveOne = accountsAndActiveOne,
                                dateRangeWithEnum = dateRangeWithEnum,
                                onRecordClick = { recordNum: Int ->
                                    onNavigateToScreenMovingTowardsLeft(
                                        MainScreens.RecordCreation(recordNum = recordNum)
                                    )
                                },
                                onTransferClick = { recordNum: Int ->
                                    onNavigateToScreenMovingTowardsLeft(
                                        MainScreens.TransferCreation(recordNum = recordNum)
                                    )
                                },
                                onNavigateToRecordsScreen = {
                                    onNavigateToScreenMovingTowardsLeft(MainScreens.Records)
                                }
                            )
                        }
                        WidgetName.TopExpenseCategories -> {
                            CategoriesStatisticsWidget(
                                activeAccount = accountsAndActiveOne.activeAccount,
                                activeDateRange = dateRangeWithEnum.dateRange,
                                onNavigateToCategoriesStatisticsScreen = { categoryId, type ->
                                    if (type != null) {
                                        onNavigateToScreenMovingTowardsLeft(
                                            MainScreens.CategoryStatistics(
                                                parentCategoryId = categoryId, type = type.name
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        widgetSettingsBottomSheets()
    }
}



@Preview(
    apiLevel = 34,
    heightDp = 2000,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun HomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isBottomBarVisible: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountsAndActiveOne: AccountsAndActiveOne = AccountsAndActiveOne(
        accounts = listOf(
            Account(
                id = 1,
                orderNum = 1,
                name = "Main Card CZK",
                currency = "CZK",
                balance = 43551.63,
                color = AccountColors.Pink,
                isActive = true
            ),
            Account(
                id = 2,
                orderNum = 2,
                name = "USD Card",
                currency = "USD",
                balance = 1516.41,
                color = AccountColors.Blue,
                isActive = false
            )
        ),
        activeAccount = Account(
            id = 1,
            orderNum = 1,
            name = "Main Card CZK",
            currency = "CZK",
            balance = 43551.63,
            color = AccountColors.Pink,
            isActive = true
        )
    ),
    dateRangeMenuUiState: DateRangeMenuUiState = DateRangeMenuUiState.fromEnum(DateRangeEnum.ThisMonth),
    isCustomDateRangeWindowOpened: Boolean = false,
    widgetNamesList: List<WidgetName> = listOf(
        WidgetName.ChosenBudgets,
        WidgetName.TopExpenseCategories,
        WidgetName.RecentRecords,
        WidgetName.TotalForPeriod,
    ),
    recordList: List<RecordEntity>? = null,
    recordStackList: List<RecordStack> = recordList?.toRecordStacks(
        accounts = accountsAndActiveOne.accounts,
        groupedCategoriesByType = groupedCategoriesByType
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getCurrentDateLong(),
            type = RecordType.Expense,
            account = accountsAndActiveOne.accounts[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 2,
            date = getCurrentDateLong(),
            type = RecordType.OutTransfer,
            account = accountsAndActiveOne.accounts[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = accountsAndActiveOne.accounts[1].id.toString(),
                    includeInBudgets = true
                )
            )
        ),
    ),
    budgetsOnWidget: List<Budget> = listOf(
        Budget(
            id = 1,
            priorityNum = 1.0,
            amountLimit = 4000.0,
            usedAmount = 2250.0,
            usedPercentage = 56.25f,
            category = groupedCategoriesByType.expense[0].category,
            name = groupedCategoriesByType.expense[0].category.name,
            repeatingPeriod = RepeatingPeriod.Monthly,
            dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
            currentTimeWithinRangeGraphPercentage = .5f,
            currency = accountsAndActiveOne.activeAccount?.currency ?: "",
            linkedAccountsIds = listOf(1)
        )
    )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Home) }
    ) { scaffoldPadding ->
        HomeScreen(
            scaffoldPadding = scaffoldPadding,
            isAppThemeSetUp = true,
            accountsAndActiveOne = accountsAndActiveOne,
            onTopBarAccountClick = {},
            dateRangeWithEnum = dateRangeMenuUiState.dateRangeWithEnum,
            onDateRangeChange = {},
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onCustomDateRangeButtonClick = {},
            widgetNames = widgetNamesList,
            onChangeHideActiveAccountBalance = {},
            onWidgetSettingsButtonClick = {},
            onNavigateToScreenMovingTowardsLeft = {},
            widgetSettingsBottomSheets = {}
        )
    }
}
