package com.flosun.pomodoro.presentation.map

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.core.CoreSpinner
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosunn.core.extensions.tapGesture

@Composable
fun MapView(
    viewModel: MapViewModel = hiltViewModel<MapViewModel>()
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .tapGesture { focusManager.clearFocus() },
            contentAlignment = Alignment.TopCenter,
        ) {

            MapView(
                modifier = Modifier
                    .fillMaxSize()
                    .tapGesture { focusManager.clearFocus() }
            )

            CoreTextField(
                value = uiState.searchAddress,
                onValueChanged = viewModel::onChangedSearchAddress,
                placeholder = "e.g. 123 Main St, Springfield",
                backgroundColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                prefix = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color(0xFF6C6C6C),
                        modifier = Modifier.size(24.dp),
                    )
                },
                keyboardType = KeyboardType.Text,
                suffix = {
                    if (uiState.isSearching) CoreSpinner(
                        modifier = Modifier.size(16.dp),
                        circleColors = listOf(
                            Color.LightGray,
                            Color.LightGray,
                            Color.LightGray,
                        )
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                uiState.searchResults.forEach { location ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = location.address!!,
                            color = Color(0xFF333333),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                }
            }
        }
    }
}