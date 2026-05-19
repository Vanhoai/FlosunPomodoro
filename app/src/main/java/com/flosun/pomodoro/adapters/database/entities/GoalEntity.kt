package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "goals",
    foreignKeys = [
        ForeignKey(
            entity = WeekEntity::class,
            parentColumns = ["id"],
            childColumns = ["week_id"],
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
        Index(value = ["week_id"], unique = false),
        Index(value = ["lagging_indicator_id"], unique = false),
    ]
)
data class GoalEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "week_id") val weekId: String,
    @ColumnInfo(name = "lagging_indicator_id") val laggingIndicatorId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: Int,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "progress") val progress: Int = 0,
)