package com.flosun.pomodoro.core.permissions

import androidx.compose.runtime.Stable

@Stable
interface MultiplePermissionManager {

    val permissions: List<PermissionManager>
    val allPermissionGranted: Boolean
    val isDeniedPermanently: Boolean
    val shouldShowRationale: Boolean

    fun requestPermissions()
    fun openAppSettings()

}