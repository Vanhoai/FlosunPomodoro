package com.flosun.pomodoro

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.services.LocationService
import com.flosun.pomodoro.core.services.audio.AudioConnection
import com.flosun.pomodoro.core.services.audio.AudioService
import com.flosun.pomodoro.core.services.audio.LocalAudioConnection
import com.flosun.pomodoro.core.services.pomodoro.PomodoroService
import com.flosun.pomodoro.core.utils.result_store.LocalResultStore
import com.flosun.pomodoro.core.utils.result_store.rememberResultStore
import com.flosun.pomodoro.domain.entites.CorrelationEntity
import com.flosun.pomodoro.domain.entites.toMap
import com.flosun.pomodoro.domain.entites.update
import com.flosun.pomodoro.globals.events.DatabaseConsumer
import com.flosun.pomodoro.globals.events.GlobalEventBus
import com.flosun.pomodoro.globals.events.LocalEventBus
import com.flosun.pomodoro.globals.store.GlobalStore
import com.flosun.pomodoro.presentation.graph.NavGraph
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.graph.config
import com.flosun.pomodoro.ui.components.shared.AlertMessageManager
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessageManager
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosun.pomodoro.ui.components.shared.LocalAlertMessageManager
import com.flosun.pomodoro.ui.components.shared.LocalFlashStackedMessageManager
import com.flosun.pomodoro.ui.components.shared.LocalGlobalLoading
import com.flosun.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var database: PomodoroDatabase

    @Inject
    lateinit var globalLoading: GlobalLoading

    @Inject
    lateinit var locationService: LocationService

    @Inject
    lateinit var pomodoroService: PomodoroService

    @Inject
    lateinit var flashStackedMessageManager: FlashStackedMessageManager

    @Inject
    lateinit var alertMessageManager: AlertMessageManager

    @Inject
    lateinit var globalEventBus: GlobalEventBus

    @Inject
    lateinit var databaseConsumer: DatabaseConsumer

    @Inject
    lateinit var globalStore: GlobalStore

    private var audioService: AudioService? = null
    private var audioConnection by mutableStateOf<AudioConnection?>(null)
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            audioService = (binder as AudioService.AudioBinder).service
            audioConnection = AudioConnection(
                context = this@MainActivity,
                binder = binder,
                scope = lifecycleScope,
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioConnection?.dispose()
            audioConnection = null
        }
    }

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalUuidApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        globalStore.init(lifecycleScope)
        setContent {
            val resultStore = rememberResultStore()
            val navBackStack = rememberNavBackStack(
                configuration = config,
                NavRoute.Auth,
            )

            LaunchedEffect(Unit) { databaseConsumer.listenGlobalEvents() }

            CompositionLocalProvider(
                LocalDatabase provides database,
                LocalGlobalLoading provides globalLoading,
                LocalFlashStackedMessageManager provides flashStackedMessageManager,
                LocalAlertMessageManager provides alertMessageManager,
                LocalPomodoroService provides pomodoroService,
                LocalAudioConnection provides audioConnection,
                LocalEventBus provides globalEventBus,
                LocalGlobalStore provides globalStore,
                LocalNavBackStack provides navBackStack,
                LocalResultStore provides resultStore,
            ) {
                PomodoroTheme {
                    AppWrapper {
                        NavGraph(navBackStack = navBackStack)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, AudioService::class.java))
        bindService(
            Intent(this, AudioService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkAndSetCurrentYear()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationService.onDispose()
        pomodoroService.onDispose()
        databaseConsumer.onDispose()

        if (isFinishing) {
            stopService(Intent(this, AudioService::class.java))
            unbindService(serviceConnection)
            audioService = null
        }
    }
}

val LocalNavBackStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("No NavBackStack provided")
}

val LocalPomodoroService = staticCompositionLocalOf<PomodoroService> {
    error("No PomodoroService provided")
}

val LocalGlobalStore = staticCompositionLocalOf<GlobalStore> {
    error("No GlobalStore provided")
}

@Composable
fun rememberNavBackStack(): NavBackStack<NavKey> {
    return LocalNavBackStack.current
}

@Composable
fun rememberPomodoroService(): PomodoroService {
    return LocalPomodoroService.current
}