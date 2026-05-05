package com.flosun.pomodoro.presentation.goals.add_goal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.ui.components.shared.SelectColor
import com.flosun.pomodoro.ui.components.shared.SelectIcon
import com.flosun.pomodoro.presentation.goals.add_goal.components.SelectLaggingIndicator
import com.flosun.pomodoro.ui.components.shared.SelectWeek
import com.flosun.pomodoro.ui.components.shared.SelectYear
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.borderTop

@Composable
fun AddGoalView(
    viewModel: AddGoalViewModel = hiltViewModel<AddGoalViewModel>(),
) {
    val navBackStack = LocalNavBackStack.current
    val focusManager = LocalFocusManager.current
    val database = LocalDatabase.current

    val uiState by viewModel.uiState.collectAsState()

    val weeks by database.findWeeksByYearId(
        uiState.selectedYear?.id ?: ""
    ).collectAsState(initial = emptyList())

    val laggingIndicators by database.findLaggingIndicatorsByYearId(
        uiState.selectedYear?.id ?: ""
    ).collectAsState(initial = emptyList())

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .tapGesture(Unit) { focusManager.clearFocus() },
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    CommonBackHeading(
                        title = "Add New Goal",
                        actions = {}
                    )
                }

                item {
                    SelectIcon(
                        color = uiState.selectedColor,
                        icon = uiState.selectedIcon,
                        onChangeIcon = { viewModel.onChangedSelectedIcon(it) },
                    )

                    SelectColor(
                        selectedColor = uiState.selectedColor,
                        onChangedSelectedColor = { viewModel.onChangedSelectedColor(it) }
                    )
                }

                item {
                    Text(
                        text = "Goal Name",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                    )

                    CoreTextField(
                        value = uiState.goalName,
                        onValueChanged = { viewModel.onChangedGoalName(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        placeholder = "e.g., Lose weight, Learn a new language, etc.",
                    )
                }

                item {
                    SelectYear(
                        selectedYear = uiState.selectedYear,
                        onChangedSelectedYear = { viewModel.onChangedSelectedYear(it) },
                    )
                }

                item {
                    SelectWeek(
                        selectedWeek = uiState.selectedWeek,
                        weeks = weeks,
                        onChangedSelectedWeek = { viewModel.onChangedSelectedWeek(it) },
                    )
                }

                item {
                    SelectLaggingIndicator(
                        selectedLaggingIndicator = uiState.selectedLaggingIndicator,
                        laggingIndicators = laggingIndicators,
                        onChangedSelectedLaggingIndicator = {
                            viewModel.onChangedSelectedLaggingIndicator(
                                it
                            )
                        },
                    )
                }

                item { VerticalDivider(modifier = Modifier.height(160.dp)) }
            }

            TwoOptionActions(
                okLabel = "Add",
                cancelLabel = "Cancel",
                onOk = { viewModel.addNewGoal(onSuccess = { navBackStack.removeLastOrNull() }) },
                onCancel = { navBackStack.removeLastOrNull() },
                modifier = Modifier
                    .fillMaxWidth()
                    .borderTop(
                        strokeWidth = 1.dp,
                        color = AppTheme.colors.borderColor,
                    )
                    .padding(20.dp)
            )
        }
    }
}