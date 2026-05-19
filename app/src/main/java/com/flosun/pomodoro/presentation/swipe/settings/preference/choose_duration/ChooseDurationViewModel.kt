package com.flosun.pomodoro.presentation.swipe.settings.preference.choose_duration

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.DurationType
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosunn.core.libraries.datastore.datastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject


data class ChooseDurationUiState(
    val isExpanded: Boolean = false,
    val durationType: DurationType = DurationType.DAILY_TARGET_DURATION,
    val options: List<Int> = emptyList(),
    val selectedOption: Int = 0,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChooseDurationViewModel @Inject constructor(
    application: Application,
    private val database: PomodoroDatabase,
    private val appStorage: AppStorage,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(ChooseDurationUiState())
    val uiState = _uiState.asStateFlow()

    private val _setting = MutableStateFlow<SettingEntity?>(null)
    val setting = _setting.asStateFlow()

    init {
        viewModelScope.launch { listenSettingChange() }
    }

    suspend fun listenSettingChange() {
        application
            .datastore
            .data
            .map { it[CURRENT_ACCOUNT_ID_KEY] ?: "" }
            .distinctUntilChanged()
            .flatMapLatest { database.findSettingByAccountId(it) }
            .collect { _setting.value = it }
    }

    fun initialize(durationType: DurationType, currentSetting: SettingEntity) = ioRun {
        val options = resolveOptions(durationType)

        _uiState.update {
            it.copy(
                options = options,
                selectedOption = when (durationType) {
                    DurationType.DAILY_TARGET_DURATION -> currentSetting.dailyTargetMs
                    DurationType.POMODORO_DURATION -> currentSetting.pomodoroDuration
                    DurationType.SHORT_BREAK_DURATION -> currentSetting.shortBreakDuration
                    DurationType.LONG_BREAK_DURATION -> currentSetting.longBreakDuration
                    DurationType.LONG_BREAK_AFTER -> currentSetting.longBreakAfter
                    DurationType.AUTO_RESET_POMODORO -> currentSetting.autoResetPomodoro
                },
                durationType = durationType
            )
        }
    }

    fun onChangedOption(selectedOption: Int) {
        _uiState.update { it.copy(selectedOption = selectedOption) }
    }

    fun onExpandedCustomDuration() {
        _uiState.update { it.copy(isExpanded = true) }
    }

    fun onDismissCustomDuration() {
        _uiState.update { it.copy(isExpanded = false) }
    }

    fun onAddCustomDuration(customDuration: Int) = ioRun {
        val currentOptions = _uiState.value.options.toMutableList()
        if (!currentOptions.contains(customDuration)) {
            currentOptions.add(customDuration)
            appStorage.writeSerializable(
                key = _uiState.value.durationType.preferenceKey,
                value = currentOptions,
            )

            _uiState.update {
                it.copy(
                    options = currentOptions,
                    selectedOption = customDuration,
                )
            }
        } else {
            showToast("Option already exists")
        }
    }

    // region ======================================== PRIVATE FUNCTIONS ========================================
    private suspend fun resolveOptions(durationType: DurationType): List<Int> {
        val defaultOptions = durationType.defaultOptions
        val options = appStorage.readSerializable(durationType.preferenceKey, defaultOptions)
        return options
    }

    // endregion ======================================== PRIVATE FUNCTIONS ========================================
}