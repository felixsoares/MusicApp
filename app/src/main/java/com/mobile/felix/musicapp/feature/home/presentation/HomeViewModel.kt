package com.mobile.felix.musicapp.feature.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.data.useCase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun fetchSongsByTerm(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            when(val result = homeUseCase.getSongsByTerm(query)) {
                is Result.Success -> _uiState.value = HomeUiState.Data(result.data)
                is Result.Error -> {
                    _uiState.value = when(result.failure) {
                        Failure.NetworkError -> HomeUiState.InternetError
                        else -> HomeUiState.UnknowError
                    }
                }
            }
        }
    }

}