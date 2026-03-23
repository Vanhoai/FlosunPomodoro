package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val id: String,
    val accountId: String,
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: Int = 25,
    @ColumnInfo(name = "short_break_duration") val shortBreakDuration: Int = 5,
    @ColumnInfo(name = "long_break_duration") val longBreakDuration: Int = 15,
    @ColumnInfo(name = "long_break_after") val longBreakAfter: Int = 4,
    @ColumnInfo(name = "sound_enabled") val soundEnabled: Boolean = true,
    @ColumnInfo(name = "notification_enabled") val notificationEnabled: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),
)