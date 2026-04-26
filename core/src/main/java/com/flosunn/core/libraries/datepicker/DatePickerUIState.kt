package com.flosunn.core.libraries.datepicker

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class DatePickerUIState(
    val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),

    val months: List<Month> = listOf(
        Month.JANUARY,
        Month.FEBRUARY,
        Month.MARCH,
        Month.APRIL,
        Month.MAY,
        Month.JUNE,
        Month.JULY,
        Month.AUGUST,
        Month.SEPTEMBER,
        Month.OCTOBER,
        Month.NOVEMBER,
        Month.DECEMBER,
    ),
    val years: List<Int> = (2000..2030).toList(),

    val isMonthYearViewVisible: Boolean = false,
    val selectedMonthIndex: Int = 0,
    val selectedYearIndex: Int = 0,
    val selectedDayOfMonth: Int? = null,
)
