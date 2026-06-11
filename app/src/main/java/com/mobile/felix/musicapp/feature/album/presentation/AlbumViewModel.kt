package com.mobile.felix.musicapp.feature.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.data.useCase.GetAlbumUseCase
import com.mobile.felix.musicapp.feature.album.presentation.action.AlbumAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<AlbumAction>()

    var uiState = MutableStateFlow(AlbumState())
        private set

    init {
        handlePendingActions()
    }

    private fun handlePendingActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is AlbumAction.SearchAlbum -> getAlbumById(action.albumId)
                }
            }
        }
    }

    private fun getAlbumById(albumId: Long) {
        viewModelScope.launch {
            uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isUnknowError = false,
                    isInternetError = false
                )
            }
            when (val result = getAlbumUseCase.invoke(albumId = albumId)) {
                is Result.Success -> uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        songs = result.data
                    )
                }

                is Result.Error -> uiState.update { current ->
                    when (result.failure) {
                        Failure.NetworkError -> current.copy(
                            isLoading = false,
                            isInternetError = true
                        )

                        else -> current.copy(isLoading = false, isUnknowError = true)
                    }
                }
            }
        }
    }

    fun submitAction(action: AlbumAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}