package com.flosun.pomodoro.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun CoreButton(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = AppTheme.sizing.borderMedium,
    height: Dp = 52.dp,
    brush: Brush = Brush.horizontalGradient(
        colors = listOf(
            AppTheme.colors.primaryColor,
            AppTheme.colors.primaryColor,
        )
    ),
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    onPress: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = if (isEnabled) brush else Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE9E9E9),
                        Color(0xFFE9E9E9),
                    )
                ),
            )
            .rippleEffectClickable { if (isEnabled && !isLoading) onPress() },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) CoreSpinner(
                modifier = Modifier.size(24.dp),
                circleColors = listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                ),
            )
            else content()
        }
    }
}