package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "settings",
    indices = [
        Index(value = ["account_id"], unique = true),
    ]
)
data class SettingEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // Reference
    @ColumnInfo(name = "account_id") val accountId: String,

    // Daily target
    @ColumnInfo(name = "is_enabled_daily_target") val isEnabledDailyTarget: Boolean = false,
    @ColumnInfo(name = "daily_target_ms") val dailyTargetMs: Int = 0,

    // Pomodoro
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: Int = 25,
    @ColumnInfo(name = "short_break_duration") val shortBreakDuration: Int = 5,
    @ColumnInfo(name = "long_break_duration") val longBreakDuration: Int = 15,
    @ColumnInfo(name = "long_break_after") val longBreakAfter: Int = 4,
    @ColumnInfo(name = "is_disabled_break") val isDisabledBreak: Boolean = false,
    @ColumnInfo(name = "auto_start_next_pomodoro") val autoStartNextPomodoro: Boolean = true,
    @ColumnInfo(name = "auto_start_break") val autoStartBreak: Boolean = true,

    // Ticking
    @ColumnInfo(name = "focus_ticking") val focusTicking: String,
    @ColumnInfo(name = "short_break_ticking") val shortBreakTicking: String,
    @ColumnInfo(name = "long_break_ticking") val longBreakTicking: String,

    // Alarm
    @ColumnInfo(name = "focus_alarm") val focusAlarm: String,
    @ColumnInfo(name = "short_break_alarm") val shortBreakAlarm: String,
    @ColumnInfo(name = "long_break_alarm") val longBreakAlarm: String,

    // Additional Settings
    @ColumnInfo(name = "auto_reset_pomodoro") val autoResetPomodoro: Long = 0L,
    @ColumnInfo(name = "prevent_screen_lock") val preventScreenLock: Boolean = false,
    @ColumnInfo(name = "vibrant_in_silent_mode") val vibrantInSilentMode: Boolean = false,
)