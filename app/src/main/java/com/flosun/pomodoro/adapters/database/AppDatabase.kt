package com.flosun.pomodoro.adapters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.NotificationEntity
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.adapters.database.entities.SoundType
import com.flosun.pomodoro.adapters.database.entities.TagEntity
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import kotlinx.serialization.json.Json

@Database(
    entities = [
        SoundEntity::class,
        AccountEntity::class,
        SettingEntity::class,
        NotificationEntity::class,
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
    SoundTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao
}