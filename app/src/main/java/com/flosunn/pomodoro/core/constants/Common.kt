package com.flosunn.pomodoro.core.constants

enum class DurationType(
    val title: String,
) {
    POMODORO("Pomodoro Duration"),
    SHORT_BREAK("Short Break Duration"),
    LONG_BREAK("Long Break Duration"),
    LONG_BREAK_AFTER("Long Break After"),
}