package com.flosun.pomodoro.adapters.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
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


    // region ================================ YEAR QUERIES ================================
    @Query("SELECT * FROM twelve_week_years")
    fun findAllTwelveWeekYears(): Flow<List<TwelveWeekYearEntity>>

    @Query("SELECT * FROM twelve_week_years WHERE id = :id")
    fun findTwelveWeekYearById(id: String): Flow<TwelveWeekYearEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTwelveWeekYear(twelveWeekYear: TwelveWeekYearEntity): Long

    // Find One Year - date in range of start and end time
    @Query(
        """
        SELECT * FROM twelve_week_years 
        WHERE start_time_milliseconds <= :date AND end_time_milliseconds >= :date
        LIMIT 1
        """
    )
    fun findTwelveWeekYearByDate(date: Long): Flow<TwelveWeekYearEntity?>
    // endregion ================================ YEAR QUERIES ================================

    // region ================================ LAGGING INDICATORS QUERIES ================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllLaggingIndicators(laggingIndicators: List<LaggingIndicatorEntity>): List<Long>

    @Query("SELECT * FROM lagging_indicators WHERE year_id = :yearId")
    fun findLaggingIndicatorsByYearId(yearId: String): Flow<List<LaggingIndicatorEntity>>
    // endregion ================================ LAGGING INDICATORS QUERIES ================================

    // region ================================ WEEKS QUERIES ================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllWeeks(weeks: List<WeekEntity>): List<Long>

    @Query("SELECT * FROM weeks WHERE year_id = :yearId")
    fun findWeeksByYearId(yearId: String): Flow<List<WeekEntity>>

    @Query("SELECT * FROM weeks WHERE id = :weekId")
    fun findWeekById(weekId: String): Flow<WeekEntity?>
    // endregion ================================ WEEKS QUERIES ================================

    // region ================================ GOALS QUERIES ================================
    @Query("SELECT * FROM goals WHERE week_id = :weekId")
    fun findGoalsByWeekId(weekId: String): Flow<List<GoalEntity>>

    // endregion ================================ GOALS QUERIES ================================
}