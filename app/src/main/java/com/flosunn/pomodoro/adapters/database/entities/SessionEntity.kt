package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "task_id") val taskId: String,
    val duration: Int,
    val completed: Boolean,
    @ColumnInfo(name = "start_at") val startAt: Long,
    @ColumnInfo(name = "end_at") val endAt: Long,
)