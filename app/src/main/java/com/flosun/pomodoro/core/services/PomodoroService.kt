package com.flosun.pomodoro.core.services

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.TimerModeKey
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosunn.core.libraries.datastore.datastore
import com.flosunn.core.libraries.datastore.enumPreference
import io.ktor.utils.io.ioDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

enum class SessionType {
    WORK,
    SHORT_BREAK,
    LONG_BREAK,
}

data class PomodoroSession(
    val type: SessionType = SessionType.WORK,
    val totalDurationMs: Long = 0L,
    val elapsedMs: Long = 0L,
    val completedCycles: Int = 0, // Track cycles for long break
)

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

    private val timerMode by enumPreference(
        context = application.applicationContext,
        key = TimerModeKey,
        defaultValue = TimerMode.COUNTDOWN,
    )

    private val currentTask = application
        .datastore
        .data
        .map { it[CURRENT_TASK_ID_KEY] ?: "" }
        .distinctUntilChanged()
        .flatMapLatest { taskId -> database.findTaskByIdAndDate(taskId, nowMilliseconds) }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    private val currentSetting = application
        .datastore
        .data
        .map { it[CURRENT_ACCOUNT_ID_KEY] ?: "" }
        .distinctUntilChanged()
        .flatMapLatest { accountId -> database.findSettingByAccountId(accountId) }
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    // Session State
    val totalTime = MutableStateFlow(0)
    val remainTime = MutableStateFlow(0)
    val isRunning = MutableStateFlow(false)

    // Timber coroutine scope
    private var timerJob: Job? = null

    fun togglePlayPause() = ioRun {
        if (currentTask.value == null) {
            showToast("Please select a task to start the timer.")
            return@ioRun
        }
        val task = currentTask.value ?: return@ioRun

        totalTime.value = task.pomodoroDuration * 60
        remainTime.value = task.pomodoroDuration * 60
        isRunning.value = true
        startTimer()
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch(ioDispatcher()) {
            while (isRunning.value) {
                delay(1000L)

                val newRemain = remainTime.value - 1
                if (newRemain <= 0) {
                    remainTime.value = 0
                    isRunning.value = false
                } else {
                    remainTime.value = newRemain
                }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        timerJob?.cancel()
    }
}