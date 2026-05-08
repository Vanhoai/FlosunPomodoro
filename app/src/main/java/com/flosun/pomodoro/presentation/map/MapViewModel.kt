package com.flosun.pomodoro.presentation.map

import android.app.Application
import androidx.lifecycle.ViewModel
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.domain.repositories.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    private val commonRepository: CommonRepository,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        listenSearchAddress()
    }

    fun listenSearchAddress() = ioRun {
        uiState
            .map { it.searchAddress }
            .debounce(600L)
            .distinctUntilChanged()
            .collectLatest { address ->
                if (address.isNotEmpty()) {
                    commonRepository.geocodingForward(address).collect {
                        when (it) {
                            is BaseResult.Loading -> _uiState.update { state ->
                                state.copy(
                                    isSearching = true
                                )
                            }

                            is BaseResult.Failure -> {
                                Timber.tag(DEBUG_TAG)
                                    .d("Geocoding forward failure for address: $address, error: ${it.exception}")

                                _uiState.update { state ->
                                    state.copy(
                                        isSearching = false,
                                        searchResults = emptyList()
                                    )
                                }
                            }

                            is BaseResult.Success -> _uiState.update { state ->
                                state.copy(
                                    isSearching = false,
                                    searchResults = it.data
                                )
                            }
                        }
                    }
                } else {
                    _uiState.value = uiState.value.copy(searchResults = emptyList())
                }
            }
    }

    fun onChangedSearchAddress(address: String) {
        _uiState.value = uiState.value.copy(searchAddress = address)
    }

}