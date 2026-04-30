package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lagging_indicators")
data class LaggingIndicatorEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,

    // References
    @ColumnInfo(name = "year_id") val yearId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
)