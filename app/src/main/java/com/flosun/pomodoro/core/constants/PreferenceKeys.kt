package com.flosun.pomodoro.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val PREFIX = "Pomodoro@App"
val BIOMETRIC_CREDENTIALS_KEY = stringPreferencesKey("BIOMETRIC_CREDENTIALS")
val CURRENT_ACCOUNT_KEY = stringPreferencesKey("CURRENT_ACCOUNT")
val IS_ENABLED_BIOMETRIC_KEY = booleanPreferencesKey("IS_ENABLED_BIOMETRIC")
val IS_ENABLED_FACE_RECOGNITION_KEY = booleanPreferencesKey("IS_ENABLED_FACE_RECOGNITION")
val CURRENT_THEME_KEY = stringPreferencesKey("CURRENT_THEME")

val CURRENT_ACCOUNT_ID_KEY = stringPreferencesKey("CURRENT_ACCOUNT_ID")
val CURRENT_YEAR_ID_KEY = stringPreferencesKey("${PREFIX}_CURRENT_YEAR_ID")
val CURRENT_TASK_ID_KEY = stringPreferencesKey("${PREFIX}_CURRENT_TASK_ID")
val SoundIdKey = stringPreferencesKey("${PREFIX}_CURRENT_SOUND_ID")
val TimerModeKey = stringPreferencesKey("${PREFIX}_TIMER_MODE")

// Custom Duration Keys
val DAILY_TARGET_DURATION_KEY = stringPreferencesKey("${PREFIX}_DAILY_TARGET_DURATION")
val POMODORO_DURATION_KEY = stringPreferencesKey("${PREFIX}_POMODORO_DURATION")
val SHORT_BREAK_DURATION_KEY = stringPreferencesKey("${PREFIX}_SHORT_BREAK_DURATION")
val LONG_BREAK_DURATION_KEY = stringPreferencesKey("${PREFIX}_LONG_BREAK_DURATION")
val LONG_BREAK_AFTER_KEY = stringPreferencesKey("${PREFIX}_LONG_BREAK_AFTER")
val AUTO_RESET_POMODORO_KEY = stringPreferencesKey("${PREFIX}_AUTO_RESET_POMODORO")