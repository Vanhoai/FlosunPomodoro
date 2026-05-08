package com.flosun.pomodoro.presentation.swipe.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.settings.components.SignOutButton
import com.flosun.pomodoro.presentation.swipe.settings.components.groupSettings
import com.flosun.pomodoro.ui.components.shared.SharedSwipeHeading

@Composable
fun SettingsView(navBackStack: NavBackStack<NavKey>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })
        }

        groupSettings(navBackStack)

        item {
            SignOutButton()
            VerticalDivider(modifier = Modifier.height(120.dp))
        }
    }
}