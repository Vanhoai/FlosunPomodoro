package com.flosun.pomodoro.core.services

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.constants.TimerModeKey
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosunn.core.libraries.datastore.datastore
import com.flosunn.core.libraries.datastore.enumPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class TimerMode {
    COUNTDOWN,
    INFINITY,
}

enum class SessionType {
    WORK,
    SHORT_BREAK,
    LONG_BREAK,
}

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    FINISHED,
}

data class TimerConfig(
    val workDuration: Int,
    val shortBreakDuration: Int,
    val longBreakDuration: Int,
    val cyclesBeforeLongBreak: Int,
)

data class PomodoroSession(
    val taskId: String = "",
    val type: SessionType = SessionType.WORK,
    val timerState: TimerState = TimerState.IDLE,

    // Countdown mode
    val totalTime: Int = 0,
    val remainTime: Int = 0,

    // Infinity mode
    val elapsedTime: Int = 0,

    // Cycle tracking
    val completedCycles: Int = 0,
)

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class PomodoroService @Inject constructor(
    application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
) : CoroutineService(application) {

    private val nowMilliseconds = TimeFuncs.nowMilliseconds()

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


    val session = MutableStateFlow(PomodoroSession())

    private var timerJob: Job? = null
    private var timerConfig: TimerConfig? = null

    fun play() = ioRun {
        if (currentTask.value == null) {
            showToast("Please select a task to start the timer.")
            return@ioRun
        }

        val taskEntity = currentTask.value ?: return@ioRun
        if (session.value.timerState == TimerState.IDLE) {
            buildConfig(taskEntity)
            initSession(taskEntity.id, SessionType.WORK)
        }

        session.update { it.copy(timerState = TimerState.RUNNING) }
        startTick()
    }

    fun pause() {
        timerJob?.cancel()
        session.update { it.copy(timerState = TimerState.PAUSED) }
    }

    fun skip() {
        timerJob?.cancel()
        // Move to next session
    }

    fun reset() {
        timerJob?.cancel()
        session.value = PomodoroSession()
        timerConfig = null
    }


    private fun startTick() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch(Dispatchers.IO) {
            while (session.value.timerState == TimerState.RUNNING) {
                delay(1000L)
                tick()
            }
        }
    }

    private fun tick() {
        when (timerMode) {
            TimerMode.COUNTDOWN -> tickCountdown()
            TimerMode.INFINITY -> tickInfinity()
        }
    }

    private fun tickCountdown() {
        val current = session.value
        val newRemain = current.remainTime - 1

        if (newRemain <= 0) {
            // Session finished
            session.update {
                it.copy(
                    remainTime = 0,
                    elapsedTime = it.totalTime,
                    timerState = TimerState.FINISHED,
                )
            }
            onSessionFinished()
            timerJob?.cancel()
        } else {
            session.update {
                it.copy(
                    remainTime = newRemain,
                    elapsedTime = it.totalTime - newRemain,
                )
            }
        }
    }

    private fun onSessionFinished() = ioRun {
        val current = session.value
        val config = timerConfig ?: return@ioRun

        // Save session record to database
        // Move to next session
    }

    private fun tickInfinity() {
        session.update { it.copy(elapsedTime = it.elapsedTime + 1) }
    }

    private fun buildConfig(taskEntity: TaskEntity) {
        val setting = currentSetting.value ?: return

        timerConfig = TimerConfig(
            workDuration = taskEntity.pomodoroDuration,
            shortBreakDuration = setting.shortBreakDuration,
            longBreakDuration = setting.longBreakDuration,
            cyclesBeforeLongBreak = setting.longBreakAfter,
        )
    }

    private fun initSession(
        taskId: String,
        type: SessionType,
        completedCycles: Int = session.value.completedCycles,
    ) {
        val config = timerConfig ?: return
        val duration = durationFor(type, config)

        session.value = PomodoroSession(
            taskId = taskId,
            type = type,
            timerState = TimerState.IDLE,
            totalTime = duration,
            remainTime = duration,
            elapsedTime = 0,
            completedCycles = completedCycles,
        )
    }

    private fun durationFor(type: SessionType, config: TimerConfig): Int {
        return when (type) {
            SessionType.WORK -> config.workDuration
            SessionType.SHORT_BREAK -> config.shortBreakDuration
            SessionType.LONG_BREAK -> config.longBreakDuration
        }
    }

    override fun onDispose() {
        super.onDispose()
        timerJob?.cancel()
    }
}