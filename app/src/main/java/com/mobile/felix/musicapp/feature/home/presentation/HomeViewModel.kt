package com.mobile.felix.musicapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.data.useCase.GetLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetSongsByTermUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.SaveSongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongsByTermUseCase: GetSongsByTermUseCase,
    private val saveSongUseCase: SaveSongUseCase,
    private val getLocalSongsUseCase: GetLocalSongsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getLocalSongs()

        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .flatMapLatest { query ->
                flowOf(fetchSongsByTerm(query))
            }
            .launchIn(viewModelScope)
    }

    fun getLocalSongs() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            when (val result = getLocalSongsUseCase.invoke()) {
                is Result.Success -> _uiState.value = HomeUiState.Data(result.data)
                is Result.Error -> {
                    _uiState.value = when (result.failure) {
                        Failure.NetworkError -> HomeUiState.InternetError
                        else -> HomeUiState.UnknowError
                    }
                }
            }
        }
    }

    fun fetchSongsByTerm(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            if(query.isBlank()) {
                getLocalSongs()
                return@launch
            }

            when (val result = getSongsByTermUseCase.invoke(query)) {
                is Result.Success -> _uiState.value = HomeUiState.Data(result.data)
                is Result.Error -> {
                    _uiState.value = when (result.failure) {
                        Failure.NetworkError -> HomeUiState.InternetError
                        else -> HomeUiState.UnknowError
                    }
                }
            }
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun saveSong(song: Song) {
        viewModelScope.launch {
            saveSongUseCase.invoke(song)
        }
    }
}