package com.flosun.pomodoro

import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.services.LocationService
import com.flosun.pomodoro.core.utils.result_store.LocalResultStore
import com.flosun.pomodoro.core.utils.result_store.rememberResultStore
import com.flosun.pomodoro.presentation.graph.NavGraph
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.graph.config
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosun.pomodoro.ui.components.shared.LocalGlobalLoading
import com.flosun.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var database: PomodoroDatabase

    @Inject
    lateinit var globalLoading: GlobalLoading

    @Inject
    lateinit var locationService: LocationService

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

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
                NavRoute.Experiment
            )

            val resultStore = rememberResultStore()

            CompositionLocalProvider(
                LocalDatabase provides database,
                LocalGlobalLoading provides globalLoading,
                LocalNavBackStack provides navBackStack,
                LocalResultStore provides resultStore,
            ) {
                PomodoroTheme {
                    GlobalLoading {
                        NavGraph(navBackStack = navBackStack)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkAndSetCurrentYear()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationService.onDispose()
    }
}

val LocalNavBackStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("No NavBackStack provided")
}