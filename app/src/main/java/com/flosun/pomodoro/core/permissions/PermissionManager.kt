package com.flosun.pomodoro.core.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import timber.log.Timber

@Stable
interface PermissionManager {

    val permission: String

    val isDeniedPermanently: Boolean

    val status: PermissionStatus

    fun checkPermissionStatus(permission: String): PermissionStatus

    fun requestPermission()
    fun openAppSettings()
}

@Composable
fun rememberPermissionManager(
    permission: String,
    onPermissionResult: (granted: Boolean) -> Unit = {},
): PermissionManager {
    val context = LocalContext.current
    val activity = LocalActivity.current
    requireNotNull(activity) { "PermissionManager requires an Activity context" }

    val permissionManager = remember(permission) {
        MutablePermissionManager(permission, context, activity)
    }

    PermissionLifeCycleObserver(permissionManager)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionManager.updatePermissionStatus()
        onPermissionResult(granted)
    }

    DisposableEffect(permissionManager, launcher) {
        permissionManager.launcher = launcher
        onDispose { permissionManager.launcher = null }
    }

    return permissionManager
}