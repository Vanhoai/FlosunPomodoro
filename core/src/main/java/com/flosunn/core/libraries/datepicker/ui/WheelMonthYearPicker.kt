package com.flosunn.core.libraries.datepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flosunn.core.ui.AnimatedWheelColumn


@Composable
fun WheelMonthYearPicker(
    selectedMonthIndex: Int,
    selectedYearIndex: Int,
    onSelectedMonthIndex: (Int) -> Unit,
    onSelectedYearIndex: (Int) -> Unit,
    years: List<String>,
    months: List<String>,
    modifier: Modifier = Modifier,
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
                items = months,
                selectedIndex = selectedMonthIndex,
                onSelectedIndex = onSelectedMonthIndex,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )

            AnimatedWheelColumn(
                items = years,
                selectedIndex = selectedYearIndex,
                onSelectedIndex = onSelectedYearIndex,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}