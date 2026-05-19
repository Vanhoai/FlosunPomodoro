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
    tableName = "twelve_week_years",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["account_id"], unique = false),
    ],
)
data class TwelveWeekYearEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "account_id") val accountId: String,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cover") val cover: String,

    // Reward
    @ColumnInfo(name = "reward") val reward: String,
    @ColumnInfo(name = "reward_images") val rewardImages: StringArray = emptyList(),
    @ColumnInfo(name = "review") val review: String = "",
    @ColumnInfo(name = "stars") val stars: Int = 0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "address") val address: String = "",

    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "start_time_milliseconds") val startTimeMilliseconds: Long,
    @ColumnInfo(name = "end_time_milliseconds") val endTimeMilliseconds: Long,
)