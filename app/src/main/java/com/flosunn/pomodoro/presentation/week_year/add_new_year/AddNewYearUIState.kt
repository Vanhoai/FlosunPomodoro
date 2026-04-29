package com.flosunn.pomodoro.presentation.week_year.add_new_year

import kotlinx.datetime.LocalDate


enum class PickTimeType {
    START_TIME,
    END_TIME
}

data class AddNewYearUIState(
    val startTimeMilliseconds: Long = 0L,
    val endTimeMilliseconds: Long = 0L,
    val isShowDatePicker: Boolean = false,
    val currentDate: LocalDate? = null,
    val pickTimeType: PickTimeType? = null,
)
