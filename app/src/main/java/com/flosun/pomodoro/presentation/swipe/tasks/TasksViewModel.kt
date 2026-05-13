package com.flosun.pomodoro.presentation.swipe.tasks

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

data class TasksUiState(
    val selectedOptionIndex: Int = 0,
    val options: List<String> = listOf("Active", "Completed"),
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksViewModel @Inject constructor(
    application: Application,
    private val database: PomodoroDatabase,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val nowMilliseconds = Clock.System.todayIn(TimeZone.currentSystemDefault())
        .atTime(0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    val tasks = uiState
        .map { it.selectedOptionIndex }
        .flatMapLatest { selectedOptionIndex ->
            val isCompleted = selectedOptionIndex == 1
            database.findTasksByDate(nowMilliseconds, isCompleted)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun onChangedOptionIndex(optionIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedOptionIndex = optionIndex)
    }
}