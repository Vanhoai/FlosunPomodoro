package com.flosun.pomodoro.presentation.experiment

import android.app.Application
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.services.LocationService
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.domain.values.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

data class ExperimentUiState(
    val address: String = "Unknown",
    val location: Location? = null,
)

@HiltViewModel
class ExperimentViewModel @Inject constructor(
    private val application: Application,
    private val locationService: LocationService,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(ExperimentUiState())
    val uiState: StateFlow<ExperimentUiState> = _uiState.asStateFlow()

    init {
        initialize()
    }

    fun initialize() = ioRun {
        try {
            val location = locationService.retrieveLastLocation()
            _uiState.update { it.copy(location = location) }

            Timber.tag(DEBUG_TAG).d("Location: $location")
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}