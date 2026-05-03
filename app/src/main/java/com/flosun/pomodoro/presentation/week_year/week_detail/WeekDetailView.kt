package com.flosun.pomodoro.presentation.week_year.week_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.core.extensions.noRippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.week_year.week_detail.components.SelfReward
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun WeekDetailView(
    navBackStack: NavBackStack<NavKey>,
    navRoute: NavRoute.WeekDetail,
    viewModel: WeekDetailViewModel = hiltViewModel<WeekDetailViewModel>(),
) {
    val focusManager = LocalFocusManager.current
    val database = LocalDatabase.current
    val screenWidth = ViewFuncs.screenWidthDp()
    val containerProgressWidth = screenWidth - 40 * 2 - 12 * 2 - 50 - 12

    // States
    val weekEntity by database.findWeekById(navRoute.weekId).collectAsState(null)
    if (weekEntity == null) return
    val goals by database.findGoalsByWeekId(navRoute.weekId).collectAsState(emptyList())

    val duration = TimeFuncs.formatDuration(
        weekEntity!!.startTimeMilliseconds,
        weekEntity!!.endTimeMilliseconds
    )

    Scaffold(containerColor = Color.White) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .tapGesture(Unit) { focusManager.clearFocus() }
        ) {
            item { CommonBackHeading(title = weekEntity!!.name) }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            progress = { 0.6f },
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.Transparent),
                            color = Color(0xFF3FA039),
                            trackColor = Color(0xFFE0E0E0),
                            strokeWidth = 8.dp,
                        )

                        Text(
                            text = "${weekEntity!!.progress}%",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = duration,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 32.dp),
                    textAlign = TextAlign.Center,
                )

                SelfReward(
                    reward = weekEntity!!.reward,
                    onChangedReward = {},
                    rewardImages = weekEntity!!.rewardImages,
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Goals:",
                        fontSize = 18.sp,
                        modifier = Modifier
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color(0xFF616161),
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleEffectClickable { navBackStack.add(NavRoute.AddGoal) },
                    )
                }
            }

            items(goals) { goal ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF75D06A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_grid),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White,
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = goal.name,
                            fontSize = 16.sp,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .width(containerProgressWidth.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFFEBEBEB)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .width((containerProgressWidth * (goal.progress / 100f)).dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFF75D06A)),
                                )
                            }

                            Text(
                                text = "${goal.progress}%",
                                fontSize = 14.sp,
                                modifier = Modifier.width(50.dp),
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                }
            }
        }
    }
}