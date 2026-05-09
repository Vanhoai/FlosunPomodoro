package com.flosun.pomodoro.presentation.map

import android.app.Application
import androidx.lifecycle.ViewModel
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.domain.repositories.CommonRepository
import com.flosun.pomodoro.domain.values.Location
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

    fun initialize(location: Location) {
        _uiState.value = uiState.value.copy(
            selectedAddress = location.address ?: "",
            selectedLatitude = location.latitude,
            selectedLongitude = location.longitude,
        )
    }

    fun listenSearchAddress() = ioRun {
        uiState
            .map { it.searchAddress }
            .debounce(1000L)
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

    fun onSelectedLocation(location: Location) {
        _uiState.value = uiState.value.copy(
            searchAddress = location.address!!,
            selectedAddress = location.address,
            selectedLongitude = location.longitude,
            selectedLatitude = location.latitude,
            searchResults = emptyList(),
        )
    }

    fun onChangedSelectedAddress(address: String) {
        _uiState.value = uiState.value.copy(selectedAddress = address)
    }

    fun onChangedLngLatFromMap(longitude: Double, latitude: Double) = ioRun {
        // Update the selected longitude and latitude in the UI state
        _uiState.value = uiState.value.copy(
            selectedLongitude = longitude,
            selectedLatitude = latitude,
        )

        // Perform reverse geocoding to get the address from the longitude and latitude
        commonRepository.geocodingReverse(
            longitude = longitude,
            latitude = latitude,
        ).collect {
            when (it) {
                is BaseResult.Loading -> {}
                is BaseResult.Failure -> {}
                is BaseResult.Success -> _uiState.update { state ->
                    state.copy(
                        selectedAddress = it.data ?: state.selectedAddress
                    )
                }
            }
        }
    }

}