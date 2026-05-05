package com.flosunn.core.libraries.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flosunn.core.ui.AnimatedWheelColumn

@Composable
fun DurationPicker(
    modifier: Modifier = Modifier,
    selectedDuration: Int = 0,
    spacing: Int = 5,
    min: Int = 5,
    max: Int = 120,
    onChanged: (Int) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var times by remember { mutableStateOf((min..max step spacing).toList()) }
    var selectedIndex by remember {
        mutableIntStateOf(
            times
                .indexOfFirst { it >= selectedDuration }.takeIf { it != -1 }
                ?: (times.size / 2))
    }

    LaunchedEffect(spacing, min, max) {
        // Update the times list whenever spacing, min, or max changes
        times = (min..max step spacing).toList()
    }

    // Two-way binding: Update selectedIndex when selected changes, and update selected when selectedIndex changes
    LaunchedEffect(selectedDuration) {
        val index = times.indexOfFirst { it >= selectedDuration }
        selectedIndex = if (index != -1) index else times.size - 1
    }

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
                        items = times.map { it.toString().padStart(2, '0') + " Minutes" },
                        selectedIndex = selectedIndex,
                        onSelectedIndex = {
                            selectedIndex = it
                            onChanged(times[it])
                        },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}