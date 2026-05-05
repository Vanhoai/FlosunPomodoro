package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "settings")
data class SettingEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // Reference
    @ColumnInfo(name = "account_id") val accountId: String,

    // Daily target
    @ColumnInfo(name = "is_enabled_daily_target") val isEnabledDailyTarget: Boolean,
    @ColumnInfo(name = "daily_target_ms") val dailyTargetMs: Int,

    // Pomodoro
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: Int,
    @ColumnInfo(name = "short_break_duration") val shortBreakDuration: Int,
    @ColumnInfo(name = "long_break_duration") val longBreakDuration: Int,
    @ColumnInfo(name = "long_break_after") val longBreakAfter: Int,
    @ColumnInfo(name = "is_disabled_break") val isDisabledBreak: Boolean,
    @ColumnInfo(name = "auto_start_next_pomodoro") val autoStartNextPomodoro: Boolean,
    @ColumnInfo(name = "auto_start_break") val autoStartBreak: Boolean,

    // Ticking
    @ColumnInfo(name = "focus_ticking") val focusTicking: Int,
    @ColumnInfo(name = "short_break_ticking") val shortBreakTicking: Int,
    @ColumnInfo(name = "long_break_ticking") val longBreakTicking: Int,

    // Alarm
    @ColumnInfo(name = "focus_alarm") val focusAlarm: Int,
    @ColumnInfo(name = "short_break_alarm") val shortBreakAlarm: Int,
    @ColumnInfo(name = "long_break_alarm") val longBreakAlarm: Int,

    // Additional Settings
    @ColumnInfo(name = "auto_reset_pomodoro") val autoResetPomodoro: Long,
    @ColumnInfo(name = "prevent_screen_lock") val preventScreenLock: Boolean,
    @ColumnInfo(name = "vibrant_in_silent_mode") val vibrantInSilentMode: Boolean,
)