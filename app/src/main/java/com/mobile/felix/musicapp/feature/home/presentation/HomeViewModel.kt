package com.mobile.felix.musicapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.data.useCase.ClearLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetHomeSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetSongsByTermUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.SaveSongUseCase
import com.mobile.felix.musicapp.feature.home.presentation.action.HomeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongsByTermUseCase: GetSongsByTermUseCase,
    private val saveSongUseCase: SaveSongUseCase,
    private val getLocalSongsUseCase: GetLocalSongsUseCase,
    private val clearLocalSongsUseCase: ClearLocalSongsUseCase,
    getHomeSongsUseCase: GetHomeSongsUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<HomeAction>()
    private val _searchQuery = MutableStateFlow("")
    var uiState = MutableStateFlow(HomeUiState())
        private set

    val pagedSongsFlow: Flow<PagingData<Song>> = getHomeSongsUseCase
        .invoke()
        .cachedIn(viewModelScope)

    init {
        handlePendingActions()
        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    getLocalSongs()
                    clearLocalSongsUseCase.invoke()
                } else {
                    getSongsByTerm(query)
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
                    is HomeAction.SaveSong -> saveSong(action.trackId)
                }
            }
        }
    }

    private fun getLocalSongs() {
        viewModelScope.launch {
            uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isUnknowError = false,
                    isInternetError = false
                )
            }

            when (val result = getLocalSongsUseCase.invoke()) {
                is Result.Success -> uiState.update {
                    it.copy(
                        isLoading = false,
                        songs = result.data
                    )
                }

                is Result.Error -> handleErrors(result.failure)
            }
        }
    }

    private fun getSongsByTerm(query: String) {
        viewModelScope.launch {
            uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isUnknowError = false,
                    isInternetError = false
                )
            }

            when (val result = getSongsByTermUseCase.invoke(query)) {
                is Result.Success -> {
                    uiState.update { current -> current.copy(isLoading = false) }
                }

                is Result.Error -> handleErrors(result.failure)
            }
        }
    }

    private fun handleErrors(failure: Failure) {
        uiState.update { current ->
            when (failure) {
                Failure.NetworkError -> current.copy(isLoading = false, isInternetError = true)
                else -> current.copy(isLoading = false, isUnknowError = true)
            }
        }
    }

    private fun onQueryChanged(query: String) {
        uiState.update { current -> current.copy(query = query) }
        _searchQuery.value = query
    }

    private fun saveSong(trackId: Long) = viewModelScope.launch {
        saveSongUseCase.invoke(trackId)
    }

    fun submitAction(action: HomeAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}