package com.flosunn.core.extensions

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.dashed(strokeWidth: Dp, color: Color, cornerRadius: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.tapGesture(key1: Any? = Unit, key2: Any? = null, onTap: () -> Unit): Modifier =
    composed {
        pointerInput(key1, key2) {
            detectTapGestures {
                onTap()
            }
        }
    }

@Composable
fun Modifier.rippleEffectClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = ripple(),
    enabled = enabled,
    onClick = onClick
)

fun Modifier.noRippleEffectClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled,
        ) {
            onClick()
        }
    }


fun Modifier.cropVertical(padding: Dp): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val paddingPx = padding.roundToPx()

    // Reduce the height of the layout by twice the padding (top and bottom)
    layout(placeable.width, placeable.height - paddingPx * 2) {
        // Position the content slightly higher to "swallow" the top padding
        placeable.placeRelative(0, -paddingPx)
    }
}