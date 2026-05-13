package com.flosun.pomodoro.adapters.database

import androidx.room.TypeConverter
import com.flosun.pomodoro.adapters.database.entities.SoundType
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

class SoundTypeConverter {

    @TypeConverter
    fun fromString(value: String): SoundType {
        return SoundType.valueOf(value)
    }

    @TypeConverter
    fun fromEnum(enum: SoundType): String {
        return enum.name
    }

}
