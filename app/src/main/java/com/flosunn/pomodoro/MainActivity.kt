package com.flosunn.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.pomodoro.adapters.database.LocalDatabase
import com.flosunn.pomodoro.adapters.database.PomodoroDatabase
import com.flosunn.pomodoro.presentation.graph.NavGraph
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            CompositionLocalProvider(
                LocalDatabase provides database,
                LocalGlobalLoading provides globalLoading
            ) {
                PomodoroTheme {
                    GlobalLoading {
                        NavGraph()
                    }
                }
            }
        }
    }
}