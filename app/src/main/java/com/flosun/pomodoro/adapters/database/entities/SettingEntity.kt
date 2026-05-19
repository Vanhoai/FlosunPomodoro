package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.flosun.pomodoro.core.constants.DurationType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "settings",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
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
    @ColumnInfo(name = "daily_target_ms") val dailyTargetMs: Int = DurationType.DAILY_TARGET_DURATION.defaultValue,

    // Pomodoro
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: Int = DurationType.POMODORO_DURATION.defaultValue,
    @ColumnInfo(name = "short_break_duration") val shortBreakDuration: Int = DurationType.SHORT_BREAK_DURATION.defaultValue,
    @ColumnInfo(name = "long_break_duration") val longBreakDuration: Int = DurationType.LONG_BREAK_DURATION.defaultValue,
    @ColumnInfo(name = "long_break_after") val longBreakAfter: Int = DurationType.LONG_BREAK_AFTER.defaultValue,
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
    @ColumnInfo(name = "auto_reset_pomodoro") val autoResetPomodoro: Int = DurationType.AUTO_RESET_POMODORO.defaultValue,
    @ColumnInfo(name = "prevent_screen_lock") val preventScreenLock: Boolean = false,
    @ColumnInfo(name = "vibrant_in_silent_mode") val vibrantInSilentMode: Boolean = false,
) {
    fun update(
        isEnabledDailyTarget: Boolean = this.isEnabledDailyTarget,
        dailyTargetMs: Int = this.dailyTargetMs,
        pomodoroDuration: Int = this.pomodoroDuration,
        shortBreakDuration: Int = this.shortBreakDuration,
        longBreakDuration: Int = this.longBreakDuration,
        longBreakAfter: Int = this.longBreakAfter,
        isDisabledBreak: Boolean = this.isDisabledBreak,
        autoStartNextPomodoro: Boolean = this.autoStartNextPomodoro,
        autoStartBreak: Boolean = this.autoStartBreak,
        focusTicking: String = this.focusTicking,
        shortBreakTicking: String = this.shortBreakTicking,
        longBreakTicking: String = this.longBreakTicking,
        focusAlarm: String = this.focusAlarm,
        shortBreakAlarm: String = this.shortBreakAlarm,
        longBreakAlarm: String = this.longBreakAlarm,
        autoResetPomodoro: Int = this.autoResetPomodoro,
        preventScreenLock: Boolean = this.preventScreenLock,
        vibrantInSilentMode: Boolean = this.vibrantInSilentMode,
    ): SettingEntity {
        return copy(
            isEnabledDailyTarget = isEnabledDailyTarget,
            dailyTargetMs = dailyTargetMs,
            pomodoroDuration = pomodoroDuration,
            shortBreakDuration = shortBreakDuration,
            longBreakDuration = longBreakDuration,
            longBreakAfter = longBreakAfter,
            isDisabledBreak = isDisabledBreak,
            autoStartNextPomodoro = autoStartNextPomodoro,
            autoStartBreak = autoStartBreak,
            focusTicking = focusTicking,
            shortBreakTicking = shortBreakTicking,
            longBreakTicking = longBreakTicking,
            focusAlarm = focusAlarm,
            shortBreakAlarm = shortBreakAlarm,
            longBreakAlarm = longBreakAlarm,
            autoResetPomodoro = autoResetPomodoro,
            preventScreenLock = preventScreenLock,
            vibrantInSilentMode = vibrantInSilentMode,
            updatedAt = System.currentTimeMillis(),
        )
    }
}