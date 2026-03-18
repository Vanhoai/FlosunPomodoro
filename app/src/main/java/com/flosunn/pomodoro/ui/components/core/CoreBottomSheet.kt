package com.flosunn.pomodoro.ui.components.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.ui.theme.AppTheme

const val DURATION_MS = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreBottomSheet(
    sheetState: SheetState,
    title: String,
    closeBottomSheet: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = { closeBottomSheet() },
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(
            topStart = AppTheme.sizing.borderMedium,
            topEnd = AppTheme.sizing.borderMedium
        ),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color(0xFFEDEDED),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, top = 8.dp),
            textAlign = TextAlign.Center,
        )

        HorizontalDivider(
            color = Color(0xFFE1E1E1),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        content()
    }
}