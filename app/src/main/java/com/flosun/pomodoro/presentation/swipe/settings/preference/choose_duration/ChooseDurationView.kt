package com.flosun.pomodoro.presentation.swipe.settings.preference.choose_duration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.request.Disposable
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.DurationType
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datepicker.DurationPicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDurationView(
    navRoute: NavRoute.ChooseDuration,
    viewModel: ChooseDurationViewModel = hiltViewModel<ChooseDurationViewModel>(),
) {
    val navBackStack = LocalNavBackStack.current
    val database = LocalDatabase.current

    val uiState by viewModel.uiState.collectAsState()
    val setting by viewModel.setting.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var customDurationTime by remember { mutableStateOf("") }

    DisposableEffect(focusRequester) {
        onDispose { focusRequester.freeFocus() }
    }

    LaunchedEffect(setting) {
        if (setting == null) return@LaunchedEffect

        viewModel.initialize(
            navRoute.type,
            setting!!
        )
    }

    fun saveDuration() {
        if (setting == null) return
        scope.launch(Dispatchers.IO) {
            val selectedOption = uiState.selectedOption
            val updatedSetting = when (uiState.durationType) {
                DurationType.DAILY_TARGET_DURATION -> setting!!.copy(dailyTargetMs = selectedOption)
                DurationType.POMODORO_DURATION -> setting!!.copy(pomodoroDuration = selectedOption)
                DurationType.SHORT_BREAK_DURATION -> setting!!.copy(shortBreakDuration = selectedOption)
                DurationType.LONG_BREAK_DURATION -> setting!!.copy(longBreakDuration = selectedOption)
                DurationType.LONG_BREAK_AFTER -> setting!!.copy(longBreakAfter = selectedOption)
                DurationType.AUTO_RESET_POMODORO -> setting!!.copy(autoResetPomodoro = selectedOption)
            }

            database.updateSetting(updatedSetting)
            delay(1000L)
            navBackStack.removeLastOrNull()
        }
    }

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(
                onBack = { navBackStack.removeLastOrNull() },
                title = navRoute.type.title,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White)
            ) {
                uiState
                    .options
                    .sortedBy { it }
                    .forEachIndexed { index, option ->
                        val isSelected = option == uiState.selectedOption
                        val label = when (uiState.durationType) {
                            DurationType.DAILY_TARGET_DURATION,
                            DurationType.POMODORO_DURATION,
                            DurationType.SHORT_BREAK_DURATION,
                            DurationType.LONG_BREAK_DURATION -> "$option Minutes"

                            DurationType.LONG_BREAK_AFTER -> "$option Pomodoros"
                            DurationType.AUTO_RESET_POMODORO -> TimeFuncs.formatTimeAMPM(option)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .rippleEffectClickable { viewModel.onChangedOption(option) }
                                .height(52.dp)
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            if (isSelected) Icon(
                                painter = painterResource(R.drawable.ic_checked),
                                contentDescription = null,
                                tint = Color(0xFF3FA039),
                            )
                        }

                        if (index != uiState.options.lastIndex) HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = Color(0xFFE0E0E0)
                        )
                    }
            }

            CoreButton(
                modifier = Modifier.padding(20.dp),
                onPress = {
                    viewModel.onExpandedCustomDuration()
                    focusRequester.requestFocus()
                },
            ) {
                Text(
                    text = "Custom Duration",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            VerticalDivider(modifier = Modifier.weight(1f))
            TwoOptionActions(
                modifier = Modifier.padding(20.dp),
                okLabel = "Save",
                onOk = ::saveDuration,
            )
        }
    }

    if (uiState.isExpanded) CoreBottomSheet(
        sheetState = sheetState,
        title = "Custom Duration",
        closeBottomSheet = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) viewModel.onDismissCustomDuration()
            }
        },
    ) {
        CoreTextField(
            focusRequester = focusRequester,
            value = customDurationTime,
            onValueChanged = { customDurationTime = it },
            placeholder = when (uiState.durationType) {
                DurationType.DAILY_TARGET_DURATION,
                DurationType.POMODORO_DURATION,
                DurationType.SHORT_BREAK_DURATION,
                DurationType.LONG_BREAK_DURATION -> "E.g. 45"

                DurationType.LONG_BREAK_AFTER -> "E.g. 4"
                DurationType.AUTO_RESET_POMODORO -> "hh:mm in 24-hour format, e.g. 14:30"
            },
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
            onOk = {
                val parsedDuration = when (uiState.durationType) {
                    DurationType.DAILY_TARGET_DURATION,
                    DurationType.POMODORO_DURATION,
                    DurationType.SHORT_BREAK_DURATION,
                    DurationType.LONG_BREAK_DURATION,
                    DurationType.LONG_BREAK_AFTER -> customDurationTime.toIntOrNull()

                    DurationType.AUTO_RESET_POMODORO -> TimeFuncs.parseTimeToMs(customDurationTime)
                }

                if (parsedDuration == null) {
                    Timber.tag(DEBUG_TAG).e("Invalid custom duration: $customDurationTime")
                    return@TwoOptionActions
                }

                focusRequester.freeFocus()
                viewModel.onAddCustomDuration(parsedDuration)
                customDurationTime = ""
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) viewModel.onDismissCustomDuration()
                }
            },
        )
    }
}