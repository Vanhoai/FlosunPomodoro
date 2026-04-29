package com.flosunn.pomodoro.presentation.week_year.year_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.presentation.week_year.year_detail.components.WeekProgressedCard
import com.flosunn.pomodoro.presentation.week_year.year_detail.components.YearDetailBanner
import com.flosunn.pomodoro.presentation.week_year.year_detail.components.YearDetailReward
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.theme.AppTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun YearDetailView(
    navBackStack: NavBackStack<NavKey>,
) {
    val laggingIndicators = listOf(
        "Total Focus Time",
        "Total Distractions",
        "Average Focus Time",
        "Longest Focus Session",
    )

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                CommonBackHeading(
                    onBack = { navBackStack.removeLastOrNull() },
                    title = "The Beginning",
                    actions = {
                        Icon(
                            painter = painterResource(R.drawable.ic_upload),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black,
                        )
                    }
                )

                YearDetailBanner()
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
                            text = "${index + 1}. $indicator",
                            fontSize = 18.sp,
                            color = Color(0xFF5F5F5F),
                        )
                    }
                }
            }

            item {
                YearDetailReward()
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

            items(4) {
                WeekProgressedCard(
                    navigateToWeek = { navBackStack.add(NavRoute.WeekDetail) }
                )
            }
        }
    }
}