package com.flosunn.pomodoro.presentation.week_year.twelve_week_year

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.adapters.database.LocalDatabase
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.core.functions.TimeFuncs
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.NamedDivider
import com.flosunn.pomodoro.ui.components.shared.TwelveWeekYearCard
import com.flosunn.pomodoro.ui.theme.AppTheme
import timber.log.Timber

@Composable
fun TwelveWeekYearView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: TwelveWeekYearViewModel = hiltViewModel<TwelveWeekYearViewModel>()
) {
    val allYears by viewModel.allYears.collectAsState()

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                CommonBackHeading(
                    onBack = { navBackStack.removeLastOrNull() },
                    title = "12 Week Year",
                    actions = {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            tint = Color(0xFF141B34),
                            modifier = Modifier
                                .size(24.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        navBackStack.add(NavRoute.AddNewYear)
                                    }
                                },
                        )
                    }
                )
            }

            item {
                NamedDivider(
                    name = "Current Year",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp)
                )

//                TwelveWeekYearCard(
//                    modifier = Modifier
//                        .padding(horizontal = 20.dp)
//                        .padding(bottom = 20.dp),
//                    onPress = { navBackStack.add(NavRoute.YearDetail) }
//                )
            }

            item {
                NamedDivider(
                    name = "Past Years",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp)
                )
            }

            items(allYears) { year ->
                val duration = "${TimeFuncs.formatTimeString(year.startTimeMilliseconds)} - ${
                    TimeFuncs.formatTimeString(year.endTimeMilliseconds)
                }"

                TwelveWeekYearCard(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    onPress = { navBackStack.add(NavRoute.YearDetail) },
                    cover = year.cover,
                    name = year.name,
                    duration = duration,
                )
            }
        }
    }
}