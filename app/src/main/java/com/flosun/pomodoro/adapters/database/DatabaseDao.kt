package com.flosun.pomodoro.adapters.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.flosun.pomodoro.adapters.database.embedded.SettingWithSounds
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.adapters.database.entities.SoundType
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    // region ======================================= ACCOUNT QUERIES =======================================
    @Query("SELECT * FROM accounts")
    fun findAllAccount(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun findAccountByEmail(email: String): AccountEntity?

    @Query("SELECT * FROM accounts WHERE id = :id LIMIT 1")
    fun findAccountById(id: String): Flow<AccountEntity?>

    @Insert
    fun insertAccount(account: AccountEntity): Long

    @Query("DELETE FROM accounts")
    suspend fun removeAllAccounts(): Int
    // endregion ======================================= ACCOUNT QUERIES =======================================

    // region ======================================= YEAR QUERIES =======================================
    @Query("SELECT * FROM twelve_week_years")
    fun findAllTwelveWeekYears(): Flow<List<TwelveWeekYearEntity>>

    @Query("SELECT * FROM twelve_week_years WHERE id = :id")
    fun findTwelveWeekYearById(id: String): Flow<TwelveWeekYearEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTwelveWeekYear(twelveWeekYear: TwelveWeekYearEntity): Long

    @Update
    fun updateTwelveWeekYear(twelveWeekYear: TwelveWeekYearEntity): Int

    // Find One Year - date in range of start and end time
    @Query(
        """
        SELECT * FROM twelve_week_years 
        WHERE start_time_milliseconds <= :date AND end_time_milliseconds >= :date
        LIMIT 1
        """
    )
    fun findTwelveWeekYearByDate(date: Long): Flow<TwelveWeekYearEntity?>

    @Query("DELETE FROM tasks WHERE goal_id IN (SELECT id FROM goals WHERE week_id IN (SELECT id FROM weeks WHERE year_id = :yearId))")
    suspend fun deleteTasksByYearId(yearId: String): Int

    @Query("DELETE FROM goals WHERE week_id IN (SELECT id FROM weeks WHERE year_id = :yearId)")
    suspend fun deleteGoalsByYearId(yearId: String): Int

    @Query("DELETE FROM weeks WHERE year_id = :yearId")
    suspend fun deleteWeeksByYearId(yearId: String): Int

    @Query("DELETE FROM tasks WHERE lagging_indicator_id IN (SELECT id FROM lagging_indicators WHERE year_id = :yearId)")
    suspend fun deleteTasksByLaggingIndicatorsYearId(yearId: String): Int

    @Query("DELETE FROM lagging_indicators WHERE year_id = :yearId")
    suspend fun deleteLaggingIndicatorsByYearId(yearId: String): Int

    @Query("DELETE FROM twelve_week_years WHERE id = :id")
    suspend fun deleteTwelveWeekYearById(id: String): Int

    // Cascade delete everything for a year
    @Transaction
    suspend fun deleteYearCascade(yearId: String) {
        deleteTasksByYearId(yearId)
        deleteTasksByLaggingIndicatorsYearId(yearId)
        deleteGoalsByYearId(yearId)
        deleteWeeksByYearId(yearId)
        deleteLaggingIndicatorsByYearId(yearId)
        deleteTwelveWeekYearById(yearId)
    }
    // endregion ======================================= YEAR QUERIES =======================================

    // region ======================================= LAGGING INDICATORS QUERIES =======================================
    @Query("SELECT * FROM lagging_indicators WHERE id = :id")
    fun findLaggingIndicatorById(id: String): Flow<LaggingIndicatorEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewLaggingIndicator(laggingIndicator: LaggingIndicatorEntity): Long

    @Update
    fun updateLaggingIndicator(laggingIndicator: LaggingIndicatorEntity): Int

    @Query("DELETE FROM lagging_indicators WHERE id = :id")
    fun deleteLaggingIndicator(id: String): Int

    @Query("DELETE FROM lagging_indicators WHERE id IN (:ids)")
    fun deleteMultiLaggingIndicators(ids: List<String>): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllLaggingIndicators(laggingIndicators: List<LaggingIndicatorEntity>): List<Long>

    @Query("SELECT * FROM lagging_indicators WHERE year_id = :yearId")
    fun findLaggingIndicatorsByYearId(yearId: String): Flow<List<LaggingIndicatorEntity>>
    // endregion ======================================= LAGGING INDICATORS QUERIES =======================================

    // region ======================================= WEEKS QUERIES =======================================
    @Update
    fun updateWeek(week: WeekEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllWeeks(weeks: List<WeekEntity>): List<Long>

    @Query("SELECT * FROM weeks WHERE year_id = :yearId")
    fun findWeeksByYearId(yearId: String): Flow<List<WeekEntity>>

    @Query(
        """
        SELECT * FROM weeks
        WHERE year_id = :yearId AND start_time_milliseconds <= :currentTime AND end_time_milliseconds >= :currentTime
        LIMIT 1
        """
    )
    fun findCurrentWeekByYearId(yearId: String, currentTime: Long): Flow<WeekEntity?>

    @Query(
        """
        SELECT * FROM weeks
        WHERE year_id = :yearId AND start_time_milliseconds <= :startTime
        """
    )
    fun findWeekByYearIdAndBeforeStartTime(yearId: String, startTime: Long): Flow<List<WeekEntity>>

    @Query("SELECT * FROM weeks WHERE id = :weekId")
    fun findWeekById(weekId: String): Flow<WeekEntity?>
    // endregion ======================================= WEEKS QUERIES =======================================

    // region ======================================= GOAL QUERIES =======================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewGoal(goal: GoalEntity): Long

    @Query("SELECT * FROM goals WHERE week_id = :weekId")
    fun findGoalsByWeekId(weekId: String): Flow<List<GoalEntity>>
    // endregion ======================================= GOAL QUERIES =======================================

    // region ======================================= TASK QUERIES =======================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTask(task: TaskEntity): Long

    @Update
    fun updateTask(task: TaskEntity): Int

    @Query(
        """
        SELECT * FROM tasks
        WHERE date = :date AND is_completed = :isCompleted
        """
    )
    fun findTasksByDate(date: Long, isCompleted: Boolean = false): Flow<List<TaskEntity>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE date = :date AND name = :name
        LIMIT 1
        """
    )
    fun findTasksByDateAndName(date: Long, name: String): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun findTaskById(id: String): Flow<TaskEntity?>

    @Query(
        """
        SELECT * FROM tasks
        WHERE id = :id AND date = :date
        LIMIT 1
        """
    )
    fun findTaskByIdAndDate(id: String, date: Long): Flow<TaskEntity?>

    // endregion ======================================= TASK QUERIES =======================================

    // region ======================================= SOUND QUERIES =======================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSound(sound: SoundEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllSounds(sounds: List<SoundEntity>): List<Long>

    @Query("SELECT * FROM sounds WHERE sound_type = :soundType")
    fun findSoundsByType(soundType: SoundType): Flow<List<SoundEntity>>

    @Query("SELECT * FROM sounds WHERE id = :id LIMIT 1")
    fun findSoundById(id: String): Flow<SoundEntity?>
    // endregion ======================================= SOUND QUERIES =======================================

    // region ======================================= SETTING QUERIES =======================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSetting(setting: SettingEntity): Long

    @Update
    fun updateSetting(setting: SettingEntity): Int

    @Query(
        """
        SELECT * FROM settings
        WHERE account_id = :accountId
        LIMIT 1
        """
    )
    fun findSettingByAccountId(accountId: String): Flow<SettingEntity?>

    @Transaction
    @Query("SELECT * FROM settings WHERE account_id = :accountId")
    fun findSettingWithSoundsByAccountId(accountId: String): Flow<SettingWithSounds?>

    @Update
    fun updateSettings(setting: SettingEntity): Int
    // endregion ======================================= SETTING QUERIES =======================================

}