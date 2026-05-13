package com.flosun.pomodoro.presentation.goals.add_goal

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.graphics.toColorInt
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class AddGoalUiState(
    val selectedIcon: Int? = null,
    val selectedColor: Color? = null,
    val goalName: String = "",
    val selectedYear: TwelveWeekYearEntity? = null,
    val selectedWeek: WeekEntity? = null,
    val selectedLaggingIndicator: LaggingIndicatorEntity? = null,
)

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(AddGoalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialize()
    }

    fun initialize() = ioRun {
        val currentTime = System.currentTimeMillis()
        val currentYearId = appStorage.read(CURRENT_YEAR_ID_KEY, "")
        if (currentYearId.isBlank()) return@ioRun

        val year = database.findTwelveWeekYearById(currentYearId).first() ?: return@ioRun
        if (currentTime < year.startTimeMilliseconds || currentTime > year.endTimeMilliseconds) return@ioRun

        val week = database.findCurrentWeekByYearId(year.id, currentTime).first() ?: return@ioRun

        // Update default selected year and week
        _uiState.update { state ->
            state.copy(
                selectedYear = year,
                selectedWeek = week,
            )
        }
    }

    fun onChangedSelectedIcon(icon: Int) {
        _uiState.value = _uiState.value.copy(selectedIcon = icon)
    }

    fun onChangedSelectedColor(color: Color) {
        _uiState.value = _uiState.value.copy(selectedColor = color)
    }

    fun onChangedGoalName(name: String) {
        _uiState.value = _uiState.value.copy(goalName = name)
    }

    fun onChangedSelectedYear(year: TwelveWeekYearEntity) {
        _uiState.value = _uiState.value.copy(selectedYear = year)

        // Remove default value if selected year is changed
        if (uiState.value.selectedWeek?.yearId != year.id) {
            _uiState.value = _uiState.value.copy(selectedWeek = null)
        }
    }

    fun onChangedSelectedWeek(week: WeekEntity) {
        _uiState.value = _uiState.value.copy(selectedWeek = week)
    }

    fun onChangedSelectedLaggingIndicator(laggingIndicator: LaggingIndicatorEntity) {
        _uiState.value = _uiState.value.copy(selectedLaggingIndicator = laggingIndicator)
    }

    fun addNewGoal(onSuccess: () -> Unit = {}) = viewModelScope.launch(Dispatchers.IO) {
        if (!validateInputs()) return@launch

        val newGoal = GoalEntity(
            weekId = uiState.value.selectedWeek!!.id,
            laggingIndicatorId = uiState.value.selectedLaggingIndicator!!.id,
            name = uiState.value.goalName,
            icon = uiState.value.selectedIcon!!,
            color = uiState.value.selectedColor!!.value.toHexString(),
        )

        val insertedResult = database.addNewGoal(newGoal)
        if (insertedResult > 0) {
            launch(Dispatchers.Main) {
                Toast.makeText(
                    application,
                    "New goal added successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onSuccess()
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application,
                    "Failed to add new goal. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // ================================= PRIVATE FUNCTIONS =================================
    private fun validateInputs(): Boolean {
        if (uiState.value.selectedIcon == null) {
            Toast.makeText(
                application,
                "Please select an icon for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        if (uiState.value.selectedColor == null) {
            Toast.makeText(
                application,
                "Please select a color for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        if (uiState.value.goalName.isBlank()) {
            Toast.makeText(
                application,
                "Please enter a name for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        if (uiState.value.selectedYear == null) {
            Toast.makeText(
                application,
                "Please select a year for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        if (uiState.value.selectedWeek == null) {
            Toast.makeText(
                application,
                "Please select a week for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        if (uiState.value.selectedLaggingIndicator == null) {
            Toast.makeText(
                application,
                "Please select a reference lagging indicator for your goal.",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }

        return true
    }
}