package com.flosun.pomodoro.core.services

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.TimerModeKey
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosunn.core.libraries.datastore.datastore
import com.flosunn.core.libraries.datastore.enumPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock

enum class TimerMode {
    COUNTDOWN,
    INFINITY,
}

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class PomodoroService @Inject constructor(
    application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
) : CoroutineService(application) {

    private val nowMilliseconds = Clock.System.todayIn(TimeZone.currentSystemDefault())
        .atTime(0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    private val timberMode by enumPreference(
        context = application.applicationContext,
        key = TimerModeKey,
        defaultValue = TimerMode.COUNTDOWN,
    )

    val totalTime = MutableStateFlow(0)
    val remainTime = MutableStateFlow(0)
    val isRunning = MutableStateFlow(false)

    private var currentTask: TaskEntity? = null

    fun run() = ioRun {
        val currentTaskId = appStorage.read(CURRENT_TASK_ID_KEY, "")
        val taskEntity = database.findTaskById(currentTaskId).first()

        // Make sure task entity created today
        if (taskEntity == null || taskEntity.date != nowMilliseconds) {
            showToast("Please select a today task to start the Pomodoro timer.")
            return@ioRun
        }

        currentTask = taskEntity
        if (timberMode == TimerMode.INFINITY) {
            totalTime.value = 0
            remainTime.value = 0
        } else {
            totalTime.value = taskEntity.pomodoroDuration
            remainTime.value = taskEntity.pomodoroDuration
        }

        isRunning.value = true
    }

    fun pause() {
        isRunning.value = false
    }
}