package com.flosunn.pomodoro.adapters.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flosunn.pomodoro.adapters.database.entities.AccountEntity

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM accounts")
    suspend fun findAllAccount(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun findAccountByEmail(email: String): AccountEntity?

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Query("DELETE FROM accounts")
    suspend fun removeAllAccounts(): Int

}