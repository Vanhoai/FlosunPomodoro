package com.flosun.pomodoro.presentation.swipe.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val database: PomodoroDatabase,
) : BaseViewModel(application) {

    private val nowMilliseconds = Clock.System.todayIn(TimeZone.currentSystemDefault())
        .atTime(0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    val tasks = database
        .findTasksByDate(nowMilliseconds, false)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

}