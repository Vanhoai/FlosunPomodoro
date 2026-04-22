package com.flosunn.pomodoro.ui.components.core.datepicker

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.flosunn.pomodoro.core.constants.DatePickerCommon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.streams.toList

enum class Days(val abbreviation: String, val value: String, val number: Int) {
    MONDAY("MON", "Monday", 1),
    TUESDAY("TUE", "Tuesday", 2),
    WEDNESDAY("WED", "Wednesday", 3),
    THURSDAY("THU", "Thursday", 4),
    FRIDAY("FRI", "Friday", 5),
    SATURDAY("SAT", "Saturday", 6),
    SUNDAY("SUN", "Sunday", 7);

    companion object {
        fun fromNum(number: Int): Days {
            return when (number) {
                1 -> MONDAY
                2 -> TUESDAY
                3 -> WEDNESDAY
                4 -> THURSDAY
                5 -> FRIDAY
                6 -> SATURDAY
                7 -> SUNDAY
                else -> throw IllegalArgumentException("Invalid day number: $number")
            }
        }
    }
}

data class DatePickerUIState(
    val selectedYear: Int = Calendar.getInstance()[Calendar.YEAR],
    val selectedMonth: Int = Calendar.getInstance()[Calendar.MONTH],
    val selectedDay: Int = Calendar.getInstance()[Calendar.DAY_OF_MONTH],

    val months: List<String> = DatePickerCommon.getMonths(),
    val years: List<String> = DatePickerCommon.years.map { "$it" },

    val isMonthYearViewVisible: Boolean = false,
    val selectedMonthIndex: Int = 0,
    val selectedYearIndex: Int = 0,
)

@HiltViewModel
class DatePickerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DatePickerUIState())
    val uiState = _uiState.asStateFlow()
    private var availableMonths = DatePickerCommon.getMonths()

    init {
        val currentMonth = Calendar.getInstance()[Calendar.MONTH]
        val currentYear = Calendar.getInstance()[Calendar.YEAR]

        _uiState.update {
            it.copy(
                selectedMonthIndex = availableMonths.indexOf(DatePickerCommon.getMonths()[currentMonth]),
                selectedYearIndex = DatePickerCommon.years.indexOf(currentYear)
            )
        }
    }

    fun toggleMonthYearView() {
        _uiState.update {
            it.copy(isMonthYearViewVisible = !it.isMonthYearViewVisible)
        }
    }

    fun updateSelectedMonthIndex(index: Int) {
        _uiState.update {
            it.copy(
                selectedMonthIndex = index,
            )
        }
    }

    fun updateSelectedYearIndex(index: Int) {
        _uiState.update {
            it.copy(selectedYearIndex = index)
        }
    }
}