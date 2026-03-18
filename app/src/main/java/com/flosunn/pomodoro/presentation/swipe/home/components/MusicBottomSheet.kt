package com.flosunn.pomodoro.presentation.swipe.home.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

val soundOptions = listOf(
    "None",
    "Rain",
    "Forest",
    "Coffee Shop",
    "Fireplace",
    "Ocean Waves",
    "White Noise",
    "Pink Noise",
    "Brown Noise"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
    sheetState: SheetState,
    closeBottomSheet: () -> Unit = {},
) {
    var selectedOption by remember { mutableIntStateOf(0) }

    CoreBottomSheet(
        sheetState = sheetState,
        title = "Sound",
        closeBottomSheet = closeBottomSheet,
    ) {
        soundOptions.forEachIndexed { index, sound ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = { selectedOption = index },
                    )
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sound),
                    contentDescription = null,
                    tint = if (selectedOption == index) AppTheme.colors.primaryColor else Color(
                        0xFF757575
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = sound,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )

                if (selectedOption == index) Icon(
                    painter = painterResource(R.drawable.ic_checked),
                    contentDescription = null,
                    tint = AppTheme.colors.primaryColor,
                )
            }

            if (index != soundOptions.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFE0E0E0),
                )
            }
        }

        CoreButton(
            modifier = Modifier.padding(20.dp),
            onPress = { closeBottomSheet() }
        ) {
            Text(
                text = "Apply",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}