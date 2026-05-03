package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,

    // References
    @ColumnInfo(name = "goal_id") val goalId: String,
    @ColumnInfo(name = "lagging_indicator_id") val laggingIndicatorId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_pomodoro") val numPomodoro: UInt,
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: UInt,
    @ColumnInfo(name = "date") val date: Long,
)