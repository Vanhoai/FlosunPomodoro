package com.flosunn.pomodoro.presentation.swipe.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreBottomSheet
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerModeBottomSheet(
    sheetState: SheetState,
    closeBottomSheet: () -> Unit = {},
) {
    var selectedOption by remember { mutableIntStateOf(0) }

    CoreBottomSheet(
        sheetState = sheetState,
        title = "Timer Mode",
        closeBottomSheet = closeBottomSheet,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = { selectedOption = 0 }
                )
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "25:00 - 00:00",
                    fontSize = 18.sp,
                )

                Text(
                    text = "Countdown from 25 minutes until time runs out.",
                    fontSize = 14.sp,
                )
            }

            Box(modifier = Modifier.width(40.dp)) {
                if (selectedOption == 0) Icon(
                    painter = painterResource(R.drawable.ic_checked),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = AppTheme.colors.primaryColor
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = { selectedOption = 1 }
                )
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "00:00 - Infinity",
                    fontSize = 18.sp,
                )

                Text(
                    text = "Start counting up from 00:00 until you stop the timer.",
                    fontSize = 14.sp,
                )
            }

            Box(modifier = Modifier.width(40.dp)) {
                if (selectedOption == 1) Icon(
                    painter = painterResource(R.drawable.ic_checked),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = AppTheme.colors.primaryColor
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        CoreButton(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            onPress = { closeBottomSheet() },
        ) {
            Text(
                text = "Apply",
                fontSize = 16.sp,
                color = Color.White,
                style = AppTheme.typography.body
            )
        }
    }
}