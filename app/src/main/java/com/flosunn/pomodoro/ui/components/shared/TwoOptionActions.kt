package com.flosunn.pomodoro.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.ui.components.core.CoreButton

@Composable
fun TwoOptionActions(
    modifier: Modifier = Modifier,
    okLabel: String = "OK",
    cancelLabel: String = "Cancel",
    onOk: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CoreButton(
            modifier = Modifier.weight(1f),
            onPress = onOk,
        ) {
            Text(
                text = okLabel,
                fontSize = 16.sp,
                color = Color.White,
            )
        }

        CoreButton(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFFCACA),
                    Color(0xFFFFCACA)
                )
            ),
            modifier = Modifier.weight(1f),
            onPress = onCancel,
        ) {
            Text(
                text = cancelLabel,
                fontSize = 16.sp,
                color = Color(0xFFFF6767),
            )
        }
    }
}