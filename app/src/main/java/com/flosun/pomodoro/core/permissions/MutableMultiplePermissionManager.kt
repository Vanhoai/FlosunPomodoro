package com.flosun.pomodoro.core.permissions

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Stable
class MutableMultiplePermissionsManager(
    private val mutablePermissions: List<MutablePermissionManager>
) : MultiplePermissionManager {

    override val permissions: List<PermissionManager> = mutablePermissions
    override val isDeniedPermanently: Boolean by derivedStateOf {
        permissions.any { it.isDeniedPermanently }
    }

    internal var launcher: ActivityResultLauncher<Array<String>>? = null

    override val allPermissionGranted: Boolean by derivedStateOf {
        permissions.all { it.status.isGranted }
    }

    override val shouldShowRationale: Boolean by derivedStateOf {
        permissions.any { it.status.shouldShowRationale } &&
                permissions.none { !it.status.isGranted && !it.status.shouldShowRationale }
    }

    internal fun updatePermissionsStatus(permissionResults: Map<String, Boolean>) {
        for (permission in permissionResults.keys) {
            mutablePermissions.firstOrNull { it.permission == permission }?.apply {
                permissionResults[permission]?.let {
                    this.updatePermissionStatus()
                }
            }
        }
    }

    override fun requestPermissions() = launcher?.launch(
        permissions.map { it.permission }.toTypedArray()
    ) ?: throw IllegalStateException("MultiplePermissionManager is not initialized with a launcher")

    override fun openAppSettings() {
        permissions.forEach { it.openAppSettings() }
    }
}

@Composable
fun rememberMultiplePermissionManager(
    permissions: List<String>,
    onPermissionResults: (Map<String, Boolean>) -> Unit = { _ -> }
): MutableMultiplePermissionsManager {
    val mutablePermissions = rememberMutablePermissionsManager(permissions)
    PermissionsLifeCycleObserver(mutablePermissions)

    val multiplePermissionsManager = remember(permissions) {
        MutableMultiplePermissionsManager(mutablePermissions)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        multiplePermissionsManager.updatePermissionsStatus(results)
        onPermissionResults(results)
    }

    DisposableEffect(multiplePermissionsManager, launcher) {
        multiplePermissionsManager.launcher = launcher
        onDispose { multiplePermissionsManager.launcher = null }
    }

    return multiplePermissionsManager
}

@Composable
fun rememberMutablePermissionsManager(
    permissions: List<String>,
): List<MutablePermissionManager> {
    val context = LocalContext.current
    val activity = LocalActivity.current
    requireNotNull(activity) { "MultiplePermissionManager requires an Activity context" }

    val mutablePermissions: List<MutablePermissionManager> = remember(permissions) {
        return@remember permissions.map { MutablePermissionManager(it, context, activity) }
    }

    for (permissionManager in mutablePermissions) {
        key(permissionManager.permission) {
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                permissionManager.updatePermissionStatus()
            }
        }
    }

    return mutablePermissions
}