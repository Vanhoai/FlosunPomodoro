package com.flosunn.pomodoro.presentation.goals.add_goal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.cropVertical
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.functions.ViewFuncs
import com.flosunn.pomodoro.presentation.goals.add_goal.components.GoalIcon
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.TwoOptionActions
import com.flosunn.pomodoro.ui.theme.AppTheme

private val years = listOf(
    "The Beginning",
    "The Middle",
    "Sky's the Limit",
    "Beyond the Horizon",
)

private val weeks = listOf(
    "Week 1",
    "Week 2",
    "Week 3",
    "Week 4",
)

private val colors = listOf(
    Color(0xFF75D06A),
    Color(0xFF9956DE),
    Color(0xFF1FA7E1),
    Color(0xFF6ED1CF),
    Color(0xFF7274ED),
    Color(0xFFFFB356),
    Color(0xFFFF8B8B),
    Color(0xFFFB96BB),
    Color(0xFFFFA189),
    Color(0xFFCBDD62),
    Color(0xFF89C4FF),
    Color(0xFF3BB076),
)

@Composable
fun AddGoalView() {
    val focusManager = LocalFocusManager.current
    val screenWidth = ViewFuncs.screenWidthDp()

    var goalName by remember { mutableStateOf("") }
    var yearSelected by remember { mutableStateOf(years.first()) }
    var weekSelected by remember { mutableStateOf(weeks.first()) }
    var expandedSelectYear by remember { mutableStateOf(false) }
    var expandedSelectWeek by remember { mutableStateOf(false) }

    val colorSize = (screenWidth - 40 * 2) / 6

    var selectedColor by remember { mutableStateOf<Color?>(null) }
    var selectedIcon by remember { mutableStateOf<Int?>(null) }

    Scaffold(containerColor = Color.White) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .tapGesture(Unit) { focusManager.clearFocus() }
        ) {
            item {
                CommonBackHeading(
                    title = "Add New Goal",
                    actions = {}
                )
            }

            item {
                GoalIcon(
                    color = selectedColor,
                    icon = selectedIcon,
                    onChangeIcon = { selectedIcon = it },
                )
            }

            item {
                Text(
                    text = "Goal Name",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 32.dp, bottom = 8.dp),
                )

                CoreTextField(
                    value = goalName,
                    onValueChanged = { goalName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    placeholder = "e.g., Lose weight, Learn a new language, etc.",
                )
            }

            item {
                Text(
                    text = "Year",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 32.dp, bottom = 8.dp),
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .rippleEffectClickable { expandedSelectYear = true }
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Text(
                        text = yearSelected,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        tint = Color(0xFF141B34),
                        modifier = Modifier.size(16.dp),
                    )

                    DropdownMenu(
                        expanded = expandedSelectYear,
                        onDismissRequest = { expandedSelectYear = false },
                        containerColor = Color.White,
                        modifier = Modifier
                            .width(screenWidth.dp - 40.dp)
                            .cropVertical(8.dp),
                        offset = DpOffset(x = 10.dp, y = 0.dp),
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Text(
                                        text = year,
                                        fontSize = 16.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = AppTheme.typography.body
                                    )
                                },
                                onClick = { yearSelected = year; expandedSelectYear = false }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Week",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 8.dp),
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .rippleEffectClickable { expandedSelectWeek = true }
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Text(
                        text = weekSelected,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        tint = Color(0xFF141B34),
                        modifier = Modifier.size(16.dp),
                    )

                    DropdownMenu(
                        expanded = expandedSelectWeek,
                        onDismissRequest = { expandedSelectWeek = false },
                        containerColor = Color.White,
                        modifier = Modifier
                            .width(screenWidth.dp - 40.dp)
                            .cropVertical(8.dp),
                        offset = DpOffset(x = 10.dp, y = 0.dp),
                    ) {
                        weeks.forEach { week ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Text(
                                        text = week,
                                        fontSize = 16.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = AppTheme.typography.body
                                    )
                                },
                                onClick = { weekSelected = week; expandedSelectWeek = false }
                            )
                        }
                    }
                }
            }



            item {
                Text(
                    text = "Color",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 8.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    colors.subList(0, 6).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(colorSize.dp)
                                .clip(CircleShape)
                                .background(color)
                                .rippleEffectClickable {},
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    colors.subList(6, 12).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(colorSize.dp)
                                .clip(CircleShape)
                                .background(color)
                                .rippleEffectClickable { selectedColor = color },
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    colors.subList(6, 12).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(colorSize.dp)
                                .clip(CircleShape)
                                .background(color)
                                .rippleEffectClickable { selectedColor = color },
                        )
                    }
                }
            }

            item {
                TwoOptionActions(
                    okLabel = "Add",
                    cancelLabel = "Cancel",
                    onOk = {},
                    onCancel = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }
    }
}