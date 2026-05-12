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
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosunn.core.libraries.datastore.datastore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

enum class TimberMode {
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

    var timberMode: TimberMode by mutableStateOf(TimberMode.COUNTDOWN)
    var totalTime: Long by mutableLongStateOf(0L)
    var remainingTime: Long by mutableLongStateOf(0L)

    private var currentTask: TaskEntity? = null

    fun run() = ioRun {
        val currentTaskId = appStorage.read(CURRENT_TASK_ID_KEY, "")
        currentTask = database.findTaskById(currentTaskId).first()

        if (currentTask == null) {
            showToast("Please select a task to start the Pomodoro timer.")
            return@ioRun
        }

    }
}