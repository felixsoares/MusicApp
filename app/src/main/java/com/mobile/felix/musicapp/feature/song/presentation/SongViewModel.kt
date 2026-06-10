package com.mobile.felix.musicapp.feature.song.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.song.data.useCase.GetSongUseCase
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import com.mobile.felix.musicapp.feature.song.presentation.action.SongAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val useCase: GetSongUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<SongAction>()

    var uiState = MutableStateFlow(SongUiState())
        private set

    init {
        handlePendingActions()
        observePlaybackProgress()
    }

    private fun handlePendingActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is SongAction.Idle -> {}
                    is SongAction.Load -> loadSong(action.id)
                    is SongAction.Play -> {
                        audioPlayer.resume()
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Playing)
                        }
                    }

                    is SongAction.Pause -> {
                        audioPlayer.pause()
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Paused)
                        }
                    }

                    is SongAction.Repeat -> {
                        audioPlayer.repeat()
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Playing)
                        }
                    }

                    is SongAction.FastForward -> {
                        audioPlayer.fastForward()
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Playing)
                        }
                    }

                    is SongAction.FastRewind -> {
                        audioPlayer.fastRewind()
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Playing)
                        }
                    }

                    is SongAction.SeekTo -> {
                        audioPlayer.seekTo(action.position)
                        uiState.update { currentState ->
                            currentState.copy(playbackState = PlaybackState.Playing)
                        }
                    }
                }
            }
        }
    }

    private fun loadSong(id: Long) {
        viewModelScope.launch {
            uiState.update { current -> current.copy(isLoading = true, hasError = false) }
            when (val song = useCase.invoke(id)) {
                is Result.Success -> {
                    val song = song.data ?: return@launch

                    val playbackStatus = if (song.songPreview == null) {
                        PlaybackState.Error
                    } else {
                        audioPlayer.play(song.songPreview)
                        PlaybackState.Playing
                    }

                    uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            hasError = false,
                            song = song,
                            playbackState = playbackStatus
                        )
                    }
                }

                is Result.Error -> {
                    uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
            }
        }
    }

    private fun observePlaybackProgress() {
        viewModelScope.launch {
            audioPlayer.currentPositionFlow.collect { positionMs ->
                uiState.update { currentState ->
                    currentState.copy(playbackPosition = positionMs)
                }
            }
        }
    }

    fun submitAction(action: SongAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.pause()
        audioPlayer.release()
    }
}