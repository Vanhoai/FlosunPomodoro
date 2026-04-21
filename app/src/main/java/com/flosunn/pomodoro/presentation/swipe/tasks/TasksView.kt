package com.flosunn.pomodoro.presentation.swipe.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavGraph
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.presentation.swipe.tasks.components.OptionsSlider
import com.flosunn.pomodoro.ui.components.core.ActionIcon
import com.flosunn.pomodoro.ui.components.core.CoreFloatingButton
import com.flosunn.pomodoro.ui.components.core.SwipeableCard
import com.flosunn.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosunn.pomodoro.ui.components.shared.TaskCard
import com.flosunn.pomodoro.ui.theme.PomodoroTheme
import timber.log.Timber

fun Modifier.cropVertical(padding: Dp): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val paddingPx = padding.roundToPx()

    // Reduce the height of the layout by twice the padding (top and bottom)
    layout(placeable.width, placeable.height - paddingPx * 2) {
        // Position the content slightly higher to "swallow" the top padding
        placeable.placeRelative(0, -paddingPx)
    }
}

@Composable
fun TasksView(navBackStack: NavBackStack<NavKey>) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val bounds = it.boundsInRoot()
                Timber.tag("TasksView").d("Bounds: $bounds")
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })
            OptionsSlider()
            Text(
                text = "8 Tasks",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(10) {
                    TaskCard(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        isRevealed = false,
                    )
                }
            }
        }

        CoreFloatingButton()
    }
}
