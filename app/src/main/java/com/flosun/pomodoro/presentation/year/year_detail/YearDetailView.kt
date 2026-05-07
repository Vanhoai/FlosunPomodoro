package com.flosun.pomodoro.presentation.year.year_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.year.year_detail.components.WeekProgressedCard
import com.flosun.pomodoro.presentation.year.year_detail.components.YearDetailBanner
import com.flosun.pomodoro.presentation.year.year_detail.components.YearDetailReward
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.tapGesture

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun YearDetailView(
    navBackStack: NavBackStack<NavKey>,
    navRoute: NavRoute.YearDetail,
) {
    val database = LocalDatabase.current
    val yearEntity by database.findTwelveWeekYearById(navRoute.yearId).collectAsState(null)

    val laggingIndicators by database
        .findLaggingIndicatorsByYearId(navRoute.yearId)
        .collectAsState(emptyList())

    val weeks by database
        .findWeekByYearIdAndBeforeStartTime(
            yearId = navRoute.yearId,
            startTime = System.currentTimeMillis(),
        )
        .collectAsState(emptyList())

    var currentTimeMilliseconds by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(yearEntity) {
        if (yearEntity == null) return@LaunchedEffect

        if (currentTimeMilliseconds >= yearEntity!!.startTimeMilliseconds && currentTimeMilliseconds <= yearEntity!!.endTimeMilliseconds) {
            currentTimeMilliseconds = System.currentTimeMillis()
        } else {
            // If the year is not active
            val now = System.currentTimeMillis()
            currentTimeMilliseconds = when {
                now < yearEntity!!.startTimeMilliseconds -> yearEntity!!.startTimeMilliseconds
                now > yearEntity!!.endTimeMilliseconds -> yearEntity!!.endTimeMilliseconds
                else -> now
            }
        }
    }

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                CommonBackHeading(
                    onBack = { navBackStack.removeLastOrNull() },
                    title = yearEntity?.name ?: "Unknown Year",
                    actions = {
                        Icon(
                            painter = painterResource(R.drawable.ic_update),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .tapGesture(Unit) {
                                    navBackStack.add(NavRoute.UpdateYear(yearId = navRoute.yearId))
                                },
                            tint = Color.Black,
                        )
                    }
                )

                YearDetailBanner(
                    name = yearEntity?.name ?: "Unknown Year",
                    coverUri = yearEntity?.cover
                        ?: "https://i.pinimg.com/736x/c8/0e/34/c80e34ebfc7b7ca87e11779b36d950a4.jpg",
                    startTimeMilliseconds = yearEntity?.startTimeMilliseconds ?: 0L,
                    endTimeMilliseconds = yearEntity?.endTimeMilliseconds ?: 0L,
                    currentTimeMilliseconds = currentTimeMilliseconds,
                )
            }

            item {
                Text(
                    text = "Lagging Indicators",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp),
                )

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    laggingIndicators.forEachIndexed { index, indicator ->
                        Text(
                            text = "${index + 1}. ${indicator.name}",
                            fontSize = 18.sp,
                            color = Color(0xFF5F5F5F),
                        )
                    }
                }
            }

            item {
                YearDetailReward(
                    reward = yearEntity?.reward ?: "No reward set",
                    rewardImages = yearEntity?.rewardImages ?: emptyList(),
                )
            }

            item {
                Text(
                    text = "Progress",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp),
                )
            }

            items(weeks) { week ->
                WeekProgressedCard(
                    name = week.name,
                    startTimeMilliseconds = week.startTimeMilliseconds,
                    endTimeMilliseconds = week.endTimeMilliseconds,
                    progress = week.progress,
                    navigateToWeek = { navBackStack.add(NavRoute.WeekDetail(weekId = week.id)) }
                )
            }

            item { VerticalDivider(modifier = Modifier.height(40.dp)) }
        }
    }
}