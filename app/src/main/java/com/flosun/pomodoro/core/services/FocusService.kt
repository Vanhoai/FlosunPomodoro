package com.flosun.pomodoro.core.services

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class FocusState(
    val isFocusModeActive: Boolean = false,
    val blockNotifications: Boolean = false,
    val blockPhoneCalls: Boolean = false,
    val blockOtherApps: Boolean = false,
)

@Singleton
class FocusService @Inject constructor(application: Application) {
    private val notificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val _focusState = MutableStateFlow(FocusState())
    val focusState = _focusState.asStateFlow()

    fun startFocusMode() {
        // 1. Block Notifications
        if (focusState.value.blockNotifications) blockNotifications()

        // 2. Block Phone Calls
        if (focusState.value.blockPhoneCalls) blockIncomingCalls()

        // 3. Block Other Apps
        if (focusState.value.blockOtherApps) blockOtherAppsAccess()
    }

    fun stopFocusMode() {
        _focusState.value = FocusState(isFocusModeActive = false)
        restoreNotifications()
    }

    private fun blockNotifications() {
        try {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun restoreNotifications() {
        try {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun blockIncomingCalls() {
        // Require permission: ANSWER_PHONE_CALLS
    }

    private fun blockOtherAppsAccess() {}

}