package com.flosunn.pomodoro.presentation.swipe.settings.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun NotificationSettingsView(navBackStack: NavBackStack<NavKey>) {
    Scaffold(
        containerColor = AppTheme.colors.backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(
                onBack = { navBackStack.removeLastOrNull() },
                title = "Notification"
            )
        }
    }
}