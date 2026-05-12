package com.flosun.pomodoro.presentation.year.week_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.core.extensions.noRippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.core.constants.ResultStoreKeys
import com.flosun.pomodoro.core.utils.result_store.rememberLocalResultStore
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.year.update_year.UpdateYearUiEvent
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.GoalCard
import com.flosun.pomodoro.ui.components.shared.RewardSection
import com.flosun.pomodoro.ui.components.shared.SelectLocation
import com.flosun.pomodoro.ui.components.shared.SwipeableCard
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.components.shared.UpdateReview
import com.flosun.pomodoro.ui.components.shared.map.MapClickEvent
import com.flosun.pomodoro.ui.components.shared.map.rememberMapState
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WeekDetailView(
    navBackStack: NavBackStack<NavKey>,
    navRoute: NavRoute.WeekDetail,
    viewModel: WeekDetailViewModel = hiltViewModel<WeekDetailViewModel>(),
) {
    val focusManager = LocalFocusManager.current
    val database = LocalDatabase.current

    val scope = rememberCoroutineScope()
    val mapState = rememberMapState()
    val resultStore = rememberLocalResultStore()

    val goals by database.findGoalsByWeekId(navRoute.weekId).collectAsState(emptyList())
    val uiState by viewModel.uiState.collectAsState()

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

    LaunchedEffect(Unit) {
        viewModel.initialize(navRoute.weekId)

        // Listen event from viewmodel
        viewModel.uiEvent.collect { event ->
            when (event) {
                is WeekDetailUiEvent.ZoomAndShowMapClickEvent -> {
                    val longitude = event.longitude
                    val latitude = event.latitude
                    updateMapState(longitude, latitude)
                }
            }
        }
    }

    val location = resultStore.get<Location>(ResultStoreKeys.UPDATE_WEEK_LOCATION)
    LaunchedEffect(location) {
        if (location == null) return@LaunchedEffect

        viewModel.onChangedLngLat(location.longitude, location.latitude, isRunTrigger = false)
        viewModel.onChangedAddress(location.address!!)
        updateMapState(location.longitude, location.latitude)
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .tapGesture(Unit) { focusManager.clearFocus() }
            ) {
                item { CommonBackHeading(title = uiState.name) }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                progress = { uiState.progress / 100f },
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(Color.Transparent),
                                color = Color(0xFF3FA039),
                                trackColor = Color(0xFFE0E0E0),
                                strokeWidth = 8.dp,
                            )

                            Text(
                                text = "${uiState.progress}%",
                                fontSize = 20.sp,
                                color = Color(0xFF3FA039),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = uiState.duration,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 12.dp, bottom = 32.dp),
                        textAlign = TextAlign.Center,
                    )

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Goals:",
                            fontSize = 18.sp,
                            modifier = Modifier
                        )

                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            tint = Color(0xFF616161),
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleEffectClickable { navBackStack.add(NavRoute.AddGoal) },
                        )
                    }
                }

                items(goals) { goal ->
                    SwipeableCard(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 12.dp),
                        isRevealed = false
                    ) {
                        GoalCard(
                            name = goal.name,
                            progress = goal.progress,
                            icon = goal.icon,
                        )
                    }
                }

                item { VerticalDivider(modifier = Modifier.height(200.dp)) }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = AppTheme.colors.borderColor
                )

                TwoOptionActions(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp),
                    okLabel = "Update",
                    cancelLabel = "Reset",
                    onOk = { viewModel.updateWeek() },
                    onCancel = viewModel::resetState,
                )
            }
        }
    }
}