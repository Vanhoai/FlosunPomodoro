package com.flosun.pomodoro.core.services.audio

import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaLibrarySessionCallback @Inject constructor(
    
) : MediaLibraryService.MediaLibrarySession.Callback {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        return super.onConnect(session, controller)
    }

}