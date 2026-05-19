package com.flosun.pomodoro.core.services.audio

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.STATE_ENDED
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.extensions.togglePlayPause
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

val LocalAudioConnection = staticCompositionLocalOf<AudioConnection?> {
    error("No AudioConnection provided")
}

@Composable
fun rememberAudioConnection(): AudioConnection? {
    return LocalAudioConnection.current
}

class AudioConnection(
    context: Context,
    binder: AudioService.AudioBinder,
    scope: CoroutineScope,
) : Player.Listener {
    private val audioService = binder.service
    private val player = binder.player

    // States Flow
    val playbackState = MutableStateFlow(player.playbackState)
    private val playWhenReady = MutableStateFlow(player.playWhenReady)
    val isPlaying = combine(playbackState, playWhenReady) { playbackState, playWhenReady ->
        playWhenReady && playbackState != STATE_ENDED
    }.stateIn(
        scope,
        SharingStarted.Lazily,
        player.playWhenReady && player.playbackState != STATE_ENDED
    )

    val shuffleModeEnabled = MutableStateFlow(false)
    val repeatMode = MutableStateFlow(REPEAT_MODE_OFF)

    val exception = MutableStateFlow<PlaybackException?>(null)

    init {
        player.addListener(this)

        playbackState.value = player.playbackState
        playWhenReady.value = player.playWhenReady
        shuffleModeEnabled.value = player.shuffleModeEnabled
        repeatMode.value = player.repeatMode
    }

    // ==================================== FUNCTIONALITY ====================================
    fun playMediaItem(mediaItem: MediaItem, playWhenReady: Boolean = true) {
        audioService.playQueue(ArrayDeque(listOf(mediaItem)), playWhenReady)
    }

    fun playSoundEvent(
        mediaItem: MediaItem,
        onFinished: () -> Unit = {},
    ) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == STATE_ENDED) {
                    onFinished()
                    player.removeListener(this)
                }
            }
        }

        player.addListener(listener)
        audioService.playSound(ArrayDeque(listOf(mediaItem)))
    }

    fun setMediaItem(mediaItem: MediaItem) = audioService.setMediaItem(mediaItem)

    fun togglePlayPause() = player.togglePlayPause()

    // ==================================== PLAYER LISTENER ====================================
    override fun onPlaybackStateChanged(state: Int) {
        playbackState.value = state
        exception.value = player.playerError
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        this.playWhenReady.value = playWhenReady
    }

    fun dispose() = player.removeListener(this)
}