package com.flosun.pomodoro.adapters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import kotlinx.serialization.json.Json

class StringArrayConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        val json = Json { ignoreUnknownKeys = true }
        return json.encodeToString(list)
    }
}

@Database(
    entities = [
        AccountEntity::class,
        SettingEntity::class,
        TwelveWeekYearEntity::class,
        LaggingIndicatorEntity::class,
        WeekEntity::class,
        GoalEntity::class,
        TaskEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    StringArrayConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao
}