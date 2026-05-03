package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flosun.pomodoro.core.utils.StringArray
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "weeks")
data class WeekEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "year_id") val yearId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "reward") val reward: String = "",
    @ColumnInfo(name = "reward_images") val rewardImages: StringArray = emptyList(),
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "start_time_milliseconds") val startTimeMilliseconds: Long,
    @ColumnInfo(name = "end_time_milliseconds") val endTimeMilliseconds: Long,
)