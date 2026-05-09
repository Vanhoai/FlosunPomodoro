package com.flosun.pomodoro.presentation.experiment

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
import com.flosun.pomodoro.presentation.experiment.map.MapOptionsBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessages
import com.flosun.pomodoro.ui.components.shared.Message
import com.flosun.pomodoro.ui.components.shared.MessageCard
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
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
val message = Message(
    id = Uuid.toString(),
    title = "Event has been created",
    description = "Sunday, December 24, 2026 at 09:00 AM"
)

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExperimentView(
    viewModel: ExperimentViewModel = hiltViewModel<ExperimentViewModel>()
) {
    val density = LocalDensity.current

    var isExpanded by remember { mutableStateOf(false) }
    var messages by remember {
        mutableStateOf(
            listOf(
                message,
                message.copy(id = Uuid.toString()),
                message.copy(id = Uuid.toString()),
                message.copy(id = Uuid.toString()),
                message.copy(id = Uuid.toString()),
            )
        )
    }

    val animatedOffsetY by animateDpAsState(
        targetValue = if (isExpanded) 20.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "AnimatedOffsetY"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isExpanded) 0.9f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "AnimatedScaleX"
    )

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
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
                onPress = {
//                    val newMessage = Message(
//                        title = "New Login Detected",
//                        description = "Your account was accessed from a new device. If this wasn't you, please secure your account immediately.",
//                    )
//
//                    messages = messages + newMessage.copy(id = Uuid.random().toString())

                    isExpanded = !isExpanded
                },
            ) {
                Text(
                    text = "Add Message",
                    color = Color.White,
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                MessageCard(
                    message = message,
                    modifier = Modifier
                        .padding(20.dp)
                        .offset(y = animatedOffsetY)
                        .scale(scaleX = animatedScale, scaleY = animatedScale)
                )

                FlashStackedMessages(messages = emptyList())
            }
        }
    }
}