package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.services.Location
import com.flosun.pomodoro.presentation.experiment.map.MapClickEvent
import com.flosun.pomodoro.presentation.experiment.map.MapView
import com.flosun.pomodoro.presentation.experiment.map.rememberMapState
import com.flosun.pomodoro.ui.components.core.CoreTextField
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber

@Composable
fun SelectLocation(
    address: String,
    longitude: Double? = null,
    latitude: Double? = null,
    onChangedAddress: (String) -> Unit = {},
    onChangedLngLat: (Double, Double) -> Unit = { _, _ -> },
) {
    val mapState = rememberMapState()

    LaunchedEffect(mapState.selectedLocation) {
        if (mapState.selectedLocation == null) return@LaunchedEffect
        onChangedLngLat(
            mapState.selectedLocation!!.position.longitude,
            mapState.selectedLocation!!.position.latitude,
        )
    }

    LaunchedEffect(Unit) {
        if (longitude == null || latitude == null) return@LaunchedEffect

        // Initialize the map with the provided location
        mapState.selectedLocation = MapClickEvent(
            position = Position(longitude, latitude),
            offset = DpOffset(0.dp, 0.dp),
        )
    }

    Text(
        text = "Location (Optional)",
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        MapView(
            state = mapState,
            modifier = Modifier.fillMaxSize(),
        )
    }

    CoreTextField(
        value = address,
        onValueChanged = onChangedAddress,
        placeholder = "e.g. Central Park, New York",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
    )
}