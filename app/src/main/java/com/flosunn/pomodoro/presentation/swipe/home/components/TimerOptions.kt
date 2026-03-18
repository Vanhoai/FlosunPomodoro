package com.flosunn.pomodoro.presentation.swipe.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerOptions(navBackStack: NavBackStack<NavKey>) {
    val scope = rememberCoroutineScope()

    val focusSheetState = rememberModalBottomSheetState()
    val modeSheetState = rememberModalBottomSheetState()
    val musicSheetState = rememberModalBottomSheetState()

    var isShowFocusBottomSheet by remember { mutableStateOf(false) }
    var isShowModeBottomSheet by remember { mutableStateOf(false) }
    var isShowMusicBottomSheet by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimberOption(
            icon = R.drawable.ic_moon,
            title = "Focus",
            onPress = { isShowFocusBottomSheet = true }
        )

        TimberOption(
            icon = R.drawable.ic_mode,
            title = "Mode",
            onPress = { isShowModeBottomSheet = true }
        )

        TimberOption(
            icon = R.drawable.ic_fullscreen,
            title = "Full Screen",
            onPress = { navBackStack.add(NavRoute.FullScreen) }
        )

        TimberOption(
            icon = R.drawable.ic_music,
            title = "Sound",
            onPress = { isShowMusicBottomSheet = true }
        )
    }

    if (isShowFocusBottomSheet) {
        FocusBottomSheet(
            sheetState = focusSheetState,
            closeBottomSheet = {
                scope.launch { focusSheetState.hide() }.invokeOnCompletion {
                    if (!focusSheetState.isVisible) {
                        isShowFocusBottomSheet = false
                    }
                }
            }
        )
    }

    if (isShowModeBottomSheet) {
        TimerModeBottomSheet(
            sheetState = modeSheetState,
            closeBottomSheet = {
                scope.launch { modeSheetState.hide() }.invokeOnCompletion {
                    if (!modeSheetState.isVisible) {
                        isShowModeBottomSheet = false
                    }
                }
            }
        )
    }

    if (isShowMusicBottomSheet) {
        MusicBottomSheet(
            sheetState = musicSheetState,
            closeBottomSheet = {
                scope.launch { musicSheetState.hide() }.invokeOnCompletion {
                    if (!musicSheetState.isVisible) {
                        isShowMusicBottomSheet = false
                    }
                }
            }
        )
    }
}

@Composable
private fun RowScope.TimberOption(
    icon: Int,
    title: String,
    onPress: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .pointerInput(Unit) {
                detectTapGestures {
                    onPress()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFF676767),
        )

        Text(
            text = title,
            color = Color(0xFF676767),
            fontSize = 14.sp,
        )
    }
}