package com.flosun.pomodoro.adapters.database.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.SoundEntity

data class SettingWithSounds(
    @Embedded val setting: SettingEntity,
    @Relation(
        parentColumn = "focus_ticking",
        entityColumn = "id"
    )
    val focusTickingSound: SoundEntity?,
    @Relation(
        parentColumn = "short_break_ticking",
        entityColumn = "id"
    )
    val shortBreakTickingSound: SoundEntity?,
    @Relation(
        parentColumn = "long_break_ticking",
        entityColumn = "id"
    )
    val longBreakTickingSound: SoundEntity?,
    @Relation(
        parentColumn = "focus_alarm",
        entityColumn = "id"
    )
    val focusAlarmSound: SoundEntity?,
    @Relation(
        parentColumn = "short_break_alarm",
        entityColumn = "id"
    )
    val shortBreakAlarmSound: SoundEntity?,
    @Relation(
        parentColumn = "long_break_alarm",
        entityColumn = "id"
    )
    val longBreakAlarmSound: SoundEntity?,
) {
    fun isAnySoundNull() = focusTickingSound == null
            || shortBreakTickingSound == null
            || longBreakTickingSound == null
            || focusAlarmSound == null
            || shortBreakAlarmSound == null
            || longBreakAlarmSound == null
}