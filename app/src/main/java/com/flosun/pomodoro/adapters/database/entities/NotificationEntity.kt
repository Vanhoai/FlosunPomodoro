package com.flosun.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flosun.pomodoro.R
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "notifications",
)
data class NotificationEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    // References
    @ColumnInfo(name = "account_id") val accountId: String,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    
    @ColumnInfo(name = "icon") val icon: Int = R.drawable.ic_box,
    @ColumnInfo(name = "is_read") val isRead: Boolean = false,
)