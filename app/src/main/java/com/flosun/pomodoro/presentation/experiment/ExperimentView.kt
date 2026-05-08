package com.flosun.pomodoro.presentation.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.experiment.map.MapOptionsBottomSheet
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosunn.core.extensions.rippleEffectClickable
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.StyleState
import org.maplibre.spatialk.geojson.Position
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExperimentView(
    viewModel: ExperimentViewModel = hiltViewModel<ExperimentViewModel>()
) {
    val mapState = rememberMapState()
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .rippleEffectClickable { isExpanded = true },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(200.dp),
            ) {
                MapView(
                    state = mapState,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        MapOptionsBottomSheet(
            state = mapState,
            isExpanded = isExpanded,
            onClose = { isExpanded = false },
        )

    }
}