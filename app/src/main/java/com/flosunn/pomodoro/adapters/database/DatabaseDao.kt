package com.flosunn.pomodoro.adapters.database

import androidx.room.Dao
import androidx.room.Query
import com.flosunn.pomodoro.adapters.database.entities.AccountEntity

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM accounts")
    suspend fun findAllAccount(): List<AccountEntity>

}