package com.flosunn.pomodoro.presentation.week_year.add_new_year

import androidx.lifecycle.ViewModel
import com.flosunn.pomodoro.core.functions.TimeFuncs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class AddNewYearViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddNewYearUIState())
    val uiState: StateFlow<AddNewYearUIState> = _uiState.asStateFlow()

    init {
        val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val endTime = startTime.plus(12, DateTimeUnit.WEEK)

        val startTimeMilliseconds = startTime
            .atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        val endTimeMilliseconds = endTime
            .atTime(23, 59, 59)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        _uiState.update {
            it.copy(
                startTimeMilliseconds = startTimeMilliseconds,
                endTimeMilliseconds = endTimeMilliseconds
            )
        }
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        if (uiState.value.pickTimeType == null) return

        if (uiState.value.pickTimeType == PickTimeType.START_TIME) {
            val selectedTime = TimeFuncs.convertToMilliseconds(
                year = year,
                month = month,
                day = day,
                hour = 0,
                minute = 0,
                second = 0
            )

            _uiState.update {
                it.copy(
                    startTimeMilliseconds = selectedTime,
                    pickTimeType = null
                )
            }
            return
        }

        // validate end time must be after start time and the period between them must be 12 weeks
        val selectedTime = TimeFuncs.convertToMilliseconds(
            year = year,
            month = month,
            day = day,
            hour = 23,
            minute = 59,
            second = 59
        )

        _uiState.update {
            it.copy(
                endTimeMilliseconds = selectedTime,
                pickTimeType = null
            )
        }
    }

    fun onCloseDatePicker() {
        _uiState.update {
            it.copy(
                isShowDatePicker = false,
                pickTimeType = null
            )
        }
    }

    fun onSelectStartTime() {
        val dateTime = TimeFuncs.dateTimeFromMilliseconds(uiState.value.startTimeMilliseconds)

        _uiState.update {
            it.copy(
                currentDate = dateTime.date,
                isShowDatePicker = true,
                pickTimeType = PickTimeType.START_TIME
            )
        }
    }

    fun onSelectEndTime() {
        val dateTime = TimeFuncs.dateTimeFromMilliseconds(uiState.value.endTimeMilliseconds)

        _uiState.update {
            it.copy(
                currentDate = dateTime.date,
                isShowDatePicker = true,
                pickTimeType = PickTimeType.END_TIME
            )
        }
    }
}