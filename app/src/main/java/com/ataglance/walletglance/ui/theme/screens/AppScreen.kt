package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.model.AccountController
import com.ataglance.walletglance.model.AccountsUiState
import com.ataglance.walletglance.model.AppScreen
import com.ataglance.walletglance.model.AppUiSettings
import com.ataglance.walletglance.model.AppViewModel
import com.ataglance.walletglance.model.CategoriesUiState
import com.ataglance.walletglance.model.CategoryController
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.model.CurrencyPickerScreenArgs
import com.ataglance.walletglance.model.CurrencyPickerViewModel
import com.ataglance.walletglance.model.CurrencyPickerViewModelFactory
import com.ataglance.walletglance.model.DateRangeMenuUiState
import com.ataglance.walletglance.model.EditAccountScreenArgs
import com.ataglance.walletglance.model.EditAccountUiState
import com.ataglance.walletglance.model.EditAccountViewModel
import com.ataglance.walletglance.model.EditAccountViewModelFactory
import com.ataglance.walletglance.model.EditSubcategoryListScreenArgs
import com.ataglance.walletglance.model.LanguageViewModel
import com.ataglance.walletglance.model.MakeRecordScreenArgs
import com.ataglance.walletglance.model.MakeRecordStatus
import com.ataglance.walletglance.model.MakeRecordViewModel
import com.ataglance.walletglance.model.MakeRecordViewModelFactory
import com.ataglance.walletglance.model.MakeTransferViewModel
import com.ataglance.walletglance.model.MakeTransferViewModelFactory
import com.ataglance.walletglance.model.RecordController
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.model.RecordsTypeFilter
import com.ataglance.walletglance.model.SettingsScreen
import com.ataglance.walletglance.model.SetupAccountsViewModel
import com.ataglance.walletglance.model.SetupCategoriesViewModel
import com.ataglance.walletglance.model.ThemeUiState
import com.ataglance.walletglance.model.WidgetsUiState
import com.ataglance.walletglance.model.sharedViewModel
import com.ataglance.walletglance.ui.theme.animation.CustomAnimation
import com.ataglance.walletglance.ui.theme.screens.settings.CurrencyPickerScreen
import com.ataglance.walletglance.ui.theme.screens.settings.EditCategoryScreen
import com.ataglance.walletglance.ui.theme.screens.settings.EditSubcategoryListScreen
import com.ataglance.walletglance.ui.theme.screens.settings.RecordsScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SettingsDataScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SettingsHomeScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupAccountsScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupAppearanceScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupCategoriesScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupFinishScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupImportScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupLanguageScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupStartScreen
import com.ataglance.walletglance.ui.theme.uielements.BottomNavBar
import com.ataglance.walletglance.ui.theme.uielements.SetupProgressTopBar
import com.ataglance.walletglance.ui.theme.uielements.containers.CustomDateRangeWindow
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDateRangePicker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navigateToScreen = { screenRouteNavigateTo: String ->
        if (
            navBackStackEntry?.destination?.route != screenRouteNavigateTo &&
            !(
                navBackStackEntry?.destination?.route == SettingsScreen.SettingsHome.route &&
                screenRouteNavigateTo == AppScreen.Settings.route
            )
        ) {
            navController.navigate(screenRouteNavigateTo) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val accountsUiState by appViewModel.accountsUiState.collectAsStateWithLifecycle()
    val categoriesUiState by appViewModel.categoriesUiState.collectAsStateWithLifecycle()
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val recordsTypeFilter by appViewModel.recordsTypeFilter.collectAsStateWithLifecycle()
    val recordStackList by appViewModel.recordStackList.collectAsStateWithLifecycle()
    val filteredRecordStackList by appViewModel.filteredRecordStackList.collectAsStateWithLifecycle()
    val widgetsUiState by appViewModel.widgetsUiState.collectAsStateWithLifecycle()

    val openCustomDateRangeWindow = remember { mutableStateOf(false) }
    val openDateRangePickerDialog = remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()
    dateRangePickerState.setSelection(
        dateRangeMenuUiState.startCalendarDateMillis,
        dateRangeMenuUiState.endCalendarDateMillis
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SetupProgressTopBar(
                visible = appUiSettings.startMainDestination != AppScreen.Home.route &&
                        navBackStackEntry?.destination?.route != SettingsScreen.Start.route &&
                        navBackStackEntry?.destination?.route != AppScreen.FinishSetup.route,
                navBackStackEntry = navBackStackEntry,
                onBackNavigationButton = navController::popBackStack
            )
        },
        bottomBar = {
            BottomNavBar(
                isAppSetUp = appUiSettings.isSetUp,
                navBackStackEntry = navBackStackEntry,
                navigateBack = navController::popBackStack,
                onNavigationButton = navigateToScreen,
                onMakeRecordButtonClick = {
                    navController.navigate(
                        "${AppScreen.MakeRecord.route}/${MakeRecordStatus.Create}/0"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        HomeNavHost(
            navController = navController,
            startMainDestination = appUiSettings.startMainDestination,
            startSettingsDestination = appUiSettings.startSettingsDestination,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            themeUiState = themeUiState,
            accountsUiState = accountsUiState,
            categoriesUiState = categoriesUiState,
            dateRangeMenuUiState = dateRangeMenuUiState,
            recordsTypeFilter = recordsTypeFilter,
            filteredRecordStackList = filteredRecordStackList,
            recordStackList = recordStackList,
            widgetsUiState = widgetsUiState,
            openCustomDateRangeWindow = openCustomDateRangeWindow.value,
            onCustomDateRangeButtonClick = {
                openCustomDateRangeWindow.value = !openCustomDateRangeWindow.value
            },
            onNavigateToMakeRecordScreen = { makeRecordStatus: MakeRecordStatus, orderNum: Int ->
                navController.navigate(
                    "${AppScreen.MakeRecord.route}/${makeRecordStatus}/${orderNum}"
                ) {
                    launchSingleTop = true
                }
            },
            onNavigateToMakeTransferScreen = { makeRecordStatus: MakeRecordStatus, orderNum: Int ->
                navController.navigate(
                    "${AppScreen.MakeTransfer.route}/${makeRecordStatus}/${orderNum}"
                ) {
                    launchSingleTop = true
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomDateRangeWindow(
                visible = openCustomDateRangeWindow.value,
                padding = PaddingValues(
                    top = scaffoldPadding.calculateTopPadding() +
                            scaffoldPadding.calculateTopPadding(),
                    bottom = scaffoldPadding.calculateBottomPadding() +
                            scaffoldPadding.calculateBottomPadding(),
                    end = 16.dp
                ),
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
                dateRangePickerState = dateRangePickerState,
                onDismissRequest = {
                    openCustomDateRangeWindow.value = false
                },
                onDateRangeEnumClick = { dateRangeEnum ->
                    appViewModel.changeDateRange(dateRangeEnum)
                    openCustomDateRangeWindow.value = false
                },
                onCustomDateRangeFieldClick = {
                    openDateRangePickerDialog.value = true
                },
                onConfirmButtonClick = {
                    openCustomDateRangeWindow.value = false
                    appViewModel.changeDateRangeToCustom(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )
                }
            )
            CustomDateRangePicker(
                state = dateRangePickerState,
                openDialog = openDateRangePickerDialog.value,
                onOpenDialogChange = { value ->
                    openDateRangePickerDialog.value = value
                }
            )
        }
    }
}

@Composable
fun HomeNavHost(
    navController: NavHostController,
    startMainDestination: String,
    startSettingsDestination: String,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountsUiState: AccountsUiState,
    categoriesUiState: CategoriesUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    recordsTypeFilter: RecordsTypeFilter,
    recordStackList: List<RecordStack>,
    filteredRecordStackList: List<RecordStack>,
    widgetsUiState: WidgetsUiState,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onNavigateToMakeRecordScreen: (MakeRecordStatus, Int) -> Unit,
    onNavigateToMakeTransferScreen: (MakeRecordStatus, Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startMainDestination
    ) {
        composable(
            route = AppScreen.Home.route,
            enterTransition = { fadeIn(tween(400)) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            HomeScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountsUiState = accountsUiState,
                dateRangeMenuUiState = dateRangeMenuUiState,
                recordsTypeFilter = recordsTypeFilter,
                filteredRecordStackList = filteredRecordStackList,
                categoriesUiState = categoriesUiState,
                categoryNameAndIconMap = appViewModel.categoryNameAndIconMap,
                widgetsUiState = widgetsUiState,
                onChangeHideActiveAccountBalance = appViewModel::onChangeHideActiveAccountBalance,
                onDateRangeChange = appViewModel::changeDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                onTopBarAccountClick = { orderNum ->
                    appViewModel.chooseNewActiveAccount(orderNum)
                },
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onNavigateToRecordsScreen = {
                    navController.navigate(AppScreen.Records.route)
                },
                onRecordClick = { orderNum: Int ->
                    onNavigateToMakeRecordScreen(MakeRecordStatus.Edit, orderNum)
                },
                onTransferClick = { orderNum: Int ->
                    onNavigateToMakeTransferScreen(MakeRecordStatus.Edit, orderNum)
                }
            )
        }
        composable(
            route = AppScreen.Records.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = accountsUiState.accountList,
                filteredRecordStackList = filteredRecordStackList,
                appTheme = appUiSettings.appTheme,
                onAccountClick = { orderNum ->
                    appViewModel.chooseNewActiveAccount(orderNum)
                },
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::changeDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                recordsTypeFilter = recordsTypeFilter,
                onRecordsTypeFilterChange = appViewModel::changeRecordsTypeFilter,
                getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                    CategoryController().getCategoryAndIconRes(
                        categoriesUiState = categoriesUiState,
                        categoryNameAndIconMap = appViewModel.categoryNameAndIconMap,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        recordType = type
                    )
                },
                getAccount = { accountId: Int ->
                    AccountController().getAccountById(accountId, accountsUiState.accountList)
                },
                onRecordClick = { orderNum: Int ->
                    onNavigateToMakeRecordScreen(MakeRecordStatus.Edit, orderNum)
                },
                onTransferClick = { orderNum: Int ->
                    onNavigateToMakeTransferScreen(MakeRecordStatus.Edit, orderNum)
                }
            )
        }
        composable(
            route = "${AppScreen.MakeRecord.route}/" +
                    "{${MakeRecordScreenArgs.Status.name}}/" +
                    "{${MakeRecordScreenArgs.RecordNum.name}}",
            arguments = listOf(
                navArgument(MakeRecordScreenArgs.Status.name) { type = NavType.StringType },
                navArgument(MakeRecordScreenArgs.RecordNum.name) { type = NavType.IntType },
            ),
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val makeRecordStatus = it.arguments?.getString(MakeRecordScreenArgs.Status.name)
            val recordNum = it.arguments?.getInt(MakeRecordScreenArgs.RecordNum.name)

            val makeRecordUiStateAndUnitList =
                RecordController().convertRecordStackToMakeRecordStateAndUnitList(
                    makeRecordStatus = makeRecordStatus,
                    recordNum = recordNum,
                    accountList = accountsUiState.accountList,
                    activeAccount = accountsUiState.activeAccount,
                    recordStackList = recordStackList,
                    categoriesUiState = categoriesUiState
                )
            val lastCategoryPair = if (
                makeRecordUiStateAndUnitList.second == null && accountsUiState.activeAccount != null
            ) {
                appViewModel.getLastRecordCategory(
                    accountId = accountsUiState.activeAccount.id,
                    type = CategoryType.Expense
                )
            } else {
                null
            }
            val viewModel = viewModel<MakeRecordViewModel>(
                factory = MakeRecordViewModelFactory(
                    category = lastCategoryPair?.first,
                    subcategory = lastCategoryPair?.second,
                    makeRecordUiState = makeRecordUiStateAndUnitList.first,
                    makeRecordUnitList = makeRecordUiStateAndUnitList.second
                )
            )
            val coroutineScope = rememberCoroutineScope()

            if (makeRecordStatus != null) {
                MakeRecordScreen(
                    appTheme = appUiSettings.appTheme,
                    viewModel = viewModel,
                    makeRecordStatus = makeRecordStatus,
                    accountList = accountsUiState.accountList,
                    categoriesUiState = categoriesUiState,
                    categoryNameAndIconMap = appViewModel.categoryNameAndIconMap,
                    onMakeTransferButtonClick = {
                        onNavigateToMakeTransferScreen(MakeRecordStatus.Create, 0)
                    },
                    onSaveButton = { state, unitList ->
                        coroutineScope.launch {
                            appViewModel.saveRecord(state, unitList)
                        }
                        navController.popBackStack()
                    },
                    onRepeatButton = { state, unitList ->
                        coroutineScope.launch {
                            appViewModel.repeatRecord(state, unitList)
                        }
                        navController.popBackStack()
                    },
                    onDeleteButton = { recordNumToDelete ->
                        coroutineScope.launch {
                            appViewModel.deleteRecord(recordNumToDelete)
                        }
                        navController.popBackStack()
                    },
                )
            }
        }
        composable(
            route = "${AppScreen.MakeTransfer.route}/" +
                    "{${MakeRecordScreenArgs.Status.name}}/" +
                    "{${MakeRecordScreenArgs.RecordNum.name}}",
            arguments = listOf(
                navArgument(MakeRecordScreenArgs.Status.name) { type = NavType.StringType },
                navArgument(MakeRecordScreenArgs.RecordNum.name) { type = NavType.IntType },
            ),
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val makeRecordStatus = it.arguments?.getString(MakeRecordScreenArgs.Status.name)
            val recordNum = it.arguments?.getInt(MakeRecordScreenArgs.RecordNum.name)
            val makeTransferUiState = RecordController().convertRecordStackToMakeTransferState(
                makeRecordStatus = makeRecordStatus,
                recordNum = recordNum,
                accountList = accountsUiState.accountList,
                activeAccount = accountsUiState.activeAccount,
                recordStackList = recordStackList
            )
            val viewModel = viewModel<MakeTransferViewModel>(
                factory = MakeTransferViewModelFactory(
                    accountList = accountsUiState.accountList,
                    makeTransferUiState = makeTransferUiState
                )
            )
            val coroutineScope = rememberCoroutineScope()

            if (makeRecordStatus != null) {
                MakeTransferScreen(
                    appTheme = appUiSettings.appTheme,
                    viewModel = viewModel,
                    makeRecordStatus = makeRecordStatus,
                    accountList = accountsUiState.accountList,
                    onNavigateBack = navController::popBackStack,
                    onSaveButton = { state ->
                        coroutineScope.launch {
                            appViewModel.saveTransfer(state)
                        }
                        navController.popBackStack(AppScreen.Home.route, false)
                    },
                    onRepeatButton = { state ->
                        coroutineScope.launch {
                            appViewModel.repeatTransfer(state)
                        }
                        navController.popBackStack(AppScreen.Home.route, false)
                    },
                    onDeleteButton = { recordNumToDelete ->
                        coroutineScope.launch {
                            appViewModel.deleteTransfer(recordNumToDelete)
                        }
                        navController.popBackStack(AppScreen.Home.route, false)
                    }
                )
            }
        }
        setupGraph(
            navController = navController,
            startDestination = startSettingsDestination,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            themeUiState = themeUiState,
            accountList = accountsUiState.accountList
        )
        composable(
            route = AppScreen.FinishSetup.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val coroutineScope = rememberCoroutineScope()
            SetupFinishScreen(
                onFinishSetupButton = {
                    coroutineScope.launch {
                        appViewModel.finishSetup()
                    }
                }
            )
        }
    }
}


fun NavGraphBuilder.setupGraph(
    navController: NavHostController,
    startDestination: String,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
) {
    val onNavigateToScreen = { setupScreenRoute: String ->
        navController.navigate(setupScreenRoute)
    }

    navigation(
        startDestination = startDestination,
        route = AppScreen.Settings.route
    ) {
        composable(
            route = SettingsScreen.Start.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            SetupStartScreen(
                onManualSetupButton = {
                    onNavigateToScreen(SettingsScreen.Language.route)
                },
                onImportDataButton = {
                    onNavigateToScreen(SettingsScreen.Import.route)
                }
            )
        }
        composable(
            route = SettingsScreen.SettingsHome.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            SettingsHomeScreen(
                navController = navController,
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
            )
        }
        composable(
            route = SettingsScreen.Language.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val viewModel = viewModel<LanguageViewModel>()
            val chosenLanguage by viewModel.langCode.collectAsState()
            if (chosenLanguage == null) {
                viewModel.chooseNewLanguage(appUiSettings.langCode)
            }

            SetupLanguageScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appLanguage = appUiSettings.langCode,
                chosenLanguage = chosenLanguage,
                chooseNewLanguage = viewModel::chooseNewLanguage,
                onApplyButton = { langCode: String ->
                    appViewModel.setLanguage(langCode)
                },
                onNextNavigationButton = {
                    onNavigateToScreen(SettingsScreen.Appearance.route)
                }
            )
        }
        composable(
            route = SettingsScreen.Appearance.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            SetupAppearanceScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                onContinueButton = {
                    onNavigateToScreen(SettingsScreen.Accounts.route)
                },
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme,
                themeUiState = themeUiState
            )
        }
        composable(
            route = SettingsScreen.Accounts.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) { entry ->
            val viewModel = entry.sharedViewModel<SetupAccountsViewModel>(navController)
            val accountsSetupList by viewModel.accountsListState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(true) {
                if (accountsSetupList.isEmpty()) {
                    viewModel.applyAccountsList(accountList)
                }
            }

            SetupAccountsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                accountsList = accountsSetupList,
                appTheme = appUiSettings.appTheme,
                navigateToEditAccountScreen = { orderNum ->
                    navController.navigate("${SettingsScreen.EditAccount.route}/$orderNum")
                },
                onAddNewAccount = {
                    viewModel.addNewDefaultAccount()
                },
                swapAccounts = viewModel::swapAccounts,
                onResetButton = {
                    viewModel.resetAccountsList()
                },
                onSaveButton = { newAccountsList ->
                    coroutineScope.launch {
                        appViewModel.saveAccountsToDb(newAccountsList)
                    }
                    if (appUiSettings.isSetUp) {
                        navController.popBackStack()
                    } else {
                        onNavigateToScreen(SettingsScreen.Categories.route)
                    }
                }
            )
        }
        composable(
            route = "${SettingsScreen.EditAccount.route}/{${EditAccountScreenArgs.OrderNum.name}}",
            arguments = listOf(
                navArgument(EditAccountScreenArgs.OrderNum.name) { type = NavType.IntType }
            ),
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) { entry ->
            val coroutineScope = rememberCoroutineScope()
            val orderNum = entry.arguments?.getInt(EditAccountScreenArgs.OrderNum.name)

            val setupAccountsViewModel = entry.sharedViewModel<SetupAccountsViewModel>(navController)
            val showDeleteAccountButton by setupAccountsViewModel.showDeleteAccountButton.collectAsState()
            val account = orderNum?.let {
                setupAccountsViewModel.getAccountByOrderNum(orderNum)
            }

            val editAccountViewModel = entry.sharedViewModel<EditAccountViewModel>(
                navController = navController,
                factory = EditAccountViewModelFactory(
                    uiState = account?.let {
                        AccountController().accountToEditAccountUiState(it)
                    } ?: EditAccountUiState()
                )
            )
            val editAccountUiState by editAccountViewModel.uiState.collectAsStateWithLifecycle()
            account?.let {
                LaunchedEffect(it.id) {
                    if (editAccountUiState.id != account.id) {
                        editAccountViewModel.applyAccountData(it)
                    }
                }
            }

            account?.let {
                EditAccountScreen(
                    scaffoldPadding = scaffoldPadding,
                    uiState = editAccountUiState,
                    appTheme = appUiSettings.appTheme,
                    showDeleteAccountButton = showDeleteAccountButton,
                    onColorChange = editAccountViewModel::changeColor,
                    onNameChange = editAccountViewModel::changeName,
                    onNavigateToCurrencyPickerWindow = {
                        navController.navigate(
                            "${SettingsScreen.CurrencyPicker.route}/${editAccountUiState.currency}"
                        )
                    },
                    onBalanceChange = editAccountViewModel::changeBalance,
                    onHideChange = editAccountViewModel::changeHide,
                    onHideBalanceChange = editAccountViewModel::changeHideBalance,
                    onWithoutBalanceChange = editAccountViewModel::changeWithoutBalance,
                    onDeleteButton = { id ->
                        val updatedAccountList = setupAccountsViewModel.deleteAccountById(id)
                        updatedAccountList?.let {
                            coroutineScope.launch {
                                appViewModel.deleteAccountWithItsRecords(id, updatedAccountList)
                            }
                        }
                        navController.popBackStack()
                    },
                    onSaveButton = {
                        setupAccountsViewModel.saveAccountData(
                            editAccountViewModel.getAccountObject()
                        )
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            route = "${SettingsScreen.CurrencyPicker.route}/{${CurrencyPickerScreenArgs.Currency.name}}",
            arguments = listOf(
                navArgument(CurrencyPickerScreenArgs.Currency.name) { type = NavType.StringType }
            ),
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) { entry ->
            val currency = entry.arguments?.getString(CurrencyPickerScreenArgs.Currency.name)
            val editAccountViewModel = entry.sharedViewModel<EditAccountViewModel>(navController)
            val currencyPickerViewModel = viewModel<CurrencyPickerViewModel>(
                factory = CurrencyPickerViewModelFactory(
                    selectedCurrency = currency
                )
            )

            CurrencyPickerScreen(
                viewModel = currencyPickerViewModel,
                scaffoldPadding = scaffoldPadding,
                onSaveButtonClick = { selectedCurrency ->
                    editAccountViewModel.changeCurrency(selectedCurrency)
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = SettingsScreen.Categories.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val viewModel = it.sharedViewModel<SetupCategoriesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val newCategoryName = stringResource(R.string.new_category_name)
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            LaunchedEffect(true) {
                if (uiState.expenseParentCategoryList.isEmpty() || uiState.incomeParentCategoryList.isEmpty()) {
                    viewModel.applyCategoryList(appViewModel.categoriesUiState.value, context)
                } else {
                    viewModel.changeNavigatedFromSetupCategoriesScreen(true)
                }
            }

            SetupCategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                uiState = uiState,
                categoryNameAndIconMap = appViewModel.categoryNameAndIconMap,
                onShowCategoriesByType = viewModel::changeCategoryTypeToShow,
                onNavigateToEditSubcategoryListScreen = { orderNum: Int ->
                    navController.navigate("${SettingsScreen.EditSubcategoryList.route}/$orderNum")
                },
                onNavigateToEditCategoryScreen = { orderNum ->
                    viewModel.applyCategoryToEdit(orderNum)
                    onNavigateToScreen(SettingsScreen.EditCategory.route)
                },
                onSwapCategories = viewModel::swapParentCategories,
                onAddNewCategory = {
                    viewModel.addNewParentCategory(newCategoryName)
                },
                onResetButton = {
                    viewModel.reapplyCategoryLists(context)
                },
                onSaveAndFinishSetupButton = {
                    coroutineScope.launch {
                        appViewModel.saveCategoriesToDb(viewModel.getAllCategories())
                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            appViewModel.preFinishSetup()
                        }
                    }
                }
            )
        }
        composable(
            route = "${SettingsScreen.EditSubcategoryList.route}/{${EditSubcategoryListScreenArgs.ParentCategoryOrderNum.name}}",
            arguments = listOf(
                navArgument(EditSubcategoryListScreenArgs.ParentCategoryOrderNum.name) { type = NavType.IntType }
            ),
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val viewModel = it.sharedViewModel<SetupCategoriesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val newSubcategoryName = stringResource(R.string.new_subcategory_name)
            LaunchedEffect(true) {
                val parentCategoryOrderNum = it.arguments?.getInt(EditSubcategoryListScreenArgs.ParentCategoryOrderNum.name)
                parentCategoryOrderNum?.let {
                    if (uiState.navigatedFromSetupCategoriesScreen) {
                        viewModel.applySubcategoryList(parentCategoryOrderNum)
                    }
                }
            }

            EditSubcategoryListScreen(
                scaffoldPadding = scaffoldPadding,
                subcategoryList = uiState.subcategoryList,
                categoryNameAndIconMap = appViewModel.categoryNameAndIconMap,
                onSaveButton = {
                    viewModel.saveSubcategoryList()
                    navController.popBackStack()
                },
                onNavigateToEditCategoryScreen = { orderNum ->
                    viewModel.applyCategoryToEdit(orderNum)
                    onNavigateToScreen(SettingsScreen.EditCategory.route)
                },
                onSwapCategories = viewModel::swapSubcategories,
                onAddNewSubcategory = { viewModel.addNewSubcategory(newSubcategoryName) }
            )
        }
        composable(
            route = SettingsScreen.EditCategory.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val viewModel = it.sharedViewModel<SetupCategoriesViewModel>(navController)
            val editCategoryUiState by viewModel.editCategoryUiState.collectAsStateWithLifecycle()

            EditCategoryScreen(
                scaffoldPadding = scaffoldPadding,
                uiState = editCategoryUiState,
                categoryIconList = appViewModel.categoryNameAndIconMap.map { (name, res) ->
                    Pair(name, res)
                },
                onNameChange = viewModel::onCategoryNameChange,
                onIconClick = viewModel::onCategoryIconChange,
                onSaveButton = {
                    viewModel.saveEditedCategory()
                    navController.popBackStack()
                },
                onDeleteButton = {
                    viewModel.deleteCategory()
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = SettingsScreen.Import.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            SetupImportScreen(
                onNextNavigationButton = {
                    onNavigateToScreen(AppScreen.FinishSetup.route)
                }
            )
        }
        composable(
            route = SettingsScreen.Data.route,
            enterTransition = { CustomAnimation().screenEnterTransition(this) },
            popEnterTransition = { CustomAnimation().screenPopEnterTransition(this) },
            exitTransition = { CustomAnimation().screenExitTransition(this) },
            popExitTransition = { CustomAnimation().screenPopExitTransition(this) }
        ) {
            val coroutineScope = rememberCoroutineScope()

            SettingsDataScreen(
                onResetData = {
                    coroutineScope.launch {
                        appViewModel.resetAppData()
                    }
                },
                onExportData = {}
            )
        }
    }
}