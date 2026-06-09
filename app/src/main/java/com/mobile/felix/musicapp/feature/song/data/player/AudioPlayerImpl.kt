package com.mobile.felix.musicapp.feature.song.data.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import com.mobile.felix.musicapp.feature.song.presentation.PlaybackState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class AudioPlayerImpl @Inject constructor(
    @ApplicationContext context: Context
) : AudioPlayer {

    private val exoPlayer = ExoPlayer.Builder(context).build()

    override val currentPositionFlow: Flow<Long> = flow {
        while (currentCoroutineContext().isActive) {
            if (exoPlayer.isPlaying || exoPlayer.playbackState == Player.STATE_BUFFERING) {
                emit(exoPlayer.currentPosition)
            }
            delay(1.seconds)
        }
    }

    override fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.playWhenReady = false
        exoPlayer.seekTo(positionMs)
        exoPlayer.playWhenReady = true
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun fastForward() {
        val current = exoPlayer.currentPosition
        if (current + 10000 < exoPlayer.duration) {
            seekTo(current + 10000)
        }
    }

    override fun fastRewind() {
        val current = exoPlayer.currentPosition
        if (current - 10000 > 0) {
            seekTo(current - 10000)
        }
    }

    override fun repeat() {
        exoPlayer.seekTo(0)
    }

    override fun resume() {
        exoPlayer.play()
    }
}