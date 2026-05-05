package com.flosun.pomodoro.presentation.goals.add_goal.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flosunn.core.extensions.noRippleEffectClickable
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import kotlinx.coroutines.launch
import timber.log.Timber

data class GoalIcon(
    val resource: Int, // for display purpose
    val name: String, // for search purpose
)

private val icons = listOf(
    GoalIcon(R.drawable.ic_hexagon_filled, "Hexagon"),
    GoalIcon(R.drawable.ic_mortarboard_filled, "Mortarboard"),
    GoalIcon(R.drawable.ic_achieve_filled, "Achieve"),
    GoalIcon(R.drawable.ic_book_filled, "Book"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalIcon(
    color: Color? = null,
    icon: Int? = null,
    onChangeIcon: (Int) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val addIconBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var iconName by remember { mutableStateOf("") }

    Timber.tag(DEBUG_TAG).d("Recomposing GoalIcon with icon: $icon and color: $color")

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(color ?: Color.White)
                .border(
                    width = 1.dp,
                    color = color ?: Color(0xFFF1F1F1),
                    shape = CircleShape
                )
                .rippleEffectClickable { isShowBottomSheet = true },
            contentAlignment = Alignment.Center,
        ) {
            if (icon != null) Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = if (color != null) Color.White else Color(0xFF6E6E6E),
                modifier = Modifier.size(32.dp),
            ) else Icon(
                painter = painterResource(R.drawable.ic_add_2),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (color != null) Color.White else Color(0xFFBEBEBE),
            )
        }
    }

    if (isShowBottomSheet) CoreBottomSheet(
        sheetState = addIconBottomSheetState,
        title = "Add Icon",
        closeBottomSheet = {
            scope.launch { addIconBottomSheetState.hide() }.invokeOnCompletion {
                if (!addIconBottomSheetState.isVisible) {
                    isShowBottomSheet = false
                }
            }
        },
        modifier = Modifier.tapGesture(Unit) { focusManager.clearFocus() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(color ?: Color.White)
                    .border(
                        width = 1.dp,
                        color = color ?: Color(0xFFF1F1F1),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (icon != null) Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFF6E6E6E),
                    modifier = Modifier.size(32.dp),
                ) else Icon(
                    painter = painterResource(R.drawable.ic_add_2),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFBEBEBE),
                )
            }
        }

        CoreTextField(
            value = iconName,
            onValueChanged = { iconName = it },
            placeholder = "E.g. Book",
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(icons) { icon ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .noRippleEffectClickable {
                            onChangeIcon(icon.resource)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(icon.resource),
                        contentDescription = null,
                        tint = Color(0xFF6E6E6E),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        TwoOptionActions(
            okLabel = "OK",
            cancelLabel = "Cancel",
            onOk = {
                scope.launch { addIconBottomSheetState.hide() }
                    .invokeOnCompletion { isShowBottomSheet = false }
            },
            onCancel = {
                scope.launch { addIconBottomSheetState.hide() }
                    .invokeOnCompletion { isShowBottomSheet = false }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        )
    }
}