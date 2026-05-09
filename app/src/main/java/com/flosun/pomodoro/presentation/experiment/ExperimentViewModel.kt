package com.flosun.pomodoro.presentation.experiment

import android.app.Application
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ExperimentUiState(
    val messages: List<String> = emptyList(),
)

@HiltViewModel
class ExperimentViewModel @Inject constructor(
    private val application: Application,
) : BaseViewModel(application) {

    private val _uiState = MutableStateFlow(ExperimentUiState())
    val uiState: StateFlow<ExperimentUiState> = _uiState.asStateFlow()
    
}