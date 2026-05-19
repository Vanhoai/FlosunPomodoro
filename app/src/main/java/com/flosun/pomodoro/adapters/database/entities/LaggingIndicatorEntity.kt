package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "lagging_indicators",
    foreignKeys = [
        ForeignKey(
            entity = TwelveWeekYearEntity::class,
            parentColumns = ["id"],
            childColumns = ["year_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["year_id"], unique = false),
    ],
)
data class LaggingIndicatorEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "year_id") val yearId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
)