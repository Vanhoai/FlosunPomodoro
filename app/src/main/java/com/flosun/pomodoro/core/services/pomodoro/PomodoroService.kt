package com.flosun.pomodoro.core.services.pomodoro

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.TimerModeKey
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.services.CoroutineService
import com.flosun.pomodoro.core.services.FocusService
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.globals.events.GlobalEvent
import com.flosun.pomodoro.globals.events.GlobalEventBus
import com.flosun.pomodoro.globals.store.GlobalStore
import com.flosun.pomodoro.ui.components.shared.AlertMessageManager
import com.flosunn.core.libraries.datastore.datastore
import com.flosunn.core.libraries.datastore.enumPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

data class TimerSession(
    val type: SessionType = SessionType.WORK,
    val mode: TimerMode = TimerMode.COUNTDOWN,
    val state: TimerState = TimerState.INITIALIZING,

    val elapsedTime: Int = 0,  // For infinity mode
    val remainTime: Int = 0,
    val totalTime: Int = 0,

    // Cycle tracking
    val completedCycles: Int = 0,
)

data class TimerConfig(
    val workDuration: Int = 0,
    val shortBreakDuration: Int = 0,
    val longBreakDuration: Int = 0,

    val cyclesBeforeLongBreak: Int = 0,

    val isDisabledBreak: Boolean = false,
    val autoStartNextPomodoro: Boolean = false,
    val autoStartBreak: Boolean = false,
) {
    fun update(
        workDuration: Int = this.workDuration,
        shortBreakDuration: Int = this.shortBreakDuration,
        longBreakDuration: Int = this.longBreakDuration,
        cyclesBeforeLongBreak: Int = this.cyclesBeforeLongBreak,
        isDisabledBreak: Boolean = this.isDisabledBreak,
        autoStartNextPomodoro: Boolean = this.autoStartNextPomodoro,
        autoStartBreak: Boolean = this.autoStartBreak,
    ): TimerConfig {
        return copy(
            workDuration = workDuration,
            shortBreakDuration = shortBreakDuration,
            longBreakDuration = longBreakDuration,
            cyclesBeforeLongBreak = cyclesBeforeLongBreak,
            isDisabledBreak = isDisabledBreak,
            autoStartNextPomodoro = autoStartNextPomodoro,
            autoStartBreak = autoStartBreak,
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class PomodoroService @Inject constructor(
    application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
    private val focusService: FocusService,
    private val globalStore: GlobalStore,
    private val alertMessageManager: AlertMessageManager,
    private val globalEventBus: GlobalEventBus,
) : CoroutineService(application) {

    private val _timerSession = MutableStateFlow(TimerSession())
    val session = _timerSession.asStateFlow()

    private val timerMode by enumPreference(
        context = application.applicationContext,
        key = TimerModeKey,
        defaultValue = TimerMode.COUNTDOWN,
    )

    private var timerJob: Job? = null
    private var timerConfig: TimerConfig? = null

    init {
        ioRun {
            coroutineScope.launch { listenConfigChange() }
        }
    }

    private suspend fun listenConfigChange() {
        combine(globalStore.task, globalStore.setting) { task, setting ->
            Pair(task, setting)
        }.collect { (_, _) ->
            buildConfig()

            if (session.value.state != TimerState.INITIALIZING) {
                // If session is already initialized, update the current session with new config
                val currentType = session.value.type
                val elapsedTime = session.value.elapsedTime

                val newTotalTime = durationFor(currentType, timerConfig!!)
                val newRemainTime = (newTotalTime - elapsedTime).coerceAtLeast(0)

                _timerSession.update {
                    it.copy(
                        totalTime = newTotalTime,
                        remainTime = newRemainTime,
                    )
                }
            }
        }
    }

    fun play() = when (session.value.state) {
        TimerState.INITIALIZING -> startNewSession()
        TimerState.PAUSED -> {
            _timerSession.update { it.copy(state = TimerState.RUNNING) }
            startTick()
        }

        TimerState.IDLE -> {
            _timerSession.update { it.copy(state = TimerState.RUNNING) }
            startTick()
        }

        else -> {}
    }

    fun pause() {
        timerJob?.cancel()
        focusService.stopFocusMode()
        _timerSession.update { it.copy(state = TimerState.PAUSED) }
    }

    fun skip() {
        timerJob?.cancel()
        moveToNextSession()
    }

    fun reset() {
        timerJob?.cancel()
        val duration = durationFor(session.value.type, timerConfig!!)
        _timerSession.update {
            it.copy(
                state = TimerState.IDLE,
                elapsedTime = 0,
                remainTime = duration,
                totalTime = duration,
            )
        }
    }

    private fun moveToNextSession() {
        val (nextType, nextCycles) = resolveNextSession(
            currentType = session.value.type,
            completedCycles = session.value.completedCycles,
            config = timerConfig!!,
        )

        initSession(type = nextType, completedCycles = nextCycles)
        val autoStart = when (nextType) {
            SessionType.WORK -> timerConfig!!.autoStartNextPomodoro
            SessionType.SHORT_BREAK,
            SessionType.LONG_BREAK -> timerConfig!!.autoStartBreak
        }

        if (autoStart) {
            _timerSession.update { it.copy(state = TimerState.RUNNING) }
            startTick()
        } else {
            _timerSession.update { it.copy(state = TimerState.IDLE) }
        }
    }

    private fun resolveNextSession(
        currentType: SessionType,
        completedCycles: Int,
        config: TimerConfig,
    ): Pair<SessionType, Int> {
        return when (currentType) {
            SessionType.WORK -> {
                val newCycles = completedCycles + 1
                if (newCycles % config.cyclesBeforeLongBreak == 0) {
                    SessionType.LONG_BREAK to newCycles
                } else {
                    SessionType.SHORT_BREAK to newCycles
                }
            }

            SessionType.SHORT_BREAK,
            SessionType.LONG_BREAK -> SessionType.WORK to completedCycles
        }
    }

    private fun startNewSession() {
        if (globalStore.task.value == null) {
            alertMessageManager.showMessage(
                title = "Warning",
                description = "Do you want to start a new session without an active task? You can select a task in the home screen.",
                onConfirm = {
                    initSession(
                        type = SessionType.WORK,
                        completedCycles = 0,
                    )
                    _timerSession.update { it.copy(state = TimerState.RUNNING) }
                    startTick()
                },
            )
            return
        }

        initSession(
            type = SessionType.WORK,
            completedCycles = 0,
        )
        _timerSession.update { it.copy(state = TimerState.RUNNING) }
        startTick()
    }

    private fun startTick() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch(Dispatchers.IO) {
            while (session.value.state == TimerState.RUNNING) {
                delay(1000L)
                tick()
            }
        }

        focusService.startFocusMode()
    }

    private fun tick() = when (session.value.mode) {
        TimerMode.INFINITY -> tickInfinityMode()
        TimerMode.COUNTDOWN -> tickCountdownMode()
    }

    private fun tickInfinityMode() {
        val currentSession = session.value
        val newElapsedTime = currentSession.elapsedTime + 1
        _timerSession.update { it.copy(elapsedTime = newElapsedTime) }
    }

    private fun tickCountdownMode() {
        val currentSession = session.value
        val newRemain = currentSession.remainTime - 1
        if (newRemain > 0) {
            _timerSession.update {
                it.copy(
                    remainTime = newRemain,
                    elapsedTime = it.totalTime - newRemain,
                )
            }
            return
        }

        // Session completed, move to next session
        _timerSession.update {
            it.copy(
                remainTime = 0,
                elapsedTime = it.totalTime,
                state = TimerState.FINISHED,
            )
        }

        timerJob?.cancel()
        focusService.stopFocusMode()
        onSessionFinished()
    }

    private fun initSession(
        type: SessionType = SessionType.WORK,
        completedCycles: Int = 0,
    ) {
        val duration = durationFor(type, timerConfig!!)

        _timerSession.update {
            it.copy(
                type = type,
                state = TimerState.IDLE,
                mode = timerMode,
                remainTime = duration,
                totalTime = duration,
                elapsedTime = 0,
                completedCycles = completedCycles,
            )
        }
    }

    private fun durationFor(type: SessionType, config: TimerConfig): Int {
        return when (type) {
            SessionType.WORK -> config.workDuration
            SessionType.SHORT_BREAK -> config.shortBreakDuration
            SessionType.LONG_BREAK -> config.longBreakDuration
        }
    }

    private fun buildConfig() {
        timerConfig = TimerConfig()
        val setting = globalStore.setting.value
        if (setting != null) {
            timerConfig = timerConfig!!.update(
                workDuration = setting.pomodoroDuration * 60,
                shortBreakDuration = setting.shortBreakDuration * 60,
                longBreakDuration = setting.longBreakDuration * 60,
                cyclesBeforeLongBreak = setting.longBreakAfter,
                isDisabledBreak = setting.isDisabledBreak,
                autoStartNextPomodoro = setting.autoStartNextPomodoro,
                autoStartBreak = setting.autoStartBreak,
            )
        }

        val task = globalStore.task.value
        if (task != null) timerConfig = timerConfig!!.update(
            workDuration = task.pomodoroDuration * 60
        )
    }

    private fun onSessionFinished() = ioRun {
        // Update Task's completed pomodoro count in database
        coroutineScope.launch(Dispatchers.IO) {
            val task = globalStore.task.value ?: return@launch
            val updatedTask = task.copy(
                numPomodoroCompleted = task.numPomodoroCompleted + 1,
                totalTimeSpent = task.totalTimeSpent + session.value.elapsedTime,
            )

            database.updateTask(updatedTask)
        }
        // Emit alarm sound event
        val setting = globalStore.setting.value ?: return@ioRun
        val soundId = when (session.value.type) {
            SessionType.WORK -> setting.focusAlarm
            SessionType.SHORT_BREAK -> setting.shortBreakAlarm
            SessionType.LONG_BREAK -> setting.longBreakAlarm
        }

        val soundEntity = database.findSoundById(soundId).first()
        if (soundEntity != null) {
            globalEventBus.sendEvent(
                GlobalEvent.PlayAlarm(
                    mediaItem = soundEntity.toMediaItem(),
                    onFinished = {
                        if (session.value.type == SessionType.WORK) {
                            alertMessageManager.showMessage(
                                title = "Session Completed",
                                description = "You have completed a focus session. Great job! Take a break or start the next session.",
                                onConfirm = { moveToNextSession() },
                            )
                        } else {
                            moveToNextSession()
                        }
                    },
                )
            )
        }
    }

    override fun onDispose() {
        super.onDispose()
        timerJob?.cancel()
    }
}