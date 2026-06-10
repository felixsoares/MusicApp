package com.mobile.felix.musicapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.data.useCase.GetSongsByTermUseCase
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

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongsByTermUseCase: GetSongsByTermUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchSongsByTerm("")

        _searchQuery
            .debounce(300)
            .filterNot(String::isEmpty)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .flatMapLatest { query ->
                flowOf(fetchSongsByTerm(query))
            }
            .launchIn(viewModelScope)
    }

    fun fetchSongsByTerm(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
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

}