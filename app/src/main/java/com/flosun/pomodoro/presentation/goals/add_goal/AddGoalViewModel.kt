package com.flosun.pomodoro.presentation.goals.add_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddGoalUiState(
    val years: List<TwelveWeekYearEntity> = emptyList(),
    val weeks: List<WeekEntity> = emptyList(),

    // States
    val selectedIcon: Int? = null,
    val selectedColor: Color? = null,
    val goalName: String = "",
    val selectedYear: TwelveWeekYearEntity? = null,
    val selectedWeek: WeekEntity? = null,
    val selectedLaggingIndicator: LaggingIndicatorEntity? = null,
)

@HiltViewModel
class AddGoalViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddGoalUiState())
    val uiState = _uiState.asStateFlow()
    
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
    }

    fun onChangedSelectedWeek(week: WeekEntity) {
        _uiState.value = _uiState.value.copy(selectedWeek = week)
    }

    fun onChangedSelectedLaggingIndicator(laggingIndicator: LaggingIndicatorEntity) {
        _uiState.value = _uiState.value.copy(selectedLaggingIndicator = laggingIndicator)
    }

}