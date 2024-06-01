package com.ataglance.walletglance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.data.SettingsRepository
import com.ataglance.walletglance.ui.viewmodels.AccountColorName
import com.ataglance.walletglance.ui.viewmodels.AccountController
import com.ataglance.walletglance.ui.viewmodels.AppLanguage
import com.ataglance.walletglance.ui.viewmodels.AppViewModel
import com.ataglance.walletglance.ui.viewmodels.CategoryColorName
import com.ataglance.walletglance.ui.viewmodels.CategoryController
import com.ataglance.walletglance.ui.viewmodels.DateRangeController
import com.ataglance.walletglance.ui.viewmodels.DateTimeState
import com.ataglance.walletglance.ui.viewmodels.MakeRecordStatus
import com.ataglance.walletglance.ui.viewmodels.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.MakeRecordUnitUiState
import com.ataglance.walletglance.ui.viewmodels.MakeRecordViewModel
import com.ataglance.walletglance.ui.viewmodels.MakeTransferUiState
import com.ataglance.walletglance.ui.viewmodels.MakeTransferViewModel
import com.ataglance.walletglance.ui.viewmodels.RecordType
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountCard
import com.ataglance.walletglance.ui.theme.widgets.RecordHistoryWidget
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class WalletGlanceInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockAccountRepository: AccountRepository
    private lateinit var mockCategoryRepository: CategoryRepository
    private lateinit var mockRecordRepository: RecordRepository
    private lateinit var mockRecordAndAccountRepository: RecordAndAccountRepository
    private lateinit var mockGeneralRepository: GeneralRepository

    private lateinit var mockSettingsRepository: SettingsRepository
    private lateinit var appViewModel: AppViewModel

    private val providedRecordList = mutableListOf<Record>()

    @Before
    fun setUp() {
        mockSettingsRepository = mockk()
        setupSettingsRepository()
        mockAccountRepository = mockk()
        setupAccountRepository()
        mockCategoryRepository = mockk()
        setupCategoryRepository()
        mockRecordRepository = mockk()
        setupRecordRepository()
        mockRecordAndAccountRepository = mockk()
        setupRecordAndAccountRepository()
        mockGeneralRepository = mockk()

        appViewModel = AppViewModel(
            accountRepository = mockAccountRepository,
            categoryRepository = mockCategoryRepository,
            recordRepository = mockRecordRepository,
            recordAndAccountRepository = mockRecordAndAccountRepository,
            generalRepository = mockGeneralRepository,
            settingsRepository = mockSettingsRepository
        )
        appViewModel.fetchDataOnStart()
    }

    @After
    fun tearDown() {
        providedRecordList.clear()
    }



    private fun setupSettingsRepository() {
        every { mockSettingsRepository.language } returns flowOf(AppLanguage.English.languageCode)
        every { mockSettingsRepository.setupStage } returns flowOf(1)
        every { mockSettingsRepository.useDeviceTheme } returns flowOf(false)
        every { mockSettingsRepository.chosenLightTheme } returns flowOf(AppTheme.LightDefault.name)
        every { mockSettingsRepository.chosenDarkTheme } returns flowOf(AppTheme.DarkDefault.name)
        every { mockSettingsRepository.lastChosenTheme } returns flowOf(AppTheme.LightDefault.name)
    }

    private fun setupAccountRepository() {
        every { mockAccountRepository.getAllAccounts() } returns flowOf (
            listOf(
                Account(
                    id = 1,
                    orderNum = 1,
                    name = "Account 1",
                    currency = "USD",
                    balance = 1000.0,
                    color = AccountColorName.Default.name,
                    hide = false,
                    hideBalance = false,
                    withoutBalance = false,
                    isActive = true
                ),
                Account(
                    id = 2,
                    orderNum = 2,
                    name = "Account 2",
                    currency = "USD",
                    balance = 500.0,
                    color = AccountColorName.Default.name,
                    hide = false,
                    hideBalance = false,
                    withoutBalance = false,
                    isActive = false
                )
            )
        )
    }

    private fun setupCategoryRepository() {
        every { mockCategoryRepository.getCategories() } returns flowOf(
            listOf(
                Category(
                    id = 1,
                    type = '-',
                    rank = 'c',
                    orderNum = 1,
                    parentCategoryId = null,
                    name = "Category 1",
                    iconName = "housing",
                    colorName = CategoryColorName.Red.name
                ),
                Category(
                    id = 2,
                    type = '+',
                    rank = 'c',
                    orderNum = 1,
                    parentCategoryId = null,
                    name = "Category 2",
                    iconName = "housing",
                    colorName = CategoryColorName.Red.name
                )
            )
        )
    }

    private fun setupRecordRepository() {

        every { mockRecordRepository.getAllRecords() } returns flowOf(providedRecordList)

        every {
            mockRecordRepository.getRecordsInDateRange(any())
        } returns flowOf(providedRecordList)

        every {
            mockRecordRepository.getLastRecordNum()
        } returns flowOf(
            providedRecordList.maxOfOrNull { it.recordNum }
        )

    }

    private fun setupRecordAndAccountRepository() {
        coEvery {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(any(), any())
        } returns Unit
    }

    @Test
    fun accountBalance_Positive_DisplayedWithRightFormat() {

        composeTestRule.setContent {
            appViewModel.accountsUiState.value.activeAccount?.let {
                AccountCard(account = it, appTheme = AppTheme.LightDefault, todayExpenses = 0.0)
            }
        }

        composeTestRule.onNodeWithText("1 000").assertIsDisplayed()

    }

    @Test
    fun makingRecordProcess_Positive_ShownByDateFilter() = runTest {

        val calendar = Calendar.getInstance()
        calendar.set(2024, 4, 1)
        val dateTimeState = DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )

        val accountsUiState = appViewModel.accountsUiState.value
        val categoriesUiState = appViewModel.categoriesUiState.value

        val uiState = MakeRecordUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            account = accountsUiState.activeAccount,
            type = RecordType.Expense,
            dateTimeState = dateTimeState
        )
        val unitList = listOf(
            MakeRecordUnitUiState(
                index = 0,
                category = categoriesUiState.parentCategories.expense[0],
                subcategory = null,
                amount = "516"
            )
        )
        val viewModel = MakeRecordViewModel(
            category = null,
            subcategory = null,
            makeRecordUiState = uiState,
            makeRecordUnitList = unitList
        )

        val expectedRecordList = listOf(
            Record(
                id = 0,
                recordNum = 1,
                date = dateTimeState.dateLong,
                type = '-',
                accountId = accountsUiState.activeAccount!!.id,
                amount = 516.0,
                quantity = null,
                categoryId = categoriesUiState.parentCategories.expense[0].id,
                subcategoryId = null,
                note = null
            )
        )
        val expectedAccountList = listOf(
            accountsUiState.accountList[0].let {
                it.copy(balance = it.balance - 516.0)
            },
            accountsUiState.accountList[1]
        )

        appViewModel.saveRecord(viewModel.uiState.value, viewModel.recordUnitList.value)

        coVerify {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                expectedRecordList, expectedAccountList
            )
        }

        expectedRecordList.forEach { record ->
            providedRecordList.add(record)
        }

        appViewModel.fetchRecordsFromDbInDateRange(
            appViewModel.dateRangeMenuUiState.value.dateRangeState
        )

        composeTestRule.setContent {
            RecordHistoryWidget(
                recordStackList = appViewModel.filterRecordStackForWidgetsUiState(
                    recordStackList = appViewModel.recordStackList.value,
                    dateRangeState = appViewModel.dateRangeMenuUiState.value.dateRangeState,
                    activeAccount = accountsUiState.activeAccount
                ),
                appTheme = appViewModel.appUiSettings.value.appTheme,
                getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                    CategoryController().getCategoryAndIconRes(
                        categoriesUiState = categoriesUiState,
                        categoryNameAndIconMap = appViewModel.categoryIconNameToIconResMap,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        recordType = type
                    )
                },
                getAccount = { accountId: Int ->
                    AccountController().getAccountById(accountId, accountsUiState.accountList)
                },
                onRecordClick = {},
                onTransferClick = {}
            )
        }

        composeTestRule.onNodeWithText("- 516.00 USD").assertIsDisplayed()

    }

    @Test
    fun makingRecordProcess_Negative_NotShownByDateFilter() = runTest {

        val calendar = Calendar.getInstance()
        calendar.set(2024, 3, 1)
        val dateTimeState = DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )

        val accountsUiState = appViewModel.accountsUiState.value
        val categoriesUiState = appViewModel.categoriesUiState.value

        val uiState = MakeRecordUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            account = accountsUiState.activeAccount,
            type = RecordType.Expense,
            dateTimeState = dateTimeState
        )
        val unitList = listOf(
            MakeRecordUnitUiState(
                index = 0,
                category = categoriesUiState.parentCategories.expense[0],
                subcategory = null,
                amount = "516"
            )
        )
        val viewModel = MakeRecordViewModel(
            category = null,
            subcategory = null,
            makeRecordUiState = uiState,
            makeRecordUnitList = unitList
        )

        val expectedRecordList = listOf(
            Record(
                id = 0,
                recordNum = 1,
                date = dateTimeState.dateLong,
                type = '-',
                accountId = accountsUiState.activeAccount!!.id,
                amount = 516.0,
                quantity = null,
                categoryId = categoriesUiState.parentCategories.expense[0].id,
                subcategoryId = null,
                note = null
            )
        )
        val expectedAccountList = listOf(
            accountsUiState.accountList[0].let {
                it.copy(balance = it.balance - 516.0)
            },
            accountsUiState.accountList[1]
        )

        appViewModel.saveRecord(viewModel.uiState.value, viewModel.recordUnitList.value)

        coVerify {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                expectedRecordList, expectedAccountList
            )
        }

        expectedRecordList.forEach { record ->
            providedRecordList.add(record)
        }

        appViewModel.fetchRecordsFromDbInDateRange(
            appViewModel.dateRangeMenuUiState.value.dateRangeState
        )

        composeTestRule.setContent {
            RecordHistoryWidget(
                recordStackList = appViewModel.filterRecordStackForWidgetsUiState(
                    recordStackList = appViewModel.recordStackList.value,
                    dateRangeState = appViewModel.dateRangeMenuUiState.value.dateRangeState,
                    activeAccount = accountsUiState.activeAccount
                ),
                appTheme = appViewModel.appUiSettings.value.appTheme,
                getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                    CategoryController().getCategoryAndIconRes(
                        categoriesUiState = categoriesUiState,
                        categoryNameAndIconMap = appViewModel.categoryIconNameToIconResMap,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        recordType = type
                    )
                },
                getAccount = { accountId: Int ->
                    AccountController().getAccountById(accountId, accountsUiState.accountList)
                },
                onRecordClick = {},
                onTransferClick = {}
            )
        }

        composeTestRule.onNodeWithText("- 516.00 USD").assertIsNotDisplayed()

    }

    @Test
    fun makingRecordProcess_Negative_NotPerformed() = runTest {

        val dateTimeState = DateTimeState()
        val categoriesUiState = appViewModel.categoriesUiState.value

        val uiState = MakeRecordUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            account = null,
            type = RecordType.Expense,
            dateTimeState = dateTimeState
        )
        val unitList = listOf(
            MakeRecordUnitUiState(
                index = 0,
                category = categoriesUiState.parentCategories.expense[0],
                subcategory = null,
                amount = "516"
            )
        )
        val viewModel = MakeRecordViewModel(
            category = null,
            subcategory = null,
            makeRecordUiState = uiState,
            makeRecordUnitList = unitList
        )

        appViewModel.saveRecord(viewModel.uiState.value, viewModel.recordUnitList.value)

        coVerify(exactly = 0) {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(any(), any())
        }

    }

    @Test
    fun makingTransferProcess_Positive_ShownByDateFilter() = runTest {

        val calendar = Calendar.getInstance()
        calendar.set(2024, 4, 1)
        val dateTimeState = DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )
        val accountsUiState = appViewModel.accountsUiState.value

        val uiState = MakeTransferUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            dateTimeState = dateTimeState,
            fromAccount = accountsUiState.accountList[0],
            toAccount = accountsUiState.accountList[1],
            startAmount = "100",
            finalAmount = "200",
            idFrom = 0,
            idTo = 0
        )
        val viewModel = MakeTransferViewModel(
            accountList = accountsUiState.accountList,
            makeTransferUiState = uiState
        )

        val expectedRecordList = listOf(
            Record(
                id = 0,
                recordNum = 1,
                date = dateTimeState.dateLong,
                type = '>',
                accountId = accountsUiState.accountList[0].id,
                amount = 100.0,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                note = accountsUiState.accountList[1].id.toString()
            ),
            Record(
                id = 0,
                recordNum = 2,
                date = dateTimeState.dateLong,
                type = '<',
                accountId = accountsUiState.accountList[1].id,
                amount = 200.0,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                note = accountsUiState.accountList[0].id.toString()
            )
        )
        val expectedAccountList = listOf(
            accountsUiState.accountList[0].let {
                it.copy(balance = it.balance - 100.0)
            },
            accountsUiState.accountList[1].let {
                it.copy(balance = it.balance + 200.0)
            }
        )

        appViewModel.saveTransfer(viewModel.uiState.value)

        coVerify {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                expectedRecordList, expectedAccountList
            )
        }

        expectedRecordList.forEach { record ->
            providedRecordList.add(record)
        }

        appViewModel.fetchRecordsFromDbInDateRange(
            appViewModel.dateRangeMenuUiState.value.dateRangeState
        )

        composeTestRule.setContent {
            RecordHistoryWidget(
                recordStackList = appViewModel.filterRecordStackForWidgetsUiState(
                    recordStackList = appViewModel.recordStackList.value,
                    dateRangeState = appViewModel.dateRangeMenuUiState.value.dateRangeState,
                    activeAccount = accountsUiState.activeAccount
                ),
                appTheme = appViewModel.appUiSettings.value.appTheme,
                getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                    CategoryController().getCategoryAndIconRes(
                        categoriesUiState = appViewModel.categoriesUiState.value,
                        categoryNameAndIconMap = appViewModel.categoryIconNameToIconResMap,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        recordType = type
                    )
                },
                getAccount = { accountId: Int ->
                    AccountController().getAccountById(accountId, accountsUiState.accountList)
                },
                onRecordClick = {},
                onTransferClick = {}
            )
        }

        composeTestRule.onNodeWithText("Transfer").assertIsDisplayed()
        composeTestRule.onNodeWithText("- 100.00 USD").assertIsDisplayed()
        composeTestRule.onNodeWithText("+ 200.00 USD").assertIsNotDisplayed()

    }

    @Test
    fun makingTransferProcess_Positive_NotShownByDateFilter() = runTest {

        val calendar = Calendar.getInstance()
        calendar.set(2024, 3, 1)
        val dateTimeState = DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )
        val accountsUiState = appViewModel.accountsUiState.value

        val uiState = MakeTransferUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            dateTimeState = dateTimeState,
            fromAccount = accountsUiState.accountList[0],
            toAccount = accountsUiState.accountList[1],
            startAmount = "100",
            finalAmount = "200",
            idFrom = 0,
            idTo = 0
        )
        val viewModel = MakeTransferViewModel(
            accountList = accountsUiState.accountList,
            makeTransferUiState = uiState
        )

        val expectedRecordList = listOf(
            Record(
                id = 0,
                recordNum = 1,
                date = dateTimeState.dateLong,
                type = '>',
                accountId = accountsUiState.accountList[0].id,
                amount = 100.0,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                note = accountsUiState.accountList[1].id.toString()
            ),
            Record(
                id = 0,
                recordNum = 2,
                date = dateTimeState.dateLong,
                type = '<',
                accountId = accountsUiState.accountList[1].id,
                amount = 200.0,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                note = accountsUiState.accountList[0].id.toString()
            )
        )
        val expectedAccountList = listOf(
            accountsUiState.accountList[0].let {
                it.copy(balance = it.balance - 100.0)
            },
            accountsUiState.accountList[1].let {
                it.copy(balance = it.balance + 200.0)
            }
        )

        appViewModel.saveTransfer(viewModel.uiState.value)

        coVerify {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                expectedRecordList, expectedAccountList
            )
        }

        expectedRecordList.forEach { record ->
            providedRecordList.add(record)
        }

        appViewModel.fetchRecordsFromDbInDateRange(
            appViewModel.dateRangeMenuUiState.value.dateRangeState
        )

        composeTestRule.setContent {
            RecordHistoryWidget(
                recordStackList = appViewModel.filterRecordStackForWidgetsUiState(
                    recordStackList = appViewModel.recordStackList.value,
                    dateRangeState = appViewModel.dateRangeMenuUiState.value.dateRangeState,
                    activeAccount = accountsUiState.activeAccount
                ),
                appTheme = appViewModel.appUiSettings.value.appTheme,
                getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                    CategoryController().getCategoryAndIconRes(
                        categoriesUiState = appViewModel.categoriesUiState.value,
                        categoryNameAndIconMap = appViewModel.categoryIconNameToIconResMap,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        recordType = type
                    )
                },
                getAccount = { accountId: Int ->
                    AccountController().getAccountById(accountId, accountsUiState.accountList)
                },
                onRecordClick = {},
                onTransferClick = {}
            )
        }

        composeTestRule.onNodeWithText("Transfer").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("- 100.00 USD").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("+ 200.00 USD").assertIsNotDisplayed()

    }

    @Test
    fun makingTransferProcess_Negative_NotPerformed() = runTest {

        val dateTimeState = DateTimeState()
        val accountsUiState = appViewModel.accountsUiState.value

        val uiState = MakeTransferUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            dateTimeState = dateTimeState,
            fromAccount = null,
            toAccount = null,
            startAmount = "100",
            finalAmount = "200",
            idFrom = 0,
            idTo = 0
        )
        val viewModel = MakeTransferViewModel(
            accountList = accountsUiState.accountList,
            makeTransferUiState = uiState
        )

        appViewModel.saveTransfer(viewModel.uiState.value)

        coVerify(exactly = 0) {
            mockRecordAndAccountRepository.upsertRecordsAndUpdateAccounts(any(), any())
        }

    }

}