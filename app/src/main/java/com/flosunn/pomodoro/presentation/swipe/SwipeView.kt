package com.flosunn.pomodoro.presentation.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.flosunn.pomodoro.App
import com.flosunn.pomodoro.presentation.graph.BottomNavRoute
import com.flosunn.pomodoro.presentation.graph.bottomBarScreenSaver
import com.flosunn.pomodoro.presentation.graph.bottomNavRoutes
import com.flosunn.pomodoro.presentation.swipe.calendar.CalendarView
import com.flosunn.pomodoro.presentation.swipe.home.HomeView
import com.flosunn.pomodoro.presentation.swipe.settings.SettingsView
import com.flosunn.pomodoro.presentation.swipe.tasks.TasksView
import com.flosunn.pomodoro.ui.components.shared.NotFoundView
import com.flosunn.pomodoro.ui.theme.AppTheme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun SwipeView(navBackStack: NavBackStack<NavKey>) {

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(BottomNavRoute.Home::class, BottomNavRoute.Home.serializer())
                    subclass(BottomNavRoute.Tasks::class, BottomNavRoute.Tasks.serializer())
                    subclass(BottomNavRoute.Calendar::class, BottomNavRoute.Calendar.serializer())
                    subclass(BottomNavRoute.Settings::class, BottomNavRoute.Settings.serializer())
                }
            }
        },
        BottomNavRoute.Home
    )

    var currentBottomBarScreen: BottomNavRoute by rememberSaveable(
        stateSaver = bottomBarScreenSaver
    ) { mutableStateOf(BottomNavRoute.Home) }

    Scaffold(
        modifier = Modifier.background(AppTheme.colors.backgroundColor),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundColor)
            ) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(60.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                        ),
                    containerColor = Color.Transparent,
                ) {
                    bottomNavRoutes.forEach { destination ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        if (backStack.lastOrNull() != destination) {
                                            if (backStack.lastOrNull() in bottomNavRoutes)
                                                backStack.removeAt(backStack.lastIndex)

                                            backStack.add(destination)
                                            currentBottomBarScreen = destination
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = destination.icon),
                                contentDescription = destination.title,
                                tint = if (currentBottomBarScreen == destination) AppTheme.colors.primaryColor else Color(
                                    0xFF8C8C8C
                                ),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier
                .padding(paddingValues)
                .background(AppTheme.colors.backgroundColor),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = { key ->
                when (key) {
                    is BottomNavRoute.Home -> NavEntry(key) { HomeView(navBackStack) }
                    is BottomNavRoute.Tasks -> NavEntry(key) { TasksView(navBackStack) }
                    is BottomNavRoute.Calendar -> NavEntry(key) { CalendarView(navBackStack) }
                    is BottomNavRoute.Settings -> NavEntry(key) { SettingsView(navBackStack) }
                    else -> NavEntry(key) { NotFoundView() }
                }
            }
        )
    }
}