package com.flosunn.core.libraries.permissions.multiple

import android.Manifest
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
import com.flosunn.core.libraries.permissions.single.MutableSinglePermissionManager

@Stable
class MutableMultiplePermissionManager(
    override val permissions: List<String>,
    private val context: Context,
    private val activity: Activity,
) : MultiplePermissionManager {

    private var status: Map<String, PermissionStatus> by mutableStateOf(checkPermissionsStatus())

    override val allPermissionGranted: Boolean
        get() = status.values.all { it is PermissionStatus.Granted }

    override var isDeniedPermanently: Map<String, Boolean> by mutableStateOf(permissions.associateWith { false })

    private var isRequestingPermission by mutableStateOf(false)

    internal var launcher: ActivityResultLauncher<Array<String>>? = null

    fun initialize(isDeniedPermanentlyMap: Map<String, Boolean>) {
        isDeniedPermanently = isDeniedPermanentlyMap
    }

    fun updatePermissionsStatus(onDeniedPermanently: (String, Boolean) -> Unit = { _, _ -> }) {
        status = checkPermissionsStatus()
        status.map {
            if (it.value == PermissionStatus.Granted) {
                isDeniedPermanently = isDeniedPermanently.toMutableMap().apply {
                    this[it.key] = false
                }

                onDeniedPermanently(it.key, false)
            }
        }

        if (isRequestingPermission) {
            status.map {
                if (it.value is PermissionStatus.Denied) {
                    val shouldShowRationale =
                        (it.value as PermissionStatus.Denied).shouldShowRationale
                    val isPermanentlyDenied = !shouldShowRationale

                    isDeniedPermanently = isDeniedPermanently.toMutableMap().apply {
                        this[it.key] = isPermanentlyDenied
                    }

                    onDeniedPermanently(it.key, isPermanentlyDenied)
                }
            }

            isRequestingPermission = false
        }
    }

    fun checkPermissionsStatus(): Map<String, PermissionStatus> {
        return permissions.associateWith { permission ->
            when (ContextCompat.checkSelfPermission(context, permission)) {
                PackageManager.PERMISSION_GRANTED -> PermissionStatus.Granted
                else -> {
                    val shouldShowRationale =
                        activity.shouldShowRequestPermissionRationale(permission)
                    PermissionStatus.Denied(shouldShowRationale)
                }
            }
        }
    }

    override fun requestPermissions() {
        if (launcher == null) throw IllegalStateException("MultiplePermissionManager is not initialized with a launcher")

        isRequestingPermission = true
        launcher!!.launch(permissions.toTypedArray())
    }

    override fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}