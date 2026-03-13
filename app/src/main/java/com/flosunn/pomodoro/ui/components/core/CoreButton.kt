package com.flosunn.pomodoro.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun CoreButton(
    modifier: Modifier = Modifier,
    height: Int = 60,
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
    Button(
        onClick = onPress,
        modifier = modifier
            .height(height.dp)
            .fillMaxWidth()
            .background(brush, shape = RoundedCornerShape(AppTheme.sizing.borderMedium)),
        shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = AppTheme.colors.typographyColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        enabled = isEnabled && !isLoading,
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