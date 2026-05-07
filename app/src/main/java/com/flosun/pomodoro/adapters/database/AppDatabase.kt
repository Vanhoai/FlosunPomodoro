package com.flosun.pomodoro.adapters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.TagEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

class StringArrayConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return json.encodeToString(list)
    }
}

class JsonConverter {

    @TypeConverter
    fun fromString(value: String): Map<String, String> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return json.encodeToString(map)
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
        TagEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    StringArrayConverter::class,
    JsonConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao
}