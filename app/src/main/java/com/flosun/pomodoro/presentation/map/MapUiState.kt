package com.flosun.pomodoro.presentation.map

import com.flosun.pomodoro.domain.values.Location

data class MapUiState(
    val searchAddress: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<Location> = emptyList(),
    val selectedAddress: String = "",
    val selectedLongitude: Double = 0.0,
    val selectedLatitude: Double = 0.0,
)