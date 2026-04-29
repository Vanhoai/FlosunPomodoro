package com.flosunn.pomodoro.presentation.week_year.add_new_year.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.tapGesture
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreBottomSheet
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.TwoOptionActions
import kotlinx.coroutines.launch

data class LaggingIndicator(
    val name: String,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaggingIndicators() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var laggingIndicatorName by remember { mutableStateOf("") }
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val addLaggingIndicatorSheetState = rememberModalBottomSheetState()
    val focusRequester = remember { FocusRequester() }

    var laggingIndicators by remember { mutableStateOf(emptyList<LaggingIndicator>()) }

    LaunchedEffect(isShowBottomSheet) {
        if (isShowBottomSheet)
            focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 12.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Lagging Indicators",
            fontSize = 18.sp,
        )

        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null,
            tint = Color(0xFF5F5F5F),
            modifier = Modifier
                .size(24.dp)
                .tapGesture(Unit) {
                    isShowBottomSheet = true
                    scope.launch { addLaggingIndicatorSheetState.show() }
                }
        )
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        laggingIndicators.forEachIndexed { index, indicator ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_tag),
                    contentDescription = null,
                    tint = Color(0xFF5F5F5F),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = indicator.name,
                        fontSize = 16.sp,
                        modifier = Modifier,
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_vertical_dots),
                    contentDescription = null,
                    tint = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                )
            }

            if (index != laggingIndicators.size - 1) HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFFCCCCCC)
            )
        }
    }

    if (isShowBottomSheet) CoreBottomSheet(
        sheetState = addLaggingIndicatorSheetState,
        title = "Add Lagging Indicator",
        closeBottomSheet = {
            scope.launch { addLaggingIndicatorSheetState.hide() }.invokeOnCompletion {
                if (!addLaggingIndicatorSheetState.isVisible) {
                    isShowBottomSheet = false
                }
            }
        },
    ) {
        CoreTextField(
            focusRequester = focusRequester,
            value = laggingIndicatorName,
            onValueChanged = { laggingIndicatorName = it },
            placeholder = "E.g. Read 12 books",
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
        )

        HorizontalDivider(
            color = Color(0xFFEDEDED),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        TwoOptionActions(
            modifier = Modifier.padding(20.dp),
            okLabel = "Add",
            cancelLabel = "Cancel",
            onOk = {
                if (laggingIndicatorName.isBlank()) {
                    Toast.makeText(
                        context,
                        "Lagging Indicator name cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@TwoOptionActions
                }

                laggingIndicators = laggingIndicators + LaggingIndicator(laggingIndicatorName)
                laggingIndicatorName = ""
                scope.launch { addLaggingIndicatorSheetState.hide() }.invokeOnCompletion {
                    if (!addLaggingIndicatorSheetState.isVisible) {
                        isShowBottomSheet = false
                    }
                }
            },
            onCancel = {
                laggingIndicatorName = ""
                scope.launch { addLaggingIndicatorSheetState.hide() }.invokeOnCompletion {
                    if (!addLaggingIndicatorSheetState.isVisible) {
                        isShowBottomSheet = false
                    }
                }
            }
        )
    }
}