package com.flosunn.pomodoro.core.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val BIOMETRIC_CREDENTIALS_KEY = stringPreferencesKey("BIOMETRIC_CREDENTIALS")
val CURRENT_ACCOUNT_KEY = stringPreferencesKey("CURRENT_ACCOUNT")
val IS_ENABLED_BIOMETRIC_KEY = booleanPreferencesKey("IS_ENABLED_BIOMETRIC")
val IS_ENABLED_FACE_RECOGNITION_KEY = booleanPreferencesKey("IS_ENABLED_FACE_RECOGNITION")