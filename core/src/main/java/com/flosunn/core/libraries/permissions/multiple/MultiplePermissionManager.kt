package com.flosunn.core.libraries.permissions.multiple

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.flosunn.core.libraries.datastore.rememberPreference
import com.flosunn.core.libraries.permissions.PermissionLifeCycleObserver
import com.flosunn.core.libraries.permissions.PermissionStatus
import com.flosunn.core.libraries.permissions.PermissionsLifeCycleObserver
import com.flosunn.core.libraries.permissions.single.MutableSinglePermissionManager
import com.flosunn.core.libraries.permissions.single.SinglePermissionManager

@Stable
interface MultiplePermissionManager {

    val permissions: List<String>

    val allPermissionGranted: Boolean

    val isDeniedPermanently: Map<String, Boolean>

    val isAllDeniedPermanently: Boolean
        get() = isDeniedPermanently.values.all { it }

    fun requestPermissions()

    fun openAppSettings()

}

@Composable
fun rememberMultiplePermissionManager(
    permissions: List<String>,
    onPermissionResults: (Map<String, Boolean>) -> Unit = {},
): MultiplePermissionManager {
    val context = LocalContext.current
    val activity = LocalActivity.current
    requireNotNull(activity) { "PermissionManager requires an Activity context" }

    val keys = permissions.associateWith { permission ->
        booleanPreferencesKey("IsDeniedPermanently_$permission")
    }

    val isDeniedPermanentlyMap = keys.mapValues { (_, key) ->
        rememberPreference(
            key = key,
            defaultValue = false,
        )
    }

    val multiplePermissionManager = remember(
        permissions,
        context,
        activity
    ) {
        MutableMultiplePermissionManager(permissions, context, activity)
    }

    PermissionsLifeCycleObserver(
        multiplePermissionManager = multiplePermissionManager,
        onDeniedPermanently = { permission, isDeniedPermanently ->
            isDeniedPermanentlyMap[permission]?.value = isDeniedPermanently
        }
    )

    LaunchedEffect(Unit) {
        multiplePermissionManager.initialize(isDeniedPermanentlyMap.mapValues { it.value.value })
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onPermissionResults(it)
        multiplePermissionManager.updatePermissionsStatus { key, value ->
            Log.d("PomodoroDebug", "Permission: $key, Denied Permanently: $value")
            isDeniedPermanentlyMap[key]?.value = value
        }
    }

    DisposableEffect(launcher) {
        multiplePermissionManager.launcher = launcher
        onDispose { multiplePermissionManager.launcher = null }
    }

    return multiplePermissionManager
}