package com.flosun.pomodoro.presentation.task.add_task

import androidx.compose.ui.graphics.Color
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.presentation.task.add_task.components.RepetitionOption
import kotlinx.datetime.DayOfWeek

data class AddTaskUiState(
    val selectedIcon: Int? = null,
    val selectedColor: Color? = null,
    val taskName: String = "",
    val isValidTaskName: Boolean = false,
    val isCheckingTaskName: Boolean = false,
    val selectedEstimatedPomodoro: Int? = null,
    val selectedPomodoroDuration: Int? = 25,
    val selectedTags: List<String> = emptyList(),
    val selectedYear: TwelveWeekYearEntity? = null,
    val selectedWeek: WeekEntity? = null,
    val selectedGoal: GoalEntity? = null,
    val selectedLaggingIndicator: LaggingIndicatorEntity? = null,
    val selectedRepetitionOption: RepetitionOption? = null,
    val selectedDays: List<DayOfWeek> = emptyList(),
)