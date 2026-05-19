package com.flosun.pomodoro.presentation.swipe.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.adapters.database.entities.TaskEntity
import com.flosun.pomodoro.core.constants.CURRENT_TASK_ID_KEY
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.shared.SwipeableAction
import com.flosun.pomodoro.ui.components.shared.SwipeableCard
import com.flosun.pomodoro.ui.components.shared.TaskCard
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.libraries.datastore.rememberPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTask(
    tasks: List<TaskEntity> = emptyList(),
) {
    val database = LocalDatabase.current

    val screenHeight = ViewFuncs.screenHeightDp()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentTaskId by rememberPreference(key = CURRENT_TASK_ID_KEY, defaultValue = "")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }

    val currentTask by database.findTaskById(currentTaskId).collectAsState(null)
    val label = currentTask?.name ?: "Select Task"

    SwipeableCard(
        modifier = Modifier.padding(horizontal = 20.dp),
        onCollapsed = {},
        onExpanded = {},
        actions = listOf(
            SwipeableAction(
                icon = R.drawable.ic_task_done,
                backgroundColor = Color(0xFF75D06A),
                onPress = {
                    scope.launch(Dispatchers.IO) {
                        val updatedTask = currentTask!!.update(isCompleted = true)
                        database.updateTask(updatedTask)

                        currentTaskId = ""
                    }
                },
            ),
        )
    ) {
        TaskCard(
            name = label,
            isShowPlayIcon = false,
            numPomodoro = currentTask?.numPomodoro ?: 0,
            numPomodoroCompleted = currentTask?.numPomodoroCompleted ?: 0,
            pomodoroDuration = currentTask?.pomodoroDuration ?: 0,
            icon = currentTask?.icon ?: R.drawable.ic_grid,
            customIcon = {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF9E9E9E),
                    )
                }
            },
            onPress = { isExpanded = true },
        )
    }

    if (isExpanded) CoreBottomSheet(
        title = "Select Task",
        sheetState = bottomSheetState,
        closeBottomSheet = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                isExpanded = false
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight * 0.7f).dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
            ) {
                items(
                    items = tasks,
                    key = { it.id }
                ) { task ->
                    val isSelected = selectedTask?.id == task.id
                    val borderColor =
                        if (isSelected) AppTheme.colors.primaryColor else AppTheme.colors.borderColor

                    TaskCard(
                        name = task.name,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .border(
                                width = 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(8.dp),
                            ),
                        numPomodoro = task.numPomodoro,
                        numPomodoroCompleted = task.numPomodoroCompleted,
                        pomodoroDuration = task.pomodoroDuration,
                        onPress = {
                            selectedTask = if (isSelected) null
                            else task
                        },
                        isShowPlayIcon = false,
                    )
                }
            }

            TwoOptionActions(
                okLabel = "Ok",
                cancelLabel = "Cancel",
                onCancel = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        isExpanded = false
                    }
                },
                onOk = {
                    currentTaskId = selectedTask?.id ?: ""
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        isExpanded = false
                    }
                },
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}