package com.flosunn.pomodoro.adapters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flosunn.pomodoro.domain.entities.Account

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val password: String,
    val email: String,
) {
    fun toEntity(): Account {
        return Account(
            id = id,
            name = name,
            email = email,
            password = password,
        )
    }

    companion object {
        fun fromEntity(account: Account): AccountEntity {
            return AccountEntity(
                id = account.id,
                name = account.name,
                email = account.email,
                password = account.password,
            )
        }
    }
}