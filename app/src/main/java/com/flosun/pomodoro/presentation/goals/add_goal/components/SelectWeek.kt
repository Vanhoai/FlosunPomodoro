package com.flosun.pomodoro.presentation.goals.add_goal.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.cropVertical
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun SelectWeek(
    selectedWeek: WeekEntity? = null,
    weeks: List<WeekEntity> = emptyList(),
    onChangedSelectedWeek: (WeekEntity) -> Unit = {},
) {
    val screenWidth = ViewFuncs.screenWidthDp()
    var expandedSelectWeek by remember { mutableStateOf(false) }

    val label = selectedWeek?.let { selectedWeek.name } ?: "Select Week"

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
            text = label,
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
                val duration = TimeFuncs.formatDuration(
                    week.startTimeMilliseconds,
                    week.endTimeMilliseconds
                )

                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(
                            text = "${week.name}: $duration",
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.body
                        )
                    },
                    onClick = {
                        onChangedSelectedWeek(week)
                        expandedSelectWeek = false
                    }
                )
            }
        }
    }
}