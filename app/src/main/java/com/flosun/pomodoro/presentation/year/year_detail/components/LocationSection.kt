package com.flosun.pomodoro.presentation.year.year_detail.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosunn.core.extensions.rippleEffectClickable
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LocationSection(
    address: String,
    longitude: Double,
    latitude: Double,
) {
    val mapState = rememberMapState()
    LaunchedEffect(Unit) {
        mapState.selectedLocation = null

        // Move camera and update map click
        mapState.cameraState.animateTo(
            CameraPosition(
                target = Position(longitude, latitude),
                zoom = 14.0,
            ),
            500.milliseconds,
        )

        mapState.selectedLocation = MapClickEvent(
            Position(longitude, latitude),
            DpOffset.Zero,
        )
    }

    Text(
        text = "Review",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp),
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
            isEnabledMapClick = false,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .padding(12.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.3f))
                .rippleEffectClickable {},
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
        onValueChanged = {},
        placeholder = "e.g. Central Park, New York",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp),
        isEnabled = false,
        maxLines = 2,
        singleLine = false,
        height = 80.dp,
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