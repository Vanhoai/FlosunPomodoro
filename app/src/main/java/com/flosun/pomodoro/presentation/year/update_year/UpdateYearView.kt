package com.flosun.pomodoro.presentation.year.update_year

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.ResultStoreKeys
import com.flosun.pomodoro.core.utils.result_store.rememberLocalResultStore
import com.flosun.pomodoro.core.utils.result_store.rememberResultStore
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.UploadCover
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.DurationSetting
import com.flosun.pomodoro.ui.components.shared.LaggingIndicators
import com.flosun.pomodoro.ui.components.shared.RewardSection
import com.flosun.pomodoro.ui.components.shared.SelectLocation
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.components.shared.UpdateReview
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosunn.core.extensions.tapGesture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

private val contracts = ActivityResultContracts.PickVisualMedia()

@Composable
fun UpdateYearView(
    navRoute: NavRoute.UpdateYear,
    viewModel: UpdateYearViewModel = hiltViewModel<UpdateYearViewModel>(),
) {
    val navBackStack = LocalNavBackStack.current
    val focusManager = LocalFocusManager.current

    val resultStore = rememberLocalResultStore()
    val scope = rememberCoroutineScope()
    val mapState = rememberMapState()
    val pickMedia = rememberLauncherForActivityResult(contracts) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        viewModel.updateCoverUri(uri.toString())
    }

    fun updateMapState(longitude: Double, latitude: Double) = scope.launch {
        mapState.selectedLocation = null
        delay(300)

        // Move camera and update map click
        mapState.cameraState.animateTo(
            CameraPosition(
                target = Position(longitude, latitude),
                zoom = 14.0,
            ),
            500.milliseconds,
        )

        delay(300)
        mapState.selectedLocation = MapClickEvent(
            Position(longitude, latitude),
            DpOffset.Zero,
        )
    }

    val location = resultStore.get<Location>(ResultStoreKeys.UPDATE_YEAR_LOCATION)
    LaunchedEffect(location) {
        if (location == null) return@LaunchedEffect
        
        viewModel.onChangedLngLat(location.longitude, location.latitude, isRunTrigger = false)
        viewModel.onChangedAddress(location.address!!)
        updateMapState(location.longitude, location.latitude)
    }

    // Run initialization logic when the view is first composed
    LaunchedEffect(Unit) {
        viewModel.initialize(navRoute.yearId)

        // Listen event from viewmodel
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UpdateYearUiEvent.ZoomAndShowMapClickEvent -> {
                    val longitude = event.longitude
                    val latitude = event.latitude
                    updateMapState(longitude, latitude)
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            focusManager.clearFocus()
                        }
                    }
            ) {
                item {
                    CommonBackHeading(
                        onBack = { navBackStack.removeLastOrNull() },
                        title = "Update Year",
                        actions = {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = null,
                                tint = Color(0xFFF44336),
                                modifier = Modifier
                                    .size(24.dp)
                                    .tapGesture {}
                            )
                        }
                    )
                }

                item {
                    UploadCover(
                        coverUri = uiState.coverUri,
                        onUpload = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    )
                }

                item {
                    Text(
                        text = "Name",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                    )

                    CoreTextField(
                        value = uiState.name,
                        onValueChanged = viewModel::updateName,
                        placeholder = "e.g. The Beginning, etc.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }

                item {
                    DurationSetting(
                        isEnabled = false,
                        startTimeMilliseconds = uiState.startTimeMilliseconds,
                        endTimeMilliseconds = uiState.endTimeMilliseconds,
                        onStartTimeChanged = {},
                        onEndTimeChanged = {},
                    )
                }

                item {
                    LaggingIndicators(
                        laggingIndicators = uiState.laggingIndicators,
                        onAddLaggingIndicator = viewModel::addLaggingIndicator,
                        onDeleteLaggingIndicator = viewModel::deleteLaggingIndicator,
                        onUpdateLaggingIndicator = viewModel::updateLaggingIndicator,
                    )
                }

                item {
                    RewardSection(
                        reward = uiState.reward,
                        rewardImages = uiState.rewardImages,
                        onChangedReward = viewModel::updateReward,
                        onChangedRewardImages = viewModel::updateRewardImages,
                        onDeleteRewardImage = viewModel::deleteRewardImage,
                    )
                }

                item {
                    UpdateReview(
                        stars = uiState.stars,
                        review = uiState.review,
                        onChangedStars = viewModel::onChangedStars,
                        onChangedReview = viewModel::onChangedReview,
                    )
                }

                item {
                    SelectLocation(
                        mapState = mapState,
                        locationKey = ResultStoreKeys.UPDATE_YEAR_LOCATION,
                        address = uiState.address,
                        longitude = uiState.longitude,
                        latitude = uiState.latitude,
                        onChangedAddress = viewModel::onChangedAddress,
                        onChangedLngLat = viewModel::onChangedLngLat,
                        onFetchLocation = {
                            viewModel.fetchCurrentLocationAndUpdate { longitude, latitude ->
                                updateMapState(longitude, latitude)
                            }
                        }
                    )
                }

                item {
                    TwoOptionActions(
                        modifier = Modifier.padding(20.dp),
                        okLabel = "Update",
                        cancelLabel = "Reset",
                        onOk = { viewModel.updateYear(onUpdateSuccess = { navBackStack.removeLastOrNull() }) },
                        onCancel = viewModel::resetState,
                    )
                }
            }
        }
    }
}