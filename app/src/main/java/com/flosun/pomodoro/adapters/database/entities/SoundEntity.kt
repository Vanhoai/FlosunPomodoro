package com.flosun.pomodoro.adapters.database.entities

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class SoundType {
    AUDIO,
    TICKING,
    ALARM,
}

@Entity(
    tableName = "sounds",
    indices = [
        Index(value = ["title", "sound_type"], unique = true),
    ]
)
data class SoundEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sound_type") val soundType: SoundType,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "uri") val uri: String,
)

val audios = listOf(
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "Rain",
        uri = "file:///android_asset/audios/rain.mp3",
    ),
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "Forest",
        uri = "file:///android_asset/audios/forest.mp3",
    ),
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "Coffee Shop",
        uri = "file:///android_asset/audios/coffee_shop.mp3",
    ),
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "Fireplace",
        uri = "file:///android_asset/audios/fireplace.mp3",
    ),
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "Ocean Waves",
        uri = "file:///android_asset/audios/ocean_waves.mp3",
    ),
    SoundEntity(
        soundType = SoundType.AUDIO,
        title = "White Noise",
        uri = "file:///android_asset/audios/white_noise.mp3",
    ),
)

val alarm = listOf(
    SoundEntity(
        soundType = SoundType.ALARM,
        title = "Classical Alarm",
        uri = "file:///android_asset/alarm/classical.mp3",
    ),
    SoundEntity(
        soundType = SoundType.ALARM,
        title = "Christmas Gong",
        uri = "file:///android_asset/alarm/christmas_gong.wav",
    ),
    SoundEntity(
        soundType = SoundType.ALARM,
        title = "Clock Bells Hour",
        uri = "file:///android_asset/alarm/clock_bells_hour.wav",
    ),
    SoundEntity(
        soundType = SoundType.ALARM,
        title = "Quick Alarm",
        uri = "file:///android_asset/alarm/quick_ticking.mp3",
    ),
    SoundEntity(
        soundType = SoundType.ALARM,
        title = "Racing Countdown",
        uri = "file:///android_asset/alarm/racing_countdown.wav",
    ),
)

val ticking = listOf(
    SoundEntity(
        soundType = SoundType.TICKING,
        title = "Classic Ticking",
        uri = "file:///android_asset/ticking/classic.mp3",
    ),
)