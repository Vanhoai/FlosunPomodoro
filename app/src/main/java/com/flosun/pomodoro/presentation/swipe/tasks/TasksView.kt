package com.flosun.pomodoro.presentation.swipe.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavGraph
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.tasks.components.OptionsSlider
import com.flosun.pomodoro.ui.components.core.ActionIcon
import com.flosun.pomodoro.ui.components.core.CoreFloatingButton
import com.flosun.pomodoro.ui.components.core.MenuItem
import com.flosun.pomodoro.ui.components.core.SwipeableCard
import com.flosun.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosun.pomodoro.ui.components.shared.TaskCard
import com.flosun.pomodoro.ui.theme.PomodoroTheme
import timber.log.Timber
import kotlin.collections.listOf


private val options = listOf(
    MenuItem(
        icon = R.drawable.ic_add,
        name = "Add Task",
    ),
    MenuItem(
        icon = R.drawable.ic_database,
        name = "Manage Goals",
    ),
)

@Composable
fun TasksView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: TasksViewModel = hiltViewModel<TasksViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val bounds = it.boundsInRoot()
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })

            OptionsSlider(
                options = listOf(
                    "Active",
                    "Completed",
                ),
                selectedOptionIndex = uiState.selectedOptionIndex,
                onChangedOptionIndex = viewModel::onChangedOptionIndex,
            )

            Text(
                text = "${tasks.size} Tasks",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(tasks, key = { task -> task.id }) { task ->
                    TaskCard(
                        name = task.name,
                        numPomodoro = task.numPomodoro,
                        numPomodoroCompleted = task.numPomodoroCompleted,
                        pomodoroDuration = task.pomodoroDuration,
                        icon = task.icon,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                    )
                }

                item { VerticalDivider(modifier = Modifier.height(120.dp)) }
            }
        }

        CoreFloatingButton(
            options = options,
            onOptionSelected = { item ->
                when (item.name) {
                    "Add Task" -> navBackStack.add(NavRoute.AddTask)
                    "Manage Goals" -> {}
                }
            },
        )
    }
}
