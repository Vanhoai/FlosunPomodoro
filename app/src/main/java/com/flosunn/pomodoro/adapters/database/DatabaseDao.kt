package com.flosunn.pomodoro.adapters.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flosunn.pomodoro.adapters.database.entities.AccountEntity
import com.flosunn.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    // region ================================ ACCOUNT QUERIES ================================
    @Query("SELECT * FROM accounts")
    fun findAllAccount(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun findAccountByEmail(email: String): AccountEntity?

    @Insert
    fun insertAccount(account: AccountEntity): Long

    @Query("DELETE FROM accounts")
    suspend fun removeAllAccounts(): Int
    // endregion ================================ ACCOUNT QUERIES ================================


    // region ================================ POMODORO QUERIES ================================
    @Query("SELECT * FROM twelve_week_years")
    fun findAllTwelveWeekYears(): Flow<List<TwelveWeekYearEntity>>

    @Query("SELECT * FROM twelve_week_years WHERE id = :id")
    fun findTwelveWeekYearById(id: Long): Flow<TwelveWeekYearEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTwelveWeekYear(twelveWeekYear: TwelveWeekYearEntity): Long

    // endregion ================================ POMODORO QUERIES ================================
}