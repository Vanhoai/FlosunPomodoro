package com.flosunn.pomodoro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.flosunn.pomodoro.adapters.database.LocalDatabase
import com.flosunn.pomodoro.adapters.database.PomodoroDatabase
import com.flosunn.pomodoro.presentation.graph.NavGraph
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.presentation.graph.config
import com.flosunn.pomodoro.ui.components.shared.GlobalLoading
import com.flosunn.pomodoro.ui.components.shared.LocalGlobalLoading
import com.flosunn.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var database: PomodoroDatabase

    @Inject
    lateinit var globalLoading: GlobalLoading

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val navBackStack = rememberNavBackStack(
                configuration = config,
                NavRoute.AddNewYear
            )

            CompositionLocalProvider(
                LocalDatabase provides database,
                LocalGlobalLoading provides globalLoading,
                LocalNavBackStack provides navBackStack,
            ) {
                PomodoroTheme {
                    GlobalLoading {
                        NavGraph(navBackStack = navBackStack)
                    }
                }
            }
        }
    }
}

val LocalNavBackStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("No NavBackStack provided")
}