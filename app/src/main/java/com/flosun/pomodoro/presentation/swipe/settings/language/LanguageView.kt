package com.flosun.pomodoro.presentation.swipe.settings.language

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
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DurationType
import com.flosun.pomodoro.presentation.swipe.settings.preference.choose_duration.durationOptions
import com.flosun.pomodoro.presentation.swipe.settings.preference.choose_duration.pomodoroOptions
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.theme.AppTheme

val supportLanguages = listOf(
    "English",
    "Vietnamese",
    "Chinese",
)

@Composable
fun AppLanguageView(navBackStack: NavBackStack<NavKey>) {
    var selectedOption by remember { mutableIntStateOf(0) }

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
                title = "App Language"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White)
            ) {
                supportLanguages.forEachIndexed { index, language ->
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
                            text = language,
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

                    if (index != supportLanguages.size - 1) HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}