package com.flosun.pomodoro.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

@Immutable
data class AppSizing(
    val borderSmall: Dp,
    val borderMedium: Dp,
    val borderLarge: Dp,
    val spacingSmall: Dp,
    val spacingMedium: Dp,
    val spacingLarge: Dp,
)

val Sizing = AppSizing(
    borderSmall = Dp(4f),
    borderMedium = Dp(8f),
    borderLarge = Dp(12f),
    spacingSmall = Dp(4f),
    spacingMedium = Dp(8f),
    spacingLarge = Dp(16f),
)

inline val LocalSizing
    get() = staticCompositionLocalOf { Sizing }