package com.mobile.felix.musicapp.feature.song.domain.player

import kotlinx.coroutines.flow.Flow

interface AudioPlayer {
    val currentPositionFlow: Flow<Long>
    fun play(url: String)
    fun pause()
    fun seekTo(positionMs: Long)
    fun release()
    fun fastForward()
    fun fastRewind()
    fun resume()
    fun repeat()
}