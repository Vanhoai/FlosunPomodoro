package com.flosun.pomodoro.core.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

@Stable
class MutablePermissionManager(
    override val permission: String,
    private val context: Context,
    private val activity: Activity,
) : PermissionManager {

    override var isDeniedPermanently: Boolean by mutableStateOf(false)
    override var status: PermissionStatus by mutableStateOf(checkPermissionStatus(permission))
    internal var launcher: ActivityResultLauncher<String>? = null

    internal fun updatePermissionStatus() {
        status = checkPermissionStatus(permission)

        // Update isDeniedPermanently based on the new status
        isDeniedPermanently = when (status) {
            is PermissionStatus.Granted -> false
            is PermissionStatus.Denied -> !(status as PermissionStatus.Denied).shouldShowRationale
        }
    }

    override fun checkPermissionStatus(permission: String): PermissionStatus {
        val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
        return if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            PermissionStatus.Granted
        } else {
            val shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
            PermissionStatus.Denied(shouldShowRationale)
        }
    }

    override fun requestPermission() = launcher?.launch(permission)
        ?: throw IllegalStateException("PermissionManager is not initialized with a launcher")

    override fun openAppSettings() {
        val uri = Uri.fromParts("package", activity.packageName, null)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            uri
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        activity.startActivity(intent)
    }
}