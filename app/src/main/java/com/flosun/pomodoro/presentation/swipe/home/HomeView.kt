package com.flosun.pomodoro.presentation.swipe.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.home.components.PomodoroTimber
import com.flosun.pomodoro.presentation.swipe.home.components.TimerOptions
import com.flosun.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosun.pomodoro.ui.components.shared.TaskCard
import com.flosun.pomodoro.ui.theme.AppTheme


@Composable
fun HomeView(navBackStack: NavBackStack<NavKey>) {
    var timer by rememberSaveable { mutableIntStateOf(12 * 60) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundColor),
    ) {
        item {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })
            TaskCard(
                name = "Design a Pomodoro App",
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            PomodoroTimber(
                totalSeconds = 25 * 60,
                remainTime = timer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            TimerOptions(navBackStack)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 12.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Tasks",
                    fontSize = 18.sp,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See All",
                        fontSize = 16.sp,
                        color = AppTheme.colors.primaryColor,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_next),
                        contentDescription = null,
                        tint = AppTheme.colors.primaryColor,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        items(
            count = 10,
            key = { it }
        ) {
            TaskCard(
                name = "Task $it",
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}