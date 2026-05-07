package com.flosun.pomodoro.presentation.year.twelve_week_year

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.NamedDivider
import com.flosun.pomodoro.ui.components.shared.TwelveWeekYearCard
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.tapGesture

@Composable
fun TwelveWeekYearView(
    navBackStack: NavBackStack<NavKey>,
    viewModel: TwelveWeekYearViewModel = hiltViewModel<TwelveWeekYearViewModel>()
) {
    val currentYear by viewModel.currentYear.collectAsState()
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
                                .tapGesture { navBackStack.add(NavRoute.AddNewYear) },
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

                if (currentYear != null) TwelveWeekYearCard(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    onPress = { navBackStack.add(NavRoute.YearDetail(yearId = currentYear!!.id)) },
                    cover = currentYear!!.cover,
                    name = currentYear!!.name,
                    startTimeMilliseconds = currentYear!!.startTimeMilliseconds,
                    endTimeMilliseconds = currentYear!!.endTimeMilliseconds,
                )
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
                TwelveWeekYearCard(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    onPress = { navBackStack.add(NavRoute.YearDetail(yearId = year.id)) },
                    cover = year.cover,
                    name = year.name,
                    startTimeMilliseconds = year.startTimeMilliseconds,
                    endTimeMilliseconds = year.endTimeMilliseconds,
                )
            }
        }
    }
}