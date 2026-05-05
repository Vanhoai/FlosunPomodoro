package com.flosun.pomodoro.ui.components.shared

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.flosun.pomodoro.ui.components.core.CoreSpinner
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.cos
import kotlin.math.sin


@Singleton
class GlobalLoading @Inject constructor() {
    private val _isLoading = mutableStateOf(false)
    private val _message = mutableStateOf("Loading")

    val isLoading: Boolean
        get() = _isLoading.value

    val message: String
        get() = _message.value

    fun setLoading(isLoading: Boolean, message: String? = null) {
        _isLoading.value = isLoading
        if (message != null) _message.value = message
    }
}

val LocalGlobalLoading = compositionLocalOf<GlobalLoading> { error("Not Found Global Loading") }

@Composable
fun rememberGlobalLoading(): GlobalLoading {
    return LocalGlobalLoading.current
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GlobalLoading(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val globalLoading = rememberGlobalLoading()

    val scale by animateFloatAsState(
        targetValue = if (globalLoading.isLoading) 1.0f else 0.6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = "LoadingScaleAnimation"
    )

    Box(modifier = modifier.fillMaxSize()) {
        content()
        AnimatedVisibility(
            visible = globalLoading.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    }
                    .zIndex(Float.MAX_VALUE),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(screenWidth)
                        .height(160.dp)
                        .scale(scale)
                        .padding(horizontal = 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CoreSpinner(
                        circleColors = listOf(
                            Color(0xFF828282),
                            Color(0xFF828282),
                            Color(0xFF828282),
                        ),
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = globalLoading.message,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
