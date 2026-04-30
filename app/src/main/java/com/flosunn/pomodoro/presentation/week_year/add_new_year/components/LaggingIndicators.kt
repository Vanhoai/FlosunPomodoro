package com.flosunn.pomodoro.presentation.week_year.add_new_year.components

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.ui.components.core.CoreBottomSheet
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.SwipeableAction
import com.flosunn.pomodoro.ui.components.shared.SwipeableCard
import com.flosunn.pomodoro.ui.components.shared.TwoOptionActions
import kotlinx.coroutines.launch
import timber.log.Timber

private enum class BottomSheetAction {
    ADD,
    UPDATE,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaggingIndicators(
    laggingIndicators: List<String>,
    onAddLaggingIndicator: (String) -> Unit = {},
    onDeleteLaggingIndicator: (String) -> Unit = {},
    onUpdateLaggingIndicator: (List<String>) -> Unit = {},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var laggingIndicatorName by remember { mutableStateOf("") }
    val addLaggingIndicatorSheetState = rememberModalBottomSheetState()
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var currentAction by remember { mutableStateOf(BottomSheetAction.ADD) }
    var selectedLaggingIndicatorIndex by remember { mutableIntStateOf(-1) }
    val focusRequester = remember { FocusRequester() }


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
                    currentAction = BottomSheetAction.ADD

                    scope.launch { addLaggingIndicatorSheetState.show() }
                }
        )
    }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        laggingIndicators.forEachIndexed { index, indicator ->
            SwipeableCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                onCollapsed = {},
                onExpanded = {},
                actions = listOf(
                    SwipeableAction(
                        icon = R.drawable.ic_update,
                        backgroundColor = Color(0xFF75D06A),
                        onPress = {
                            laggingIndicatorName = indicator
                            isShowBottomSheet = true
                            currentAction = BottomSheetAction.UPDATE
                            selectedLaggingIndicatorIndex = index

                            scope.launch { addLaggingIndicatorSheetState.show() }
                        }
                    ),
                    SwipeableAction(
                        icon = R.drawable.ic_delete,
                        backgroundColor = Color(0xFFFF8B8B),
                        onPress = { onDeleteLaggingIndicator(indicator) }
                    ),
                )
            ) {
                IndicatorCard(indicator)
            }
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
            maxLines = 4,
            singleLine = false,
            height = 100.dp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
        )

        HorizontalDivider(
            color = Color(0xFFEDEDED),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        TwoOptionActions(
            modifier = Modifier.padding(20.dp),
            okLabel = if (currentAction == BottomSheetAction.ADD) "Add" else "Update",
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

                if (currentAction == BottomSheetAction.ADD) {
                    onAddLaggingIndicator(laggingIndicatorName)
                } else {
                    val updatedList = laggingIndicators.toMutableList()

                    if (selectedLaggingIndicatorIndex in updatedList.indices) {
                        updatedList[selectedLaggingIndicatorIndex] = laggingIndicatorName
                        onUpdateLaggingIndicator(updatedList)
                    }
                }

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