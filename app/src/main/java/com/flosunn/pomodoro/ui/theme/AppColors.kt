package com.flosunn.pomodoro.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    primaryColor: Color = Color(0xFFFF474A),
    backgroundColor: Color = Color(0xFFF5F5F5),
    typographyColor: Color = Color(0xFF383838),
    isAnimated: Boolean = false,
    borderColor: Color = Color(0xFFEFEFEF),
) {
    private var isAnimated by mutableStateOf(isAnimated)

    private var _primaryColor by mutableStateOf(primaryColor)
    private var _backgroundColor by mutableStateOf(backgroundColor)
    private var _typographyColor by mutableStateOf(typographyColor)

    private var _borderColor by mutableStateOf(borderColor)

    val primaryColor: Color
        @Composable
        get() = if (isAnimated) getPrimaryAnimatedColor() else _primaryColor

    val backgroundColor: Color
        @Composable
        get() = if (isAnimated) getBackgroundAnimatedColor() else _backgroundColor

    val typographyColor: Color
        @Composable
        get() = if (isAnimated) getTypographyAnimatedColor() else _typographyColor

    val borderColor: Color
        @Composable
        get() = _borderColor

    @Composable
    private fun getPrimaryAnimatedColor(): Color {
        return animateColorAsState(
            label = "PrimaryColorAnimation",
            targetValue = _primaryColor,
            animationSpec = tween(durationMillis = 500)
        ).value
    }

    @Composable
    private fun getBackgroundAnimatedColor(): Color {
        return animateColorAsState(
            label = "BackgroundColorAnimation",
            targetValue = _backgroundColor,
            animationSpec = tween(durationMillis = 500)
        ).value
    }

    @Composable
    private fun getTypographyAnimatedColor(): Color {
        return animateColorAsState(
            label = "TypographyColorAnimation",
            targetValue = _typographyColor,
            animationSpec = tween(durationMillis = 500)
        ).value
    }
}

val LocalColors = staticCompositionLocalOf<AppColors> {
    error("Colors not initialized")
}