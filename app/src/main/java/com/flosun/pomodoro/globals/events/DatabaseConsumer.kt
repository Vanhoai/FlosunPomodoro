package com.flosun.pomodoro.globals.events

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.SettingEntity
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.adapters.database.entities.SoundType
import com.flosun.pomodoro.core.services.CoroutineService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseConsumer @Inject constructor(
    application: Application,
    private val globalEventBus: GlobalEventBus,
    private val database: PomodoroDatabase,
) : CoroutineService(application) {

    fun listenGlobalEvents() = ioRun {
        globalEventBus.globalEvent.collect { event ->
            when (event) {
                is GlobalEvent.CreateSetting -> createSetting(accountId = event.accountId)
                else -> {}
            }
        }
    }

    private fun createSetting(accountId: String) = ioRun {
        val alarmSounds = findSounds(SoundType.ALARM)
        val tickingSounds = findSounds(SoundType.TICKING)

        if (alarmSounds.isEmpty() || tickingSounds.isEmpty())
            throw IllegalStateException("No alarm or ticking sounds found in the database")

        val defaultAlarmSound = alarmSounds.first()
        val defaultTickingSound = tickingSounds.first()

        val newSetting = SettingEntity(
            accountId = accountId,
            focusTicking = defaultTickingSound.id,
            shortBreakTicking = defaultTickingSound.id,
            longBreakTicking = defaultTickingSound.id,
            focusAlarm = defaultAlarmSound.id,
            shortBreakAlarm = defaultAlarmSound.id,
            longBreakAlarm = defaultAlarmSound.id,
        )

        database.addSetting(newSetting)
    }

    private suspend fun findSounds(type: SoundType): List<SoundEntity> {
        val sounds = database.findSoundsByType(type).first()
        return sounds
    }

}