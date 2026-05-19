package com.flosun.pomodoro.presentation.swipe.settings.preference

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.libraries.datastore.rememberPreference
import com.flosunn.core.libraries.datepicker.DurationPicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PreferenceView(navBackStack: NavBackStack<NavKey>) {
    val database = LocalDatabase.current
    val accountId by rememberPreference(CURRENT_ACCOUNT_ID_KEY, "")

    val settingWithSounds by database
        .findSettingWithSoundsByAccountId(accountId)
        .collectAsState(null)

    if (settingWithSounds == null || settingWithSounds!!.isAnySoundNull()) return
    val coroutineScope = rememberCoroutineScope()

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                CommonBackHeading(
                    onBack = { navBackStack.removeLastOrNull() },
                    title = "Preferences"
                )
            }

            item {
                DailyTargetPreferences(
                    dailyTarget = settingWithSounds!!.setting.dailyTargetMs,
                    isEnabledDailyTarget = settingWithSounds!!.setting.isEnabledDailyTarget,
                    onChangedIsEnabledDailyTarget = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(isEnabledDailyTarget = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                )
            }

            item {
                PomodoroPreferences(
                    pomodoroDuration = settingWithSounds!!.setting.pomodoroDuration,
                    shortBreakDuration = settingWithSounds!!.setting.shortBreakDuration,
                    longBreakDuration = settingWithSounds!!.setting.longBreakDuration,
                    longBreakAfter = settingWithSounds!!.setting.longBreakAfter,
                    isDisabledBreak = settingWithSounds!!.setting.isDisabledBreak,
                    onChangedIsDisabledBreak = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(isDisabledBreak = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                    isAutoStartNextPomodoro = settingWithSounds!!.setting.autoStartNextPomodoro,
                    onChangedIsAutoStartNextPomodoro = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(autoStartNextPomodoro = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                    isAutoStartBreak = settingWithSounds!!.setting.autoStartBreak,
                    onChangedIsAutoStartBreak = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(autoStartBreak = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                )
            }

            item {
                TickingPreferences(
                    focusTicking = settingWithSounds!!.focusTickingSound!!,
                    shortBreakTicking = settingWithSounds!!.shortBreakTickingSound!!,
                    longBreakTicking = settingWithSounds!!.longBreakTickingSound!!,
                )
            }

            item {
                AlarmPreferences(
                    focusAlarm = settingWithSounds!!.focusAlarmSound!!,
                    shortBreakAlarm = settingWithSounds!!.shortBreakAlarmSound!!,
                    longBreakAlarm = settingWithSounds!!.longBreakAlarmSound!!,
                )
            }

            item {
                AdditionalPreferences(
                    autoResetPomodoro = settingWithSounds!!.setting.autoResetPomodoro,
                    preventScreenLock = settingWithSounds!!.setting.preventScreenLock,
                    onChangePreventScreenLock = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(preventScreenLock = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                    vibrateInSilentMode = settingWithSounds!!.setting.vibrantInSilentMode,
                    onChangeVibrateInSilentMode = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val updateSetting =
                                settingWithSounds!!.setting.copy(vibrantInSilentMode = it)
                            database.updateSetting(updateSetting)
                        }
                    },
                )
            }
        }
    }
}