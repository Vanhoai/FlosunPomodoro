package com.flosunn.core.libraries.permissions.single

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
import com.flosunn.core.libraries.permissions.PermissionStatus


@Stable
class MutableSinglePermissionManager(
    override val permission: String,
    private val context: Context,
    private val activity: Activity,
) : SinglePermissionManager {

    override var status by mutableStateOf(checkPermissionStatus(permission))
    override var isDeniedPermanently: Boolean by mutableStateOf(false)

    internal var launcher: ActivityResultLauncher<String>? = null
    internal var isRequestingPermission by mutableStateOf(false)

    internal fun updatePermissionStatus(onDeniedPermanently: (Boolean) -> Unit = {}) {
        status = checkPermissionStatus(permission)
        if (status == PermissionStatus.Granted) {
            isDeniedPermanently = false
            onDeniedPermanently(false)
        }

        if (isRequestingPermission) {
            isDeniedPermanently = when (status) {
                PermissionStatus.Granted -> false
                is PermissionStatus.Denied -> !(status as PermissionStatus.Denied).shouldShowRationale
            }

            onDeniedPermanently(isDeniedPermanently)
            isRequestingPermission = false
        }
    }

    fun initialize(isDeniedPermanently: Boolean) {
        this.isDeniedPermanently = isDeniedPermanently
    }

    override fun requestPermission() {
        if (launcher == null) throw IllegalStateException("PermissionManager is not initialized with a launcher")

        isRequestingPermission = true
        launcher!!.launch(permission)
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

    override fun openAppSettings() {
        val uri = Uri.fromParts("package", activity.packageName, null)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            uri
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

}