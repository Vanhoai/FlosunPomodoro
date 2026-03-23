package com.flosunn.pomodoro.presentation.swipe.settings.preference.choose_duration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.constants.DurationType
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.theme.AppTheme


val durationOptions = listOf(
    5, 10, 15, 20, 25, 30, 35, 40
)

val pomodoroOptions = listOf(
    1, 2, 3, 4, 5
)

@Composable
fun ChooseDurationView(
    navBackStack: NavBackStack<NavKey>,
    route: NavRoute.ChooseDuration
) {
    var selectedOption by remember { mutableIntStateOf(0) }
    val options = when (route.type) {
        DurationType.LONG_BREAK_AFTER -> pomodoroOptions
        else -> durationOptions
    }

    Scaffold(
        containerColor = AppTheme.colors.backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(
                onBack = { navBackStack.removeLastOrNull() },
                title = route.type.title
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White)
            ) {
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(),
                                onClick = {
                                    selectedOption = index
                                },
                            )
                            .height(52.dp)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$option ${if (route.type == DurationType.LONG_BREAK_AFTER) "Pomodoro" else "Minutes"}",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )

                        if (selectedOption == index) Icon(
                            painter = painterResource(R.drawable.ic_checked),
                            contentDescription = null,
                            tint = Color(0xFF3FA039),
                        )
                    }

                    if (index != durationOptions.size - 1) HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE0E0E0)
                    )
                }
            }

            if (route.type != DurationType.LONG_BREAK_AFTER) {
                CoreButton(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Custom Duration",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}