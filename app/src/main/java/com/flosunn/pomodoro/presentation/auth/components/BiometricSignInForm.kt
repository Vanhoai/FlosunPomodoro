package com.flosunn.pomodoro.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavGraph
import com.flosunn.pomodoro.ui.theme.AppTheme
import com.flosunn.pomodoro.ui.theme.PomodoroTheme

@Composable
fun BiometricSignInForm(
    authWithBiometric: () -> Unit = {},
    authWithFaceRecognition: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .background(Color.White.copy(alpha = 0.8f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = authWithBiometric,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_biometric),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF979797),
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .background(Color.White.copy(alpha = 0.8f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = authWithFaceRecognition,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_face),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF979797),
            )
        }
    }
}