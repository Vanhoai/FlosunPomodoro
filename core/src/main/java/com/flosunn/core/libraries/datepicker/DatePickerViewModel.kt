package com.flosunn.core.libraries.datepicker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class DatePickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DatePickerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // update selected month and year index based on current date
        val currentDate = uiState.value.date
        val currentMonth = currentDate.month
        val currentYear = currentDate.year

        // month number starts from 1, so we subtract 1 to get the correct index
        var selectedMonthIndex = uiState.value.months.indexOf(currentMonth)
        var selectedYearIndex = uiState.value.years.indexOf(currentYear)

        _uiState.update {
            it.copy(
                selectedMonthIndex = selectedMonthIndex,
                selectedYearIndex = selectedYearIndex,
            )
        }
    }

    fun getMonthYear(): String = uiState.value.date.month.name + " " + uiState.value.date.year

    fun formatUpperFirstLetter(str: String): String {
        return str.substring(0, 1).uppercase() + str.substring(1).lowercase()
    }

    fun months(): List<String> = uiState.value.months.map { formatUpperFirstLetter(it.name) }

    fun years(): List<String> = uiState.value.years.map { it.toString() }

    fun daysInGrid(): List<LocalDate?> {
        val firstDay = LocalDate(
            year = uiState.value.date.year,
            month = uiState.value.date.month,
            day = 1,
        )

        val lastDay = firstDay.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
        val leadingBlanks = firstDay.dayOfWeek.ordinal

        return buildList {
            repeat(leadingBlanks) { add(null) }
            var d = firstDay
            while (d <= lastDay) {
                add(d)
                d = d.plus(1, DateTimeUnit.DAY)
            }
        }
    }

    fun toggleMonthYearView() {
        _uiState.update {
            it.copy(isMonthYearViewVisible = !it.isMonthYearViewVisible)
        }
    }

    fun updateState(date: LocalDate) {
        _uiState.update {
            it.copy(
                date = date,
                selectedDayOfMonth = date.day,
                selectedMonthIndex = uiState.value.months.indexOf(date.month),
                selectedYearIndex = uiState.value.years.indexOf(date.year),
            )
        }
    }

    fun updateSelectedMonthIndex(index: Int) {
        val newDate = LocalDate(
            year = uiState.value.date.year,
            month = uiState.value.months[index],
            day = uiState.value.date.day,
        )

        _uiState.update {
            it.copy(
                selectedMonthIndex = index,
                date = newDate,
                selectedDayOfMonth = null,
            )
        }
    }

    fun updateSelectedYearIndex(index: Int) {
        val newDate = LocalDate(
            year = uiState.value.years[index],
            month = uiState.value.date.month,
            day = uiState.value.date.day,
        )

        _uiState.update {
            it.copy(
                selectedYearIndex = index,
                date = newDate,
                selectedDayOfMonth = null,
            )
        }
    }

    fun updateSelectedDayOfMonth(day: Int) {
        val newDate = LocalDate(
            year = uiState.value.date.year,
            month = uiState.value.date.month,
            day = day,
        )

        _uiState.update {
            it.copy(
                selectedDayOfMonth = day,
                date = newDate,
            )
        }
    }

    fun nextMonth() {
        val currentDate = uiState.value.date
        val newDate = currentDate.plus(1, DateTimeUnit.MONTH)

        _uiState.update {
            it.copy(
                date = newDate,
                selectedDayOfMonth = null,
                selectedMonthIndex = uiState.value.months.indexOf(newDate.month),
                selectedYearIndex = uiState.value.years.indexOf(newDate.year),
            )
        }
    }

    fun previousMonth() {
        val currentDate = uiState.value.date
        val newDate = currentDate.minus(1, DateTimeUnit.MONTH)

        _uiState.update {
            it.copy(
                date = newDate,
                selectedDayOfMonth = null,
                selectedMonthIndex = uiState.value.months.indexOf(newDate.month),
                selectedYearIndex = uiState.value.years.indexOf(newDate.year),
            )
        }
    }
}