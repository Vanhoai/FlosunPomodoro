package com.flosun.pomodoro.ui.theme

import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

object AppTheme {
    private val _isDarkTheme = mutableStateOf(true)
    val isDarkTheme: Boolean
        @Composable
        get() = _isDarkTheme.value

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalColors.current

    val sizing: AppSizing
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalSizing.current

    val typography: AppTypographies
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalTypography.current
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Composable
fun PomodoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { AppColors() }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) colorPalette else colorPalette
        }

        darkTheme -> colorPalette // update dark theme here if needed
        else -> colorPalette // update light theme here if needed
    }

    CompositionLocalProvider(
        LocalSizing provides Sizing,
        LocalTypography provides Typography,
        LocalColors provides colorScheme
    ) {
        ProvideTextStyle(value = Typography.body, content = content)
    }
}