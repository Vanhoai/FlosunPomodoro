package com.flosun.pomodoro.presentation.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
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
import com.flosun.pomodoro.App
import com.flosun.pomodoro.presentation.graph.BottomNavRoute
import com.flosun.pomodoro.presentation.graph.BottomTabs
import com.flosun.pomodoro.presentation.graph.bottomBarScreenSaver
import com.flosun.pomodoro.presentation.graph.bottomNavRoutes
import com.flosun.pomodoro.presentation.swipe.calendar.CalendarView
import com.flosun.pomodoro.presentation.swipe.home.HomeView
import com.flosun.pomodoro.presentation.swipe.settings.SettingsView
import com.flosun.pomodoro.presentation.swipe.tasks.TasksView
import com.flosun.pomodoro.ui.components.shared.NotFoundView
import com.flosun.pomodoro.ui.theme.AppTheme
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
        modifier = Modifier.background(Color.Transparent),
        containerColor = Color.Transparent,
        bottomBar = {
            BottomTabs(
                currentBottomBarScreen = currentBottomBarScreen,
                onClick = { destination ->
                    if (backStack.lastOrNull() != destination) {
                        if (backStack.lastOrNull() in bottomNavRoutes)
                            backStack.removeAt(backStack.lastIndex)

                        backStack.add(destination)
                        currentBottomBarScreen = destination
                    }
                }
            )
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