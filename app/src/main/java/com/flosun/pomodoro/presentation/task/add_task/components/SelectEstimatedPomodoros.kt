package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable

@Composable
fun SelectEstimatedPomodoros(
    estimatedPomodoros: List<Int> = (1..10).toList(),
    selectedEstimatedPomodoro: Int? = null,
    onChangedSelectedEstimatedPomodoro: (Int) -> Unit = {},
) {
    Text(
        text = "Estimated Pomodoro",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(estimatedPomodoros) { index, estimatedPomodoro ->
            val isSelected = selectedEstimatedPomodoro == estimatedPomodoro

            key("$estimatedPomodoro-$index") {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) AppTheme.colors.primaryColor else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) AppTheme.colors.primaryColor else AppTheme.colors.borderColor,
                            shape = CircleShape,
                        )
                        .rippleEffectClickable {
                            onChangedSelectedEstimatedPomodoro(
                                estimatedPomodoro
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = estimatedPomodoro.toString(),
                        fontSize = 16.sp,
                        color = if (isSelected) Color.White else Color.DarkGray,
                    )
                }
            }
        }
    }
}