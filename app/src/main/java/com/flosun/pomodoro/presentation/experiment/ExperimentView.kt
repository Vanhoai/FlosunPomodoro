package com.flosun.pomodoro.presentation.experiment

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessages
import com.flosun.pomodoro.ui.components.shared.Message
import com.flosun.pomodoro.ui.components.shared.MessageCard
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosun.pomodoro.ui.components.shared.rememberAlertMessageManager
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.permissions.isGranted
import com.flosunn.core.libraries.permissions.multiple.rememberMultiplePermissionManager
import com.flosunn.core.libraries.permissions.single.rememberSinglePermissionManager
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.StyleState
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExperimentView(
    viewModel: ExperimentViewModel = hiltViewModel<ExperimentViewModel>()
) {
    val multiplePermissionManager = rememberMultiplePermissionManager(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onPermissionResults = { permissionResults ->
            Timber.tag(DEBUG_TAG).d("Permission Results: $permissionResults")
        }
    )
    val alertMessageManager = rememberAlertMessageManager()

    fun onPress() {
        Timber
            .tag(DEBUG_TAG)
            .d("Button pressed, checking permissions")

        Timber
            .tag(DEBUG_TAG)
            .d("Is Denied Permanently: ${multiplePermissionManager.isDeniedPermanently}")

        if (multiplePermissionManager.isAllDeniedPermanently) {
            alertMessageManager.showMessage(
                title = "Notification",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
                onConfirm = { multiplePermissionManager.openAppSettings() },
            )
            return
        }

        if (!multiplePermissionManager.allPermissionGranted) {
            multiplePermissionManager.requestPermissions()
            return
        }

        Timber.tag(DEBUG_TAG).d("Permission granted, proceed with the action")
    }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primaryColor)
                    .rippleEffectClickable {},
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_grid),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            CoreButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onPress = ::onPress,
            ) {
                Text(
                    text = "Add Message",
                    color = Color.White,
                )
            }
        }
    }
}