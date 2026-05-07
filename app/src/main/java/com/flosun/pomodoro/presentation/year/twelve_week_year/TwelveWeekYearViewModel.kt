package com.flosun.pomodoro.presentation.year.twelve_week_year

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.utils.datastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TwelveWeekYearViewModel @Inject constructor(
    application: Application,
    private val database: PomodoroDatabase,
) : ViewModel() {

    val currentYearId = application.datastore.data
        .map { it[CURRENT_YEAR_ID_KEY] }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val allYears = currentYearId
        .flatMapLatest { currentId ->
            database
                .findAllTwelveWeekYears()
                .map { it.filterNot { year -> year.id == currentId } }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val currentYear = application
        .datastore
        .data
        .map { it[CURRENT_YEAR_ID_KEY] }
        .distinctUntilChanged()
        .flatMapLatest { database.findTwelveWeekYearById(it ?: "") }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}