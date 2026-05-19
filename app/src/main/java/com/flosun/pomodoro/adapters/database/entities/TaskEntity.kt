package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.flosun.pomodoro.core.utils.StringArray
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = GoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goal_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = LaggingIndicatorEntity::class,
            parentColumns = ["id"],
            childColumns = ["lagging_indicator_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["goal_id"], unique = false),
        Index(value = ["lagging_indicator_id"], unique = false),
    ]
)
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
) {
    fun update(
        name: String = this.name,
        icon: Int = this.icon,
        tags: StringArray = this.tags,
        pomodoroDuration: Int = this.pomodoroDuration,
        numPomodoro: Int = this.numPomodoro,
        numPomodoroCompleted: Int = this.numPomodoroCompleted,
        totalTimeSpent: Long = this.totalTimeSpent,
        date: Long = this.date,
        isCompleted: Boolean = this.isCompleted,
    ): TaskEntity {
        return copy(
            name = name,
            icon = icon,
            tags = tags,
            pomodoroDuration = pomodoroDuration,
            numPomodoro = numPomodoro,
            numPomodoroCompleted = numPomodoroCompleted,
            totalTimeSpent = totalTimeSpent,
            date = date,
            isCompleted = isCompleted,
            updatedAt = System.currentTimeMillis(),
        )
    }
}