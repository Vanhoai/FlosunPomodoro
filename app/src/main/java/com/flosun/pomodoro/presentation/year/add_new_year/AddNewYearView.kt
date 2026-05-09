package com.flosun.pomodoro.presentation.year.add_new_year

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.ResultStoreKeys
import com.flosun.pomodoro.core.permissions.rememberMultiplePermissionManager
import com.flosun.pomodoro.core.permissions.rememberMutablePermissionsManager
import com.flosun.pomodoro.core.utils.result_store.LocalResultStore
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.ui.components.shared.DurationSetting
import com.flosun.pomodoro.ui.components.shared.LaggingIndicators
import com.flosun.pomodoro.ui.components.shared.RewardSection
import com.flosun.pomodoro.ui.components.shared.UploadCover
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.SelectLocation
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi

private val contracts = ActivityResultContracts.PickVisualMedia()

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddNewYearView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: AddNewYearViewModel = hiltViewModel<AddNewYearViewModel>(),
) {
    val focusManager = LocalFocusManager.current
    val resultStore = LocalResultStore.current

    val permissionsManager = rememberMultiplePermissionManager(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onPermissionResults = { permissionResults -> }
    )

    val mapState = rememberMapState()
    val location = resultStore.get<Location>(ResultStoreKeys.ADD_NEW_YEAR_LOCATION)

    LaunchedEffect(location) {
        // Receive the location after user selects a location in the map view, and update the state in view model
        if (location == null) return@LaunchedEffect

        // Update the map state to show the selected location
        mapState.selectedLocation = null
        viewModel.onUpdateAddressAndLngLat(
            address = location.address!!,
            longitude = location.longitude,
            latitude = location.latitude,
        )

        // Move camera and update map click
        mapState.cameraState.animateTo(
            CameraPosition(
                target = Position(location.longitude, location.latitude),
                zoom = 14.0,
            ),
            500.milliseconds,
        )

        mapState.selectedLocation = MapClickEvent(
            Position(location.longitude, location.latitude),
            DpOffset.Zero,
        )
    }


    val uiState by viewModel.uiState.collectAsState()
    val pickMedia = rememberLauncherForActivityResult(contracts) { uri ->
        if (uri != null) viewModel.updateCoverUri(uri.toString())
    }

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
                        title = "Add New Year",
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
                        startTimeMilliseconds = uiState.startTimeMilliseconds,
                        endTimeMilliseconds = uiState.endTimeMilliseconds,
                        onStartTimeChanged = viewModel::updateStartTime,
                        onEndTimeChanged = viewModel::updateEndTime,
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
                    SelectLocation(
                        mapState = mapState,
                        locationKey = ResultStoreKeys.ADD_NEW_YEAR_LOCATION,
                        address = uiState.address,
                        longitude = uiState.longitude,
                        latitude = uiState.latitude,
                        onChangedAddress = viewModel::onChangedAddress,
                        onChangedLngLat = viewModel::onChangedLngLat,
                        onUseCurrentLocation = {
                            if (permissionsManager.allPermissionGranted.not()) {
                                permissionsManager.requestPermissions()
                            } else {
                                // Fetch the current location and update the state in view model
                            }
                        },
                    )
                }

                item {
                    TwoOptionActions(
                        modifier = Modifier.padding(20.dp),
                        okLabel = "Add",
                        cancelLabel = "Reset",
                        onOk = {
                            viewModel.addNewYear(
                                onAddSuccess = {
                                    navBackStack.removeLastOrNull()
                                }
                            )
                        },
                        onCancel = viewModel::resetState,
                    )
                }
            }
        }
    }
}