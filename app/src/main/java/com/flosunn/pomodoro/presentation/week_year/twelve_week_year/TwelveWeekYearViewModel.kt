package com.flosunn.pomodoro.presentation.week_year.twelve_week_year

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosunn.pomodoro.adapters.database.PomodoroDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TwelveWeekYearViewModel @Inject constructor(
    database: PomodoroDatabase,
) : ViewModel() {

    val allYears = database
        .findAllTwelveWeekYears()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
}