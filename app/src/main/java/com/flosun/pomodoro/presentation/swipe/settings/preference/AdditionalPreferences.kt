package com.flosun.pomodoro.presentation.swipe.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.components.shared.RowSwitch
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun AdditionalPreferences() {
    var isPreventScreenLock by remember { mutableStateOf(false) }
    var isVibrateInSilentMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color.White),
    ) {
        RowNavigation(
            title = "Auto-Reset Pomodoro",
            description = "00:00 AM",
            onPress = {}
        )


        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )


        RowSwitch(
            title = "Prevent Screen Lock",
            isActive = isPreventScreenLock,
            onChange = { isPreventScreenLock = it }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )


        RowSwitch(
            title = "Vibrate in Silent Mode",
            isActive = isVibrateInSilentMode,
            onChange = { isVibrateInSilentMode = it }
        )
    }
}