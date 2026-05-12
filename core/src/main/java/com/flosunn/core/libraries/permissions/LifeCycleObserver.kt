package com.flosunn.core.libraries.permissions

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.flosunn.core.libraries.permissions.multiple.MutableMultiplePermissionManager
import com.flosunn.core.libraries.permissions.single.MutableSinglePermissionManager

@Composable
fun PermissionLifeCycleObserver(
    permissionManager: MutableSinglePermissionManager,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME,
    onDeniedPermanently: (Boolean) -> Unit = {},
) {
    val permissionCheckerObserver = remember(permissionManager) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                // If the permission is revoked, check again.
                // We don't check if the permission was denied as that triggers a process restart.
                permissionManager.updatePermissionStatus(onDeniedPermanently = onDeniedPermanently)
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}


@Composable
fun PermissionsLifeCycleObserver(
    multiplePermissionManager: MutableMultiplePermissionManager,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME,
    onDeniedPermanently: (String, Boolean) -> Unit = { _, _ -> },
) {
    val permissionCheckerObserver = remember(multiplePermissionManager) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                multiplePermissionManager.updatePermissionsStatus(onDeniedPermanently = onDeniedPermanently)
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}