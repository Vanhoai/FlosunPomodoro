package com.flosun.pomodoro.presentation.task.add_task

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.core.utils.StringArray
import com.flosun.pomodoro.presentation.task.add_task.components.RepetitionOption
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.time.debounce
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Clock


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
    val globalLoading: GlobalLoading,
) : BaseViewModel(application) {

    // Date in task will be determined by 00:00 of the day, so we can easily query tasks by date without worrying about time
    private val nowMilliseconds = Clock.System.todayIn(TimeZone.currentSystemDefault())
        .atTime(0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    val weeks = uiState
        .flatMapLatest {
            val yearId = it.selectedYear?.id ?: return@flatMapLatest MutableStateFlow(emptyList())
            database.findWeeksByYearId(yearId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val goals = uiState
        .flatMapLatest {
            val yearId = it.selectedWeek?.id ?: return@flatMapLatest MutableStateFlow(emptyList())
            database.findGoalsByWeekId(yearId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val laggingIndicators = uiState
        .flatMapLatest {
            val yearId = it.selectedYear?.id ?: return@flatMapLatest MutableStateFlow(emptyList())
            database.findLaggingIndicatorsByYearId(yearId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        initialize()
        listenDebouncedTaskName()
    }

    fun initialize() = ioRun {
        // Set default year and week based on current date
        val currentTime = System.currentTimeMillis()
        val year = database.findTwelveWeekYearByDate(currentTime).first() ?: return@ioRun
        val week = database.findCurrentWeekByYearId(year.id, currentTime).first() ?: return@ioRun

        _uiState.update { it.copy(selectedYear = year, selectedWeek = week) }
    }

    fun listenDebouncedTaskName() = ioRun {
        uiState
            .map { it.taskName }
            .debounce(600L)
            .distinctUntilChanged()
            .collectLatest { taskName ->
                _uiState.update { it.copy(isCheckingTaskName = true) }

                val task = database.findTasksByDateAndName(
                    date = nowMilliseconds,
                    name = taskName.trim(),
                ).first()

                delay(1500L)
                val isValidTaskName = uiState.value.taskName.trim().isNotBlank() && task == null
                _uiState.update {
                    it.copy(
                        isValidTaskName = isValidTaskName,
                        isCheckingTaskName = false
                    )
                }
            }
    }

    fun onChangedSelectedIcon(icon: Int) {
        _uiState.update { it.copy(selectedIcon = icon) }
    }

    fun onChangedTaskName(name: String) {
        _uiState.update { it.copy(taskName = name) }
    }

    fun onChangedSelectedEstimatedPomodoro(estimatedPomodoro: Int?) {
        _uiState.update { it.copy(selectedEstimatedPomodoro = estimatedPomodoro) }
    }

    fun onChangedSelectedPomodoroDuration(pomodoroDuration: Int?) {
        _uiState.update { it.copy(selectedPomodoroDuration = pomodoroDuration) }
    }

    fun onChangedTags(tag: String, isSelected: Boolean) {
        _uiState.update {
            val newSelectedTags = if (isSelected) {
                it.selectedTags + tag
            } else {
                it.selectedTags - tag
            }

            it.copy(selectedTags = newSelectedTags)
        }
    }

    fun onChangedSelectedYear(year: TwelveWeekYearEntity?) {
        _uiState.update { it.copy(selectedYear = year) }
    }

    fun onChangedSelectedWeek(week: WeekEntity?) {
        _uiState.update { it.copy(selectedWeek = week) }
    }

    fun onChangedSelectedGoal(goal: GoalEntity?) {
        _uiState.update { it.copy(selectedGoal = goal) }
    }

    fun onChangedSelectedLaggingIndicator(laggingIndicatorEntity: LaggingIndicatorEntity?) {
        _uiState.update { it.copy(selectedLaggingIndicator = laggingIndicatorEntity) }
    }

    fun onChangedSelectedColor(color: Color) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun onChangedSelectedRepetitionOption(repetitionOption: RepetitionOption?) {
        _uiState.update { it.copy(selectedRepetitionOption = repetitionOption) }
    }

    fun onChangedSelectedDays(day: DayOfWeek, isSelected: Boolean) {
        _uiState.update {
            val newSelectedDays = if (isSelected) {
                it.selectedDays + day
            } else {
                it.selectedDays - day
            }

            it.copy(selectedDays = newSelectedDays)
        }
    }

    fun addTask(onSuccess: () -> Unit = {}) = ioRun {
        // Validate Inputs
        if (!validateInputs()) return@ioRun

        // Create TaskEntity from uiState
        val newTask = TaskEntity(
            goalId = uiState.value.selectedGoal!!.id,
            laggingIndicatorId = uiState.value.selectedLaggingIndicator!!.id,
            name = uiState.value.taskName.trim(),
            icon = uiState.value.selectedIcon!!,
            tags = uiState.value.selectedTags,
            numPomodoro = uiState.value.selectedEstimatedPomodoro!!,
            pomodoroDuration = uiState.value.selectedPomodoroDuration!!,
            date = nowMilliseconds,
        )

        // Insert into database
        val insertedResult = database.addNewTask(newTask)
        if (insertedResult <= 0) {
            showToast("Failed to add task. Please try again.")
            return@ioRun
        }

        showToast("Task added successfully")
        delay(1000L)
        onSuccess()
    }

    fun resetState() = ioRun {
        val currentTime = System.currentTimeMillis()
        val year = database.findTwelveWeekYearByDate(currentTime).first() ?: return@ioRun
        val week = database.findCurrentWeekByYearId(year.id, currentTime).first() ?: return@ioRun

        _uiState.value = AddTaskUiState(
            selectedYear = year,
            selectedWeek = week,
        )
    }

    // ======================================= PRIVATE FUNCTIONS =======================================
    private suspend fun validateInputs(): Boolean {
        if (uiState.value.selectedIcon == null) {
            showToast("Please select an icon for the task")
            return false
        }

        if (uiState.value.selectedColor == null) {
            showToast("Please select a color for the task")
            return false
        }

        if (uiState.value.taskName.trim().isBlank()) {
            showToast("Please enter a name for the task")
            return false
        }

        if (!uiState.value.isValidTaskName) {
            showToast("A task with the same name already exists for today. Please choose a different name.")
            return false
        }

        if (uiState.value.selectedEstimatedPomodoro == null || uiState.value.selectedEstimatedPomodoro!! <= 0) {
            showToast("Please choose a valid estimated pomodoro count")
            return false
        }

        if (uiState.value.selectedPomodoroDuration == null || uiState.value.selectedPomodoroDuration!! <= 0) {
            showToast("Please choose a valid pomodoro duration")
            return false
        }

        if (uiState.value.selectedTags.isEmpty()) {
            showToast("Please select at least one tag for the task")
            return false
        }

        if (uiState.value.selectedYear == null) {
            showToast("Please select a year for the task")
            return false
        }

        if (uiState.value.selectedWeek == null) {
            showToast("Please select a week for the task")
            return false
        }

        if (uiState.value.selectedGoal == null) {
            showToast("Please select a goal for the task")
            return false
        }

        if (uiState.value.selectedLaggingIndicator == null) {
            showToast("Please select a lagging indicator for the task")
            return false
        }

        if (uiState.value.selectedRepetitionOption == null) {
            showToast("Please select at least one day for the repetition")
            return false
        }

        // Check if repetition option is custom => at least one day should be selected
        if (uiState.value.selectedRepetitionOption == RepetitionOption.CUSTOM && uiState.value.selectedDays.isEmpty()) {
            showToast("Please select at least one day for the custom repetition")
            return false
        }

        return true
    }
}