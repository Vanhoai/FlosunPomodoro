package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.components.core.CoreBadge
import com.flosunn.core.extensions.upperFirstCharAndLowerRest
import kotlinx.datetime.DayOfWeek

enum class RepetitionOption(val displayName: String) {
    ONCE("Once"),
    DAILY("Daily"),
    CUSTOM("Custom"),
}

@Composable
fun SelectRepetition(
    selectedOption: RepetitionOption? = null,
    repetitionOptions: List<RepetitionOption> = RepetitionOption.entries,
    days: List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY,
    ),
    selectedDays: List<DayOfWeek> = emptyList(),
    onSelectRepetitionOption: (RepetitionOption) -> Unit = {},
    onSelectDay: (DayOfWeek, Boolean) -> Unit = { _, _ -> },
) {
    Text(
        text = "Repetition",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(repetitionOptions) { index, option ->
            val isSelected = option == selectedOption

            key("${option.name}-$index") {
                CoreBadge(
                    isSelected = isSelected,
                    name = option.displayName,
                    onClick = { onSelectRepetitionOption(option) },
                )
            }
        }
    }

    if (selectedOption == RepetitionOption.CUSTOM) AnimatedVisibility(
        visible = true,
        enter = expandVertically() + scaleIn(initialScale = 0.2f),
        exit = shrinkVertically() + scaleOut(targetScale = 0.9f),
    ) {
        Column {
            Text(
                text = "Select Custom Days",
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
                itemsIndexed(days) { index, day ->
                    val isSelected = selectedDays.contains(day)
                    val label = day.name.substring(0, 3).upperFirstCharAndLowerRest()

                    key("${day.name}-$index") {
                        CoreBadge(
                            isSelected = isSelected,
                            name = label,
                            onClick = { onSelectDay(day, !isSelected) },
                        )
                    }
                }
            }
        }
    }
}