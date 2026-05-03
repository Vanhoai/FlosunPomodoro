package com.flosun.pomodoro.presentation.swipe.calendar

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavGraph
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.calendar.components.ProgressingCalendar
import com.flosun.pomodoro.presentation.swipe.calendar.components.SummaryCalendar
import com.flosun.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosun.pomodoro.ui.components.shared.TaskCard
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosun.pomodoro.ui.theme.PomodoroTheme

val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
fun CalendarView(navBackStack: NavBackStack<NavKey>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            ProgressingCalendar()

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )
        }

        item {
            SummaryCalendar()
        }

        items(10) {
            TaskCard(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
                isRevealed = false,
            )
        }
    }
}
