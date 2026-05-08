package com.flosun.pomodoro.ui.components.shared.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.StyleState
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.milliseconds


data class MapClickEvent(val position: Position, val offset: DpOffset)

class MapState(
    val cameraState: CameraState,
    val styleState: StyleState,
) {
    var selectedLocation by mutableStateOf<MapClickEvent?>(null)
    var selectedStyle by mutableStateOf<MapStyle>(Protomaps.Light)
    var renderOption by mutableStateOf(RenderOptions.Standard)
    var ornamentOptions by mutableStateOf(OrnamentOptions.AllEnabled)
}

@Composable
fun rememberMapState(): MapState {
    val cameraState = rememberCameraState()
    val styleState = rememberStyleState()

    LaunchedEffect(Unit) {
        cameraState.animateTo(
            CameraPosition(
                target = Position(106.6130917, 10.8395764),
                zoom = 14.0,
            ),
            500.milliseconds,
        )
    }

    val mapState = remember(
        cameraState,
        styleState,
    ) {
        MapState(cameraState, styleState)
    }

    return mapState
}