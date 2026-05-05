package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datepicker.DurationPicker
import com.flosunn.core.libraries.datepicker.TimePicker

@Composable
fun SelectPomodoroDuration(
    selectedPomodoroDuration: Int? = null,
    onChangedSelectedPomodoroDuration: (Int) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = "Pomodoro Duration",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp),
            )
            .rippleEffectClickable { expanded = true }
            .padding(12.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = "$selectedPomodoroDuration Minutes",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    if (expanded) DurationPicker(
        selectedDuration = selectedPomodoroDuration ?: 0,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onDismiss = { expanded = false },
        onChanged = { onChangedSelectedPomodoroDuration(it) }
    )
}