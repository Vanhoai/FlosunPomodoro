package com.flosunn.core.libraries.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.core.ui.AnimatedWheelColumn

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    viewModel: TimePickerViewModel = hiltViewModel<TimePickerViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        )
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = modifier.height(320.dp),
        ) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            color = Color(0xFFEEEEEE),
                            shape = RoundedCornerShape(8.dp),
                        )
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedWheelColumn(
                        items = uiState.hours,
                        selectedIndex = uiState.selectedHourIndex,
                        onSelectedIndex = viewModel::onChangedHourIndex,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center,
                    )

                    AnimatedWheelColumn(
                        items = uiState.minutes,
                        selectedIndex = uiState.selectedMinuteIndex,
                        onSelectedIndex = viewModel::onChangedMinuteIndex,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center,
                    )

                    AnimatedWheelColumn(
                        items = uiState.designators.map { it.name },
                        selectedIndex = uiState.selectedDesignatorIndex,
                        onSelectedIndex = viewModel::onChangedDesignatorIndex,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}