package com.flosunn.core.libraries.permissions.single

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.flosunn.core.libraries.datastore.datastore
import com.flosunn.core.libraries.datastore.rememberPreference
import com.flosunn.core.libraries.permissions.PermissionLifeCycleObserver
import com.flosunn.core.libraries.permissions.PermissionStatus

@Stable
interface SinglePermissionManager {

    val permission: String

    val isDeniedPermanently: Boolean

    val status: PermissionStatus

    fun checkPermissionStatus(permission: String): PermissionStatus

    fun requestPermission()

    fun openAppSettings()

}

@Composable
fun rememberSinglePermissionManager(
    permission: String,
    onPermissionResult: (granted: Boolean) -> Unit = {},
): SinglePermissionManager {
    val context = LocalContext.current
    val activity = LocalActivity.current
    requireNotNull(activity) { "PermissionManager requires an Activity context" }

    val key = booleanPreferencesKey("IsDeniedPermanently_$permission")
    var isDeniedPermanently by rememberPreference(
        key = key,
        defaultValue = false,
    )

    val singlePermissionManager = remember(activity, context, permission) {
        MutableSinglePermissionManager(
            permission = permission,
            context = context,
            activity = activity,
        )
    }

    // Initialize the permission manager with the persisted "denied permanently" status.
    LaunchedEffect(Unit) { singlePermissionManager.initialize(isDeniedPermanently) }

    PermissionLifeCycleObserver(
        singlePermissionManager,
        onDeniedPermanently = {
            isDeniedPermanently = it
        },
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        singlePermissionManager.updatePermissionStatus(onDeniedPermanently = {
            isDeniedPermanently = it
        })
        onPermissionResult(granted)
    }

    DisposableEffect(singlePermissionManager, launcher) {
        singlePermissionManager.launcher = launcher
        onDispose { singlePermissionManager.launcher = null }
    }

    return singlePermissionManager
}