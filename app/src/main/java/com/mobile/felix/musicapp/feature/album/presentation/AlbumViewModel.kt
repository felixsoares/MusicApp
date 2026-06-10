package com.mobile.felix.musicapp.feature.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.data.useCase.GetAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase
) : ViewModel() {

    var uiState = MutableStateFlow<AlbumState>(AlbumState.Loading)
        private set

    fun getAlbumById(albumId: Long) {
        viewModelScope.launch {
            uiState.value = AlbumState.Loading
            when (val result = getAlbumUseCase.invoke(albumId = albumId)) {
                is Result.Success -> uiState.value = AlbumState.Data(result.data)
                is Result.Error -> {
                    uiState.value = when (result.failure) {
                        Failure.NetworkError -> AlbumState.InternetError
                        else -> AlbumState.UnknowError
                    }
                }
            }
        }
    }

}