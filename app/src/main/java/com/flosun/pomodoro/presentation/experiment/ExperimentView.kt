package com.flosun.pomodoro.presentation.experiment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.uuid.ExperimentalUuidApi


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UseOfNonLambdaOffsetOverload", "AutoboxingStateCreation")
@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExperimentView(viewModel: ExperimentViewModel = hiltViewModel<ExperimentViewModel>()) {
    val context = LocalContext.current
    var fabPositionOnScreen by remember { mutableStateOf(Offset.Zero) }
    var pointerId by remember { mutableIntStateOf(100) }

    // Shader Setup
    val shaderCode = context.assets.open("shaders/wave.glsl").readBytes().decodeToString()
    val shader = remember { RuntimeShader(shaderCode) }

    var composableSize by remember { mutableStateOf(Size.Zero) }
    var currentTimeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        // while (true) {
        //     withFrameNanos { frameTime ->
        //         currentTimeSeconds = (frameTime - startTime) / 1_000_000_000f
        //     }
        //
        //     // Cleanup using seconds instead of millis
        //     viewModel.cleanupWaves(currentTimeSeconds)
        //     delay(16) // 60fps limit
        // }
    }

//    val waves by viewModel.waves.collectAsState()
//    val currentShaderParams = viewModel.buildShaderUniforms(currentTimeSeconds)

    SideEffect {
        // if (composableSize.width > 0f && composableSize.height > 0f) {
        //     shader.setFloatUniform("uResolution", composableSize.width, composableSize.height)
        //     shader.setFloatUniform("uTime", currentTimeSeconds)
        //     shader.setFloatUniform("uGlobalDamping", currentShaderParams.globalDamping)
        //     shader.setFloatUniform(
        //         "uMinAmplitudeThreshold",
        //         currentShaderParams.minAmplitudeThreshold
        //     )
        //     shader.setIntUniform("uNumWaves", currentShaderParams.numWaves)
        //     shader.setFloatUniform("uWaveOrigins", currentShaderParams.origins)
        //     shader.setFloatUniform("uWaveAmplitudes", currentShaderParams.amplitudes)
        //     shader.setFloatUniform("uWaveFrequencies", currentShaderParams.frequencies)
        //     shader.setFloatUniform("uWaveSpeeds", currentShaderParams.speeds)
        //     shader.setFloatUniform("uWaveStartTimes", currentShaderParams.startTimes)
        // }
    }

    val renderEffect = RenderEffect.createRuntimeShaderEffect(
        shader,
        "uTexture"
    ).asComposeRenderEffect()

    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primaryColor)
                    .onGloballyPositioned { coords ->
                        val rect = coords.boundsInRoot()
                        fabPositionOnScreen = rect.center
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            val realOffset = fabPositionOnScreen - Offset(
                                20.dp.toPx(),
                                20.dp.toPx(),
                            )

//                            viewModel.addWave(
//                                realOffset,
//                                pointerId++,
//                                currentTimeSeconds
//                            )
                        })
                    },
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
                .padding(paddingValues)
                .onSizeChanged { composableSize = it.toSize() }
                .graphicsLayer { this.renderEffect = renderEffect },
            contentAlignment = Alignment.Center,
        ) {
            CoreButton(onPress = {}) {
                Text(
                    text = "AI Assistant",
                    fontSize = 16.sp,
                    color = Color.White,
                )
            }
        }
    }
}