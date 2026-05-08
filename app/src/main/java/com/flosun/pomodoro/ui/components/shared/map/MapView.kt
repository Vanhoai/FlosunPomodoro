package com.flosun.pomodoro.ui.components.shared.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.util.ClickResult


@Composable
fun MapView(
    modifier: Modifier = Modifier,
    state: MapState = rememberMapState(),
) {
    MaplibreMap(
        modifier = modifier,
        styleState = state.styleState,
        cameraState = state.cameraState,
        baseStyle = state.selectedStyle.base,
        onMapClick = { position, offset ->
            state.selectedLocation = MapClickEvent(position, offset)
            ClickResult.Pass
        },
        options = MapOptions(
            renderOptions = state.renderOption,
            ornamentOptions = state.ornamentOptions,
        )
    ) {
        MarkersView(state)
    }
}
