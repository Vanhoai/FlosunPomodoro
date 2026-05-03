package com.flosun.pomodoro.presentation.goals.add_goal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flosunn.core.extensions.cropVertical
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.presentation.goals.add_goal.components.GoalIcon
import com.flosun.pomodoro.presentation.goals.add_goal.components.SelectColor
import com.flosun.pomodoro.presentation.goals.add_goal.components.SelectLaggingIndicator
import com.flosun.pomodoro.presentation.goals.add_goal.components.SelectWeek
import com.flosun.pomodoro.presentation.goals.add_goal.components.SelectYear
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme

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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .tapGesture(Unit) { focusManager.clearFocus() }
        ) {
            item {
                CommonBackHeading(
                    title = "Add New Goal",
                    actions = {}
                )
            }

            item {
                GoalIcon(
                    color = uiState.selectedColor,
                    icon = uiState.selectedIcon,
                    onChangeIcon = { viewModel.onChangedSelectedIcon(it) },
                )
            }

            item {
                Text(
                    text = "Goal Name",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 32.dp, bottom = 8.dp),
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

            item {
                SelectColor(onChangedSelectedColor = { viewModel.onChangedSelectedColor(it) })
            }

            item {
                TwoOptionActions(
                    okLabel = "Add",
                    cancelLabel = "Cancel",
                    onOk = {},
                    onCancel = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }
    }
}