package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flosunn.pomodoro.core.utils.StringArray

@Entity(tableName = "weeks")
data class WeekEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,

    // References
    @ColumnInfo(name = "year_id") val yearId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "reward") val reward: String,
    @ColumnInfo(name = "reward_images") val rewardImages: StringArray,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @ColumnInfo(name = "progress") val progress: Int,
    @ColumnInfo(name = "start_time_milliseconds") val startTimeMilliseconds: Long,
    @ColumnInfo(name = "end_time_milliseconds") val endTimeMilliseconds: Long,
)