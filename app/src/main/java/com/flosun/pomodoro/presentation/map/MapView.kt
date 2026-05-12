package com.flosun.pomodoro.presentation.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.result_store.LocalResultStore
import com.flosun.pomodoro.core.utils.result_store.rememberResultStore
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.map.components.LocationSearchResults
import com.flosun.pomodoro.presentation.map.components.MapBottomActions
import com.flosun.pomodoro.ui.components.core.CoreSpinner
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.MapView
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun MapView(
    route: NavRoute.Map,
    viewModel: MapViewModel = hiltViewModel<MapViewModel>(),
) {
    val navBackStack = LocalNavBackStack.current
    val focusManager = LocalFocusManager.current
    val resultStore = LocalResultStore.current

    val mapState = rememberMapState()
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (route.location == null) return@LaunchedEffect

        // Update Initial Map Location
        viewModel.initialize(route.location)

        // Move the map camera to the provided location
        mapState.cameraState.animateTo(
            CameraPosition(
                target = Position(route.location.longitude, route.location.latitude),
                zoom = 14.0,
            ),
            500.milliseconds,
        )

        // Set the selected location on the map
        mapState.selectedLocation = MapClickEvent(
            Position(route.location.longitude, route.location.latitude),
            DpOffset.Zero,
        )
    }

    LaunchedEffect(mapState.selectedLocation) {
        // Listen reverse geocoding when user clicks on the map
        if (mapState.selectedLocation == null) return@LaunchedEffect

        viewModel.onChangedLngLatFromMap(
            mapState.selectedLocation!!.position.longitude,
            mapState.selectedLocation!!.position.latitude,
        )
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            MapView(
                state = mapState,
                modifier = Modifier.fillMaxSize()
            )

            if (isFocused) Box(
                modifier = Modifier
                    .fillMaxSize()
                    .tapGesture { focusManager.clearFocus() },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                CoreTextField(
                    value = uiState.searchAddress,
                    onValueChanged = viewModel::onChangedSearchAddress,
                    placeholder = "e.g. 123 Main St, Springfield",
                    backgroundColor = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    prefix = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color(0xFF6C6C6C),
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    keyboardType = KeyboardType.Text,
                    onFocusChanged = { isFocused = it },
                    suffix = {
                        AnimatedVisibility(uiState.isSearching) {
                            CoreSpinner(
                                modifier = Modifier.size(16.dp),
                                circleColors = listOf(
                                    Color.LightGray,
                                    Color.LightGray,
                                    Color.LightGray,
                                )
                            )
                        }

                        if (uiState.searchAddress.isNotEmpty() && !uiState.isSearching) Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = Color(0xFF6C6C6C),
                            modifier = Modifier
                                .size(16.dp)
                                .rippleEffectClickable {
                                    viewModel.onChangedSearchAddress("")
                                    focusManager.clearFocus()
                                },
                        )
                    },
                )

                LocationSearchResults(
                    locations = uiState.searchResults,
                    onSelectedLocation = { location ->
                        focusManager.clearFocus()
                        viewModel.onSelectedLocation(location)

                        scope.launch {
                            mapState.cameraState.animateTo(
                                CameraPosition(
                                    target = Position(
                                        location.longitude,
                                        location.latitude
                                    ),
                                    zoom = 14.0,
                                ),
                                500.milliseconds,
                            )

                            mapState.selectedLocation = MapClickEvent(
                                Position(
                                    location.longitude,
                                    location.latitude
                                ),
                                DpOffset.Zero,
                            )
                        }
                    }
                )

                MapBottomActions(
                    selectedAddress = uiState.selectedAddress,
                    onChangedSelectedAddress = viewModel::onChangedSelectedAddress,
                    onFocusChanged = { isFocused = it },
                    onBack = { if (navBackStack.isNotEmpty()) navBackStack.removeLastOrNull() },
                    onOk = {
                        // Pass the selected location back to the caller
                        resultStore.set(
                            key = route.key,
                            value = Location(
                                longitude = uiState.selectedLongitude,
                                latitude = uiState.selectedLatitude,
                                address = uiState.selectedAddress,
                            )
                        )

                        // Navigate back after selection
                        if (navBackStack.isNotEmpty()) navBackStack.removeLastOrNull()
                    }
                )
            }
        }
    }
}