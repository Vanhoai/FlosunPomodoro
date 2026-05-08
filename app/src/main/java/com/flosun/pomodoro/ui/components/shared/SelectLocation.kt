package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosunn.core.extensions.rippleEffectClickable
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
    val navBackStack = LocalNavBackStack.current
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
        contentAlignment = Alignment.TopEnd,
    ) {
        MapView(
            state = mapState,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .padding(12.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black.copy(alpha = 0.3f))
                .rippleEffectClickable { navBackStack.add(NavRoute.Map) },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fullscreen),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
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