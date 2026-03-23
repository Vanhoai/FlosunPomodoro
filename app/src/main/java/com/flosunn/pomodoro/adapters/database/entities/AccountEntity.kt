package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val password: String,
)