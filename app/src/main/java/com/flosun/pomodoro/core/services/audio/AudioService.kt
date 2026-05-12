package com.flosun.pomodoro.core.services.audio

import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.flosun.pomodoro.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class AudioService : MediaLibraryService(), Player.Listener {

    @Inject
    lateinit var mediaLibrarySessionCallback: MediaLibrarySessionCallback

    private val binder = AudioBinder()
    private val job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)
    lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaLibrarySession
    override fun onGetSession(p0: MediaSession.ControllerInfo) = mediaSession

    inner class AudioBinder : Binder() {
        fun getService() = this@AudioService
        fun isPlaying() = exoPlayer.isPlaying
    }

    override fun onBind(intent: Intent?) = super.onBind(intent) ?: binder

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaLibrarySession.Builder(this, exoPlayer, mediaLibrarySessionCallback)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        exoPlayer.release()
        mediaSession.release()
    }
}