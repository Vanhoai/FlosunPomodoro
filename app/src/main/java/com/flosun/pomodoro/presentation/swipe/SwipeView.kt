package com.flosun.pomodoro.presentation.swipe

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.flosun.pomodoro.App
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.presentation.graph.BottomNavRoute
import com.flosun.pomodoro.presentation.graph.BottomTabs
import com.flosun.pomodoro.presentation.graph.bottomBarScreenSaver
import com.flosun.pomodoro.presentation.graph.bottomNavRoutes
import com.flosun.pomodoro.presentation.swipe.calendar.CalendarView
import com.flosun.pomodoro.presentation.swipe.home.HomeView
import com.flosun.pomodoro.presentation.swipe.settings.SettingsView
import com.flosun.pomodoro.presentation.swipe.tasks.TasksView
import com.flosun.pomodoro.ui.components.shared.NotFoundView
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import timber.log.Timber

private val configuration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(BottomNavRoute.Home::class, BottomNavRoute.Home.serializer())
            subclass(BottomNavRoute.Tasks::class, BottomNavRoute.Tasks.serializer())
            subclass(BottomNavRoute.Calendar::class, BottomNavRoute.Calendar.serializer())
            subclass(BottomNavRoute.Settings::class, BottomNavRoute.Settings.serializer())
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SwipeView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: SwipeViewModel = hiltViewModel<SwipeViewModel>(),
) {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(
        configuration = configuration,
        BottomNavRoute.Home
    )

    var currentBottomBarScreen: BottomNavRoute by rememberSaveable(
        stateSaver = bottomBarScreenSaver
    ) { mutableStateOf(BottomNavRoute.Home) }

    // Shader Setup
    val shaderCode = context.assets.open("shaders/liquid.glsl").readBytes().decodeToString()
    val shader = remember { RuntimeShader(shaderCode) }

    var pointerId by remember { mutableIntStateOf(0) }
    var composableSize by remember { mutableStateOf(Size.Zero) }
    var currentTimeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (true) {
            withFrameNanos { frameTime ->
                currentTimeSeconds = (frameTime - startTime) / 1_000_000_000f
            }

            // Cleanup using seconds instead of millis
            viewModel.cleanupWaves(currentTimeSeconds)
            delay(16) // 60fps limit
        }
    }

    val waves by viewModel.waves.collectAsState()
    val currentShaderParams = viewModel.buildShaderUniforms(currentTimeSeconds)

    fun onOpenAIAssistant(offset: Offset) {
        pointerId++
        viewModel.addWave(offset, pointerId, currentTimeSeconds)
    }

    SideEffect {
        if (composableSize.width > 0f && composableSize.height > 0f) {
            shader.setFloatUniform("uResolution", composableSize.width, composableSize.height)
            shader.setFloatUniform("uTime", currentTimeSeconds)
            shader.setFloatUniform("uGlobalDamping", currentShaderParams.globalDamping)
            shader.setFloatUniform(
                "uMinAmplitudeThreshold",
                currentShaderParams.minAmplitudeThreshold
            )
            shader.setIntUniform("uNumWaves", currentShaderParams.numWaves)
            shader.setFloatUniform("uWaveOrigins", currentShaderParams.origins)
            shader.setFloatUniform("uWaveAmplitudes", currentShaderParams.amplitudes)
            shader.setFloatUniform("uWaveFrequencies", currentShaderParams.frequencies)
            shader.setFloatUniform("uWaveSpeeds", currentShaderParams.speeds)
            shader.setFloatUniform("uWaveStartTimes", currentShaderParams.startTimes)
        }
    }

    val renderEffect = RenderEffect.createRuntimeShaderEffect(
        shader,
        "uContent"
    ).asComposeRenderEffect()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { composableSize = it.toSize() }
            .graphicsLayer { this.renderEffect = renderEffect }
    ) {
        Scaffold(
            modifier = Modifier.background(Color.Transparent),
            containerColor = Color.Transparent,
            bottomBar = {
                BottomTabs(
                    currentBottomBarScreen = currentBottomBarScreen,
                    onClick = { destination ->
                        if (backStack.lastOrNull() != destination) {
                            if (backStack.lastOrNull() in bottomNavRoutes)
                                backStack.removeAt(backStack.lastIndex)

                            backStack.add(destination)
                            currentBottomBarScreen = destination
                        }
                    },
                    onOpenAIAssistant = ::onOpenAIAssistant,
                )
            }
        ) { paddingValues ->
            NavDisplay(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(AppTheme.colors.backgroundColor),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = { key ->
                    when (key) {
                        is BottomNavRoute.Home -> NavEntry(key) { HomeView(navBackStack) }
                        is BottomNavRoute.Tasks -> NavEntry(key) { TasksView(navBackStack) }
                        is BottomNavRoute.Calendar -> NavEntry(key) { CalendarView(navBackStack) }
                        is BottomNavRoute.Settings -> NavEntry(key) { SettingsView(navBackStack) }
                        else -> NavEntry(key) { NotFoundView() }
                    }
                }
            )
        }

        
    }

}