package com.flosun.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.AppStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val database: PomodoroDatabase,
    private val appStorage: AppStorage,
) : ViewModel() {

    fun checkAndSetCurrentYear() = viewModelScope.launch(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        database.findTwelveWeekYearByDate(now).collect {
            if (it != null) appStorage.write(CURRENT_YEAR_ID_KEY, it.id)
            else appStorage.remove(CURRENT_YEAR_ID_KEY)
        }
    }

}