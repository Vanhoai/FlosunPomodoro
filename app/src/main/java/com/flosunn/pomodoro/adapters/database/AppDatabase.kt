package com.flosunn.pomodoro.adapters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flosunn.pomodoro.adapters.database.entities.AccountEntity
import com.flosunn.pomodoro.adapters.database.entities.SessionEntity
import com.flosunn.pomodoro.adapters.database.entities.SettingEntity
import com.flosunn.pomodoro.adapters.database.entities.TaskEntity
import com.flosunn.pomodoro.adapters.database.entities.VisionEntity

@Database(
    entities = [
        AccountEntity::class,
        SettingEntity::class,
        VisionEntity::class,
        TaskEntity::class,
        SessionEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao
}