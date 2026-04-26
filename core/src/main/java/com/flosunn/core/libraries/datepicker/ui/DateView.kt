package com.flosunn.core.libraries.datepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import kotlinx.datetime.LocalDate

@Composable
fun DateView(
    days: List<LocalDate?>,
    dayNames: List<String>,
    modifier: Modifier = Modifier,
    selectedDayOfMonth: Int? = null,
    onDaySelected: (Int) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        itemsIndexed(dayNames) { index, day ->
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = day,
                    color = if (index == dayNames.size - 1) Color.Red else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        itemsIndexed(days) { index, date ->
            if (date == null) return@itemsIndexed

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (date.day == selectedDayOfMonth) Color(0xFF3FA039)
                        else Color.Transparent
                    )
                    .rippleEffectClickable { onDaySelected(date.day) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = date.day.toString(),
                    color = if (date.day == selectedDayOfMonth) Color.White else if (index % 7 == 6) Color.Red else Color.Black,
                    fontSize = 14.sp,
                )
            }
        }
    }
}
