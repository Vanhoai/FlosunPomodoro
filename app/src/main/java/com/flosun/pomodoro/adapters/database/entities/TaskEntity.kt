package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flosun.pomodoro.core.utils.StringArray
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "tasks")
data class TaskEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "goal_id") val goalId: String,
    @ColumnInfo(name = "lagging_indicator_id") val laggingIndicatorId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: Int,
    @ColumnInfo(name = "tags") val tags: StringArray,
    @ColumnInfo(name = "pomodoro_duration") val pomodoroDuration: Int,
    @ColumnInfo(name = "num_pomodoro") val numPomodoro: Int,
    @ColumnInfo(name = "num_pomodoro_completed") val numPomodoroCompleted: Int = 0,
    @ColumnInfo(name = "total_time_spent") val totalTimeSpent: Long = 0L,
    @ColumnInfo(name = "date") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
)