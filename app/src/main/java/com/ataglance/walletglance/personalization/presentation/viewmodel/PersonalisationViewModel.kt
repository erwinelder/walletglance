package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.utils.moveItems
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCase
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveWidgetsUseCase
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalisationViewModel(
    private val getAppThemeConfigurationUseCase: GetAppThemeConfigurationUseCase,
    private val changeAppLookPreferencesUseCase: ChangeAppLookPreferencesUseCase,
    private val saveWidgetsUseCase: SaveWidgetsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase,
    private val saveNavigationButtonsUseCase: SaveNavigationButtonsUseCase,
    private val getNavigationButtonsUseCase: GetNavigationButtonsUseCase
) : ViewModel() {

    init {
        fetchAppThemeConfiguration()
        fetchWidgets()
        fetchNavButtons()
    }


    private val _showThemeSettings = MutableStateFlow(false)
    val showThemeSettings = _showThemeSettings.asStateFlow()

    fun showThemeSettings() = _showThemeSettings.update { true }
    fun hideThemeSettings() = _showThemeSettings.update { false }


    private val _appThemeConfiguration = MutableStateFlow(AppThemeConfiguration())
    val appThemeConfiguration = _appThemeConfiguration.asStateFlow()

    private fun fetchAppThemeConfiguration() {
        viewModelScope.launch {
            getAppThemeConfigurationUseCase.getAsFlow().let {
                _appThemeConfiguration.update { it }
            }
        }
    }

    fun setLightTheme(theme: AppTheme) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveLightThemePreference(theme = theme)
        }
    }

    fun setDarkTheme(theme: AppTheme) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveDarkThemePreference(theme = theme)
        }
    }

    fun setUseDeviceTheme(value: Boolean) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveUseDeviceThemePreference(value = value)
        }
    }


    private val _showWidgetsSettings = MutableStateFlow(false)
    val showWidgetsSettings = _showWidgetsSettings.asStateFlow()

    fun showWidgetsSettings() = _showWidgetsSettings.update { true }
    private fun hideWidgetsSettings() = _showWidgetsSettings.update { false }


    private val _widgets = MutableStateFlow<List<CheckedWidget>>(emptyList())
    val widgets = _widgets.asStateFlow()

    private fun fetchWidgets() {
        viewModelScope.launch {
            getWidgetsUseCase.getAsFlow().collect { widgets ->
                _widgets.update {
                    widgets.map { widgetName ->
                        CheckedWidget(widgetName, widgetName in widgets)
                    }
                }
            }
        }
    }

    fun changeWidgetCheckState(widgetName: WidgetName, isChecked: Boolean) {
        _widgets.update { widgetList ->
            widgetList.map { widget ->
                widget.takeIf { it.name != widgetName } ?: widget.copy(isChecked = isChecked)
            }
        }
    }

    fun moveWidgets(fromIndex: Int, toIndex: Int) {
        _widgets.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    private suspend fun saveWidgets() {
        val widgets = widgets.value.filter { it.isChecked }.map { it.name }
        saveWidgetsUseCase.execute(widgets = widgets)
    }

    fun saveWidgetsAndCloseSettings() {
        viewModelScope.launch {
            saveWidgets()
            hideWidgetsSettings()
        }
    }


    private val _showNavButtonsSettings = MutableStateFlow(false)
    val showNavButtonsSettings = _showNavButtonsSettings.asStateFlow()

    fun showNavButtonsSettings() = _showNavButtonsSettings.update { true }
    private fun hideNavButtonsSettings() = _showNavButtonsSettings.update { false }


    private val _navButtons = MutableStateFlow<List<BottomBarNavigationButton>>(emptyList())
    val navButtons = _navButtons.asStateFlow()

    private fun fetchNavButtons() {
        viewModelScope.launch {
            getNavigationButtonsUseCase.getAsFlow().collect {
                _navButtons.update { it.subList(1, it.lastIndex) }
            }
        }
    }

    fun swapNavButtons(fromIndex: Int, toIndex: Int) {
        _navButtons.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    private suspend fun saveNavButtons() {
        saveNavigationButtonsUseCase.execute(buttons = navButtons.value)
    }

    fun saveNavButtonsAndCloseSettings() {
        viewModelScope.launch {
            saveNavButtons()
            hideNavButtonsSettings()
        }
    }

}