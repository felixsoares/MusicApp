package com.mobile.felix.musicapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.data.useCase.GetLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetSongsByTermUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.SaveSongUseCase
import com.mobile.felix.musicapp.feature.home.presentation.action.HomeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
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

    private val pendingActions = MutableSharedFlow<HomeAction>()
    private val _searchQuery = MutableStateFlow("")
    var uiState = MutableStateFlow(HomeUiState())
        private set

    init {
        handlePendingActions()

        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(getLocalSongs())
                } else {
                    flowOf(fetchSongsByTerm(query))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handlePendingActions() {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is HomeAction.Idle -> {}
                    is HomeAction.Search -> onQueryChanged(action.query)
                    is HomeAction.GetLocalSongs -> getLocalSongs()
                    is HomeAction.SaveSong -> saveSong(action.song)
                }
            }
        }
    }

    private fun getLocalSongs() {
        viewModelScope.launch { fetchSongs { getLocalSongsUseCase.invoke() } }
    }

    private fun fetchSongsByTerm(query: String) {
        viewModelScope.launch { fetchSongs { getSongsByTermUseCase.invoke(query) } }
    }

    private suspend fun fetchSongs(call: suspend () -> Result<List<Song>>) {
        uiState.update { it.copy(isLoading = true, isUnknowError = false, isInternetError = false) }

        when (val result = call()) {
            is Result.Success -> uiState.update { it.copy(isLoading = false, songs = result.data) }
            is Result.Error -> uiState.update {
                when (result.failure) {
                    Failure.NetworkError -> it.copy(isLoading = false, isInternetError = true)
                    else -> it.copy(isLoading = false, isUnknowError = true)
                }
            }
        }
    }

    private fun onQueryChanged(query: String) {
        uiState.update { current -> current.copy(query = query) }
        _searchQuery.value = query
    }

    private fun saveSong(song: Song) {
        viewModelScope.launch {
            saveSongUseCase.invoke(song)
        }
    }

    fun submitAction(action: HomeAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}