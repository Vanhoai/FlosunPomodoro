package com.flosunn.pomodoro.presentation.week_year.add_new_year

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.flosunn.pomodoro.adapters.database.PomodoroDatabase
import com.flosunn.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosunn.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.core.functions.TimeFuncs
import com.flosunn.pomodoro.core.utils.AppStorage
import com.flosunn.pomodoro.core.utils.StringArray
import com.flosunn.pomodoro.domain.entities.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class AddNewYearViewModel @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
    private val appStorage: AppStorage,
) : ViewModel() {

//    @ColumnInfo(name = "account_id") val accountId: String,
//    @ColumnInfo(name = "name") val name: String,
//    @ColumnInfo(name = "cover") val cover: String,
//    @ColumnInfo(name = "reward") val reward: String,
//    @ColumnInfo(name = "reward_images") val rewardImages: StringArray,
//    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
//    @ColumnInfo(name = "start_time_milliseconds") val startTimeMilliseconds: Long,
//    @ColumnInfo(name = "end_time_milliseconds") val endTimeMilliseconds: Long,

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

    fun addNewYear() = viewModelScope.launch {
        val currentAccount = appStorage.readSerializable<Account?>(CURRENT_ACCOUNT_KEY, null)
        if (currentAccount == null) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to retrieve account information. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

//        @ColumnInfo(name = "account_id") val accountId: String,
//
//        @ColumnInfo(name = "name") val name: String,
//        @ColumnInfo(name = "cover") val cover: String,
//        @ColumnInfo(name = "reward") val reward: String,
//        @ColumnInfo(name = "reward_images") val rewardImages: StringArray,
//        @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
//        @ColumnInfo(name = "start_time_milliseconds") val startTimeMilliseconds: Long,
//        @ColumnInfo(name = "end_time_milliseconds") val endTimeMilliseconds: Long,
        val newYear = TwelveWeekYearEntity(
            accountId = currentAccount.id,
            name = "New Year",
            cover = "",
            reward = "",
            rewardImages = emptyList(),
            isCompleted = false,
            startTimeMilliseconds = uiState.value.startTimeMilliseconds,
            endTimeMilliseconds = uiState.value.endTimeMilliseconds,
        )
    }
}