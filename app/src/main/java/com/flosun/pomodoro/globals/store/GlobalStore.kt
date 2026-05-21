package com.flosun.pomodoro.globals.store

import android.app.Application
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosunn.core.libraries.datastore.datastore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.System
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GlobalStore @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
) {
    private val now = TimeFuncs.nowMilliseconds()
    private lateinit var scope: LifecycleCoroutineScope

    // StateFlow to hold in life cycle scope
    private val _accountFlow = MutableStateFlow<AccountEntity?>(null)
    val account = _accountFlow.asStateFlow()
    
    private val _settingFlow = MutableStateFlow<SettingEntity?>(null)
    val setting = _settingFlow.asStateFlow()

    private val _taskFlow = MutableStateFlow<TaskEntity?>(null)
    val task = _taskFlow.asStateFlow()

    private val _yearFlow = MutableStateFlow<TwelveWeekYearEntity?>(null)
    val year = _yearFlow.asStateFlow()

    fun init(scope: LifecycleCoroutineScope) {
        this.scope = scope

        observeCurrentAccount()
        observeCurrentSetting()
        observeCurrentTask()
        observeCurrentYear()
    }

    fun observeCurrentAccount() = scope.launch {
        application
            .datastore
            .data
            .map { it[CURRENT_ACCOUNT_ID_KEY] ?: "" }
            .distinctUntilChanged()
            .flatMapLatest { database.findAccountById(it) }
            .collect { accountEntity -> _accountFlow.value = accountEntity }
    }

    fun observeCurrentSetting() = scope.launch {
        application.datastore.data
            .map { it[CURRENT_ACCOUNT_ID_KEY] ?: "" }
            .distinctUntilChanged()
            .flatMapLatest { accountId -> database.findSettingByAccountId(accountId) }
            .collect { settingEntity -> _settingFlow.value = settingEntity }
    }

    fun observeCurrentTask() = scope.launch {
        application.datastore.data
            .map { it[CURRENT_TASK_ID_KEY] ?: "" }
            .distinctUntilChanged()
            .flatMapLatest { taskId -> database.findTaskByIdAndDate(taskId, now) }
            .collect { taskEntity -> _taskFlow.value = taskEntity }
    }

    fun observeCurrentYear() = scope.launch {
        application.datastore.data
            .map { it[CURRENT_YEAR_ID_KEY] ?: "" }
            .distinctUntilChanged()
            .flatMapLatest { yearId -> database.findTwelveWeekYearById(yearId) }
            .collect { yearEntity -> _yearFlow.value = yearEntity }
    }

}