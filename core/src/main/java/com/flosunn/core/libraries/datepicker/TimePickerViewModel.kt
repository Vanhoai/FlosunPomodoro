package com.flosunn.core.libraries.datepicker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class Designator {
    AM,
    PM,
}

data class TimePickerUiState(
    val hours: List<String> = emptyList(),
    val minutes: List<String> = emptyList(),
    val designators: List<Designator> = Designator.entries,

    val selectedHourIndex: Int = 0,
    val selectedMinuteIndex: Int = 0,
    val selectedDesignatorIndex: Int = 0,
)

@HiltViewModel
class TimePickerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TimePickerUiState())
    val uiState: StateFlow<TimePickerUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = TimePickerUiState(
            hours = (1..12).map { it.toString() },
            minutes = (0..59).map { it.toString().padStart(2, '0') },
        )
    }

    fun onChangedHourIndex(index: Int) {
        _uiState.update { it.copy(selectedHourIndex = index) }
    }

    fun onChangedMinuteIndex(index: Int) {
        _uiState.update { it.copy(selectedMinuteIndex = index) }
    }

    fun onChangedDesignatorIndex(index: Int) {
        _uiState.update { it.copy(selectedDesignatorIndex = index) }
    }

}