package com.flosun.pomodoro.presentation.week_year.add_new_year

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.core.extensions.tapGesture
import com.flosunn.core.libraries.datepicker.DatePicker
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.presentation.week_year.add_new_year.components.DurationSetting
import com.flosun.pomodoro.presentation.week_year.add_new_year.components.FullscreenImageViewer
import com.flosun.pomodoro.presentation.week_year.add_new_year.components.LaggingIndicators
import com.flosun.pomodoro.presentation.week_year.add_new_year.components.RewardSection
import com.flosun.pomodoro.presentation.week_year.add_new_year.components.UploadCover
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import timber.log.Timber
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val contracts = ActivityResultContracts.PickVisualMedia()

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddNewYearView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: AddNewYearViewModel = hiltViewModel<AddNewYearViewModel>(),
) {
    val focusManager = LocalFocusManager.current

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