package com.flosun.pomodoro.presentation.graph

import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import kotlinx.serialization.Serializable


@Serializable
sealed class BottomNavRoute(
    val icon: Int,
    val title: String,
) : NavKey {
    @Serializable
    data object Home : BottomNavRoute(
        icon = R.drawable.ic_home,
        title = "Home"
    )

    @Serializable
    data object Tasks : BottomNavRoute(
        icon = R.drawable.ic_tasks,
        title = "Tasks"
    )

    @Serializable
    data object More : BottomNavRoute(
        icon = R.drawable.ic_grid,
        title = "More"
    )

    @Serializable
    data object Calendar : BottomNavRoute(
        icon = R.drawable.ic_calendar,
        title = "Calendar"
    )

    @Serializable
    data object Settings : BottomNavRoute(
        icon = R.drawable.ic_settings,
        title = "Settings"
    )
}

val bottomNavRoutes = listOf(
    BottomNavRoute.Home,
    BottomNavRoute.Tasks,
    BottomNavRoute.More,
    BottomNavRoute.Calendar,
    BottomNavRoute.Settings
)

val bottomBarScreenSaver = Saver<BottomNavRoute, String>(
    save = { it::class.simpleName ?: "" },
    restore = { className ->
        when (className) {
            BottomNavRoute.Home::class.simpleName -> BottomNavRoute.Home
            BottomNavRoute.Tasks::class.simpleName -> BottomNavRoute.Tasks
            BottomNavRoute.Calendar::class.simpleName -> BottomNavRoute.Calendar
            BottomNavRoute.Settings::class.simpleName -> BottomNavRoute.Settings
            else -> BottomNavRoute.Home
        }
    }
)