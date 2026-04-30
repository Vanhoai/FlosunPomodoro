package com.flosunn.pomodoro.presentation.week_year.add_new_year

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.presentation.week_year.add_new_year.components.DurationSetting
import com.flosunn.pomodoro.presentation.week_year.add_new_year.components.FullscreenImageViewer
import com.flosunn.pomodoro.presentation.week_year.add_new_year.components.LaggingIndicators
import com.flosunn.pomodoro.presentation.week_year.add_new_year.components.RewardSection
import com.flosunn.pomodoro.presentation.week_year.add_new_year.components.UploadCover
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.TwoOptionActions
import timber.log.Timber


@Composable
fun AddNewYearView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: AddNewYearViewModel = hiltViewModel<AddNewYearViewModel>(),
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    // States
    var coverUri by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var laggingIndicators by remember { mutableStateOf(emptyList<String>()) }

    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                coverUri = uri.toString()
            } else {
                Toast.makeText(context, "No media selected", Toast.LENGTH_SHORT).show()
            }
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
                        coverUri = coverUri,
                        onUpload = {
                            Timber.tag(DEBUG_TAG).d("Upload cover clicked")
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
                        value = name,
                        onValueChanged = { name = it },
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
                        onSelectStartTime = { viewModel.onSelectStartTime() },
                        onSelectEndTime = { viewModel.onSelectEndTime() },
                    )
                }

                item {
                    LaggingIndicators(
                        laggingIndicators = laggingIndicators,
                        onAddLaggingIndicator = { newIndicator ->
                            laggingIndicators = laggingIndicators + newIndicator
                        },
                        onDeleteLaggingIndicator = { indicatorToDelete ->
                            laggingIndicators =
                                laggingIndicators.filterNot { it == indicatorToDelete }
                        },
                        onUpdateLaggingIndicator = { indicators ->
                            laggingIndicators = indicators
                        }
                    )
                }

                item {
                    RewardSection(
                        onChangeSelectedImage = { imageUrl ->
                            selectedImageUrl = imageUrl
                        },
                    )
                }

                item {
                    TwoOptionActions(
                        modifier = Modifier.padding(20.dp),
                        okLabel = "Add",
                        cancelLabel = "Reset",
                        onOk = { viewModel.addNewYear() },
                        onCancel = {},
                    )
                }
            }

            if (uiState.isShowDatePicker) Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .tapGesture(Unit) {
                        viewModel.onCloseDatePicker()
                    },
                contentAlignment = Alignment.Center,
            ) {
                DatePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    date = uiState.currentDate,
                    onDateSelected = { year, month, day ->
                        viewModel.onDateSelected(
                            year,
                            month,
                            day
                        )
                    }
                )
            }

            FullscreenImageViewer(
                isVisible = selectedImageUrl != null,
                imageUrl = selectedImageUrl ?: "",
                onDismiss = { selectedImageUrl = null },
            )
        }
    }
}