package com.flosun.pomodoro

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.alarm
import com.flosun.pomodoro.adapters.database.entities.audios
import com.flosun.pomodoro.adapters.database.entities.ticking
import com.flosun.pomodoro.core.constants.CURRENT_YEAR_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
) : BaseViewModel(application) {

    init {
        insertDefaultSounds()
    }

    fun checkAndSetCurrentYear() = ioRun {
        val now = System.currentTimeMillis()
        database.findTwelveWeekYearByDate(now).collect {
            if (it != null) appStorage.write(CURRENT_YEAR_ID_KEY, it.id)
            else appStorage.remove(CURRENT_YEAR_ID_KEY)
        }
    }

    fun isUriAccessible(assetPath: String): Boolean {
        return try {
            val relativePath = if (assetPath.startsWith("file:///android_asset/")) {
                assetPath.removePrefix("file:///android_asset/")
            } else {
                assetPath
            }

            application.applicationContext.assets.open(relativePath).use { true }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    // Insert Default Sounds
    fun insertDefaultSounds() = ioRun {
        // Validate uri
        var isValidUri = true

        for (sound in audios + alarm + ticking) {
            val uri = sound.uri
            if (!isUriAccessible(uri)) {
                Timber.tag(DEBUG_TAG).e("URI not accessible: $uri")
                isValidUri = false
                break
            }
        }

        if (!isValidUri) {
            showToast("Failed to load default sounds. Please check your app installation.")
            return@ioRun
        }

        database.addAllSounds(audios)
        database.addAllSounds(alarm)
        database.addAllSounds(ticking)
    }
}