package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.MapState
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber

@Composable
fun SelectLocation(
    mapState: MapState,
    locationKey: String,
    address: String,
    longitude: Double? = null,
    latitude: Double? = null,
    onChangedAddress: (String) -> Unit = {},
    onChangedLngLat: (Double, Double) -> Unit = { _, _ -> },
    onUseCurrentLocation: () -> Unit = {},
) {
    val navBackStack = LocalNavBackStack.current

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Location",
            fontSize = 18.sp,
            modifier = Modifier
        )

        Text(
            text = "use current location",
            fontSize = 16.sp,
            color = AppTheme.colors.primaryColor,
            modifier = Modifier.tapGesture { onUseCurrentLocation() },
        )
    }

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
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.3f))
                .rippleEffectClickable {
                    navBackStack.add(
                        NavRoute.Map(
                            key = locationKey,
                            location = Location(
                                longitude = longitude ?: 0.0,
                                latitude = latitude ?: 0.0,
                                address = address,
                            )
                        )
                    )
                },
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
            .padding(top = 12.dp),
        prefix = {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
                tint = Color(0xFF454545),
                modifier = Modifier.size(24.dp),
            )
        }
    )
}