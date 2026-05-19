package com.flosun.pomodoro.core.constants

import androidx.datastore.preferences.core.Preferences


const val DEBUG_TAG = "PomodoroDebug"
const val POMODORO_DATABASE = "PomodoroDatabase"

enum class DurationType(
    val title: String,
    val defaultOptions: List<Int>,
    val defaultValue: Int,
    val preferenceKey: Preferences.Key<String>,
) {
    DAILY_TARGET_DURATION(
        title = "Daily Target Duration",
        defaultOptions = listOf(400, 420, 440, 460, 480, 500),
        defaultValue = 480,
        preferenceKey = DAILY_TARGET_DURATION_KEY,
    ),
    POMODORO_DURATION(
        title = "Pomodoro Duration",
        defaultOptions = listOf(15, 25, 50, 60, 90, 120),
        defaultValue = 25,
        preferenceKey = POMODORO_DURATION_KEY,
    ),
    SHORT_BREAK_DURATION(
        title = "Short Break Duration",
        defaultOptions = listOf(5, 10, 15, 20),
        defaultValue = 5,
        preferenceKey = SHORT_BREAK_DURATION_KEY,
    ),
    LONG_BREAK_DURATION(
        title = "Long Break Duration",
        defaultOptions = listOf(15, 30, 35, 40, 45),
        defaultValue = 20,
        preferenceKey = LONG_BREAK_DURATION_KEY,
    ),
    LONG_BREAK_AFTER(
        title = "Long Break After",
        defaultOptions = listOf(1, 2, 3, 4),
        defaultValue = 3,
        preferenceKey = LONG_BREAK_AFTER_KEY,
    ),

    // Time in seconds of a day so max value is 24 * 60 * 60 = 86400
    // Examples:
    // 0 -> 00:00 AM
    // 3600 -> 01:00 AM
    // 43200 -> 12:00 PM
    AUTO_RESET_POMODORO(
        title = "Auto Reset Pomodoro",
        defaultOptions = listOf(0, 3600, 7200, 10800, 14400, 18000),
        defaultValue = 0,
        preferenceKey = AUTO_RESET_POMODORO_KEY,
    ),
}