package com.flosun.pomodoro.presentation.task.add_task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.task.add_task.components.RepetitionOption
import com.flosun.pomodoro.ui.components.shared.SelectYear
import com.flosun.pomodoro.presentation.task.add_task.components.SelectEstimatedPomodoros
import com.flosun.pomodoro.presentation.task.add_task.components.SelectGoal
import com.flosun.pomodoro.presentation.task.add_task.components.SelectLaggingIndicator
import com.flosun.pomodoro.presentation.task.add_task.components.SelectPomodoroDuration
import com.flosun.pomodoro.presentation.task.add_task.components.SelectRepetition
import com.flosun.pomodoro.presentation.task.add_task.components.SelectTags
import com.flosun.pomodoro.ui.components.core.CoreSpinner
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.SelectColor
import com.flosun.pomodoro.ui.components.shared.SelectIcon
import com.flosun.pomodoro.ui.components.shared.SelectWeek
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.borderTop
import com.flosunn.core.extensions.tapGesture


private val tags = listOf(
    "Work",
    "Study",
    "Exercise",
    "Leisure",
    "Health",
    "Finance",
    "Social",
    "Personal Development",
    "Hobby",
    "Other",
)

@Composable
fun AddTaskView(
    viewModel: AddTaskViewModel = hiltViewModel<AddTaskViewModel>()
) {
    val navBackStack = LocalNavBackStack.current
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()
    val weeks by viewModel.weeks.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val laggingIndicators by viewModel.laggingIndicators.collectAsState()

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .tapGesture(Unit) { focusManager.clearFocus() },
            contentAlignment = Alignment.BottomCenter,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    CommonBackHeading(
                        title = "Add New Task",
                        actions = {}
                    )
                }

                item {
                    SelectIcon(
                        color = uiState.selectedColor,
                        icon = uiState.selectedIcon,
                        onChangeIcon = viewModel::onChangedSelectedIcon,
                    )

                    SelectColor(
                        selectedColor = uiState.selectedColor,
                        onChangedSelectedColor = viewModel::onChangedSelectedColor
                    )
                }

                item {
                    Text(
                        text = "Task Name",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                    )

                    CoreTextField(
                        value = uiState.taskName,
                        onValueChanged = viewModel::onChangedTaskName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        placeholder = "e.g., Read a book",
                        maxLines = 1,
                        suffix = {
                            if (uiState.isCheckingTaskName) CoreSpinner(
                                circleColors = listOf(
                                    Color(0xFFD0D0D0),
                                    Color(0xFFD0D0D0),
                                    Color(0xFFD0D0D0),
                                ),
                                modifier = Modifier.size(16.dp),
                            ) else Icon(
                                painter = painterResource(R.drawable.ic_checked),
                                contentDescription = null,
                                tint = if (uiState.isValidTaskName) Color(0xFF4CAF50) else Color(
                                    0xFFBEBEBE
                                ),
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    )

                    if (uiState.taskName.isNotBlank()) Text(
                        text = if (uiState.isValidTaskName) "Task name is available" else "Task name is already taken",
                        fontSize = 15.sp,
                        color = if (uiState.isValidTaskName) Color(0xFF4CAF50) else Color(0xFFFF4F41),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }

                item {
                    SelectEstimatedPomodoros(
                        selectedEstimatedPomodoro = uiState.selectedEstimatedPomodoro,
                        onChangedSelectedEstimatedPomodoro = viewModel::onChangedSelectedEstimatedPomodoro,
                    )
                }

                item {
                    SelectPomodoroDuration(
                        selectedPomodoroDuration = uiState.selectedPomodoroDuration,
                        onChangedSelectedPomodoroDuration = viewModel::onChangedSelectedPomodoroDuration,
                    )
                }

                item {
                    SelectTags(
                        tags = tags,
                        selectedTags = uiState.selectedTags,
                        onSelect = viewModel::onChangedTags,
                    )
                }

                item {
                    SelectYear(
                        selectedYear = uiState.selectedYear,
                        onChangedSelectedYear = viewModel::onChangedSelectedYear,
                    )
                }

                item {
                    SelectWeek(
                        selectedWeek = uiState.selectedWeek,
                        weeks = weeks,
                        onChangedSelectedWeek = viewModel::onChangedSelectedWeek
                    )
                }

                item {
                    SelectGoal(
                        selectedGoal = uiState.selectedGoal,
                        goals = goals,
                        onChangedSelectedGoal = viewModel::onChangedSelectedGoal
                    )
                }

                item {
                    SelectLaggingIndicator(
                        laggingIndicators = laggingIndicators,
                        selectedLaggingIndicator = uiState.selectedLaggingIndicator,
                        onChangedSelectedLaggingIndicator = viewModel::onChangedSelectedLaggingIndicator,
                    )
                }

                item {
                    SelectRepetition(
                        selectedOption = uiState.selectedRepetitionOption,
                        repetitionOptions = RepetitionOption.entries,
                        selectedDays = uiState.selectedDays,
                        onSelectRepetitionOption = viewModel::onChangedSelectedRepetitionOption,
                        onSelectDay = viewModel::onChangedSelectedDays,
                    )
                }

                item { VerticalDivider(modifier = Modifier.height(160.dp)) }
            }

            TwoOptionActions(
                modifier = Modifier
                    .background(Color.White)
                    .borderTop(
                        strokeWidth = 1.dp,
                        color = AppTheme.colors.borderColor,
                    )
                    .padding(20.dp),
                okLabel = "Add",
                cancelLabel = "Cancel",
                onOk = { viewModel.addTask { navBackStack.removeLastOrNull() } },
                onCancel = viewModel::resetState,
            )
        }
    }
}