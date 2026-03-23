package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visions")
data class VisionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "account_id") val accountId: String,
    val title: String,
)