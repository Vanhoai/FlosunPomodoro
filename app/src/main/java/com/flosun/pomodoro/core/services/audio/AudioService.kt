package com.flosun.pomodoro.core.services.audio

import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.flosun.pomodoro.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AudioService : MediaLibraryService(), Player.Listener {

    @Inject
    lateinit var mediaLibrarySessionCallback: MediaLibrarySessionCallback

    private val binder = AudioBinder()
    private val job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var exoplayer: ExoPlayer
    private lateinit var mediaSession: MediaLibrarySession
    override fun onGetSession(p0: MediaSession.ControllerInfo) = mediaSession

    inner class AudioBinder : Binder() {
        val service: AudioService get() = this@AudioService
        val player: ExoPlayer get() = exoplayer
    }

    override fun onBind(intent: Intent?) = super.onBind(intent) ?: binder

    override fun onCreate() {
        super.onCreate()

        exoplayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaLibrarySession.Builder(this, exoplayer, mediaLibrarySessionCallback)
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
        job.cancel()
        mediaSession.release()
        exoplayer.removeListener(this)
        exoplayer.release()

        super.onDestroy()
    }

    fun playQueue(mediaItems: ArrayDeque<MediaItem>, playWhenReady: Boolean = true) {

        scope.launch {
            exoplayer.setMediaItems(mediaItems.toList())
            exoplayer.prepare()
            exoplayer.repeatMode = Player.REPEAT_MODE_ALL
            exoplayer.playWhenReady = playWhenReady
        }

    }

    fun setMediaItem(mediaItem: MediaItem) {
        exoplayer.setMediaItems(listOf(mediaItem))
        exoplayer.prepare()
        exoplayer.repeatMode = Player.REPEAT_MODE_ALL
    }
    
}