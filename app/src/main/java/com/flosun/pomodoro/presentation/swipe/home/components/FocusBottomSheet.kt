package com.flosun.pomodoro.presentation.swipe.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.RowSwitch
import com.flosun.pomodoro.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBottomSheet(
    sheetState: SheetState,
    closeBottomSheet: () -> Unit = {},
) {
    CoreBottomSheet(
        sheetState = sheetState,
        title = "Focus Settings",
        closeBottomSheet = closeBottomSheet,
    ) {
        RowSwitch(
            title = "Block All Notifications",
            isActive = false,
            onChange = {},
        )

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        RowSwitch(
            title = "Block Phone Calls",
            isActive = false,
            onChange = {},
        )

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        RowSwitch(
            title = "Block Other Apps",
            isActive = false,
            onChange = {},
        )

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        CoreButton(
            modifier = Modifier.padding(20.dp),
            onPress = { closeBottomSheet() }
        ) {
            Text(
                text = "Okay",
                style = AppTheme.typography.body,
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}