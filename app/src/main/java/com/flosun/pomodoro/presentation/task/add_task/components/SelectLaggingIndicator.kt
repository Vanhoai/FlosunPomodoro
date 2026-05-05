package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.runtime.Composable
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.ui.components.shared.LabelOptions
import com.flosun.pomodoro.ui.components.shared.SelectBoxMenuOption

@Composable
fun SelectLaggingIndicator(
    selectedLaggingIndicator: LaggingIndicatorEntity? = null,
    laggingIndicators: List<LaggingIndicatorEntity> = emptyList(),
    onChangedSelectedLaggingIndicator: (LaggingIndicatorEntity) -> Unit = {},
) {
    SelectBoxMenuOption(
        title = "Lagging Indicator",
        selectedOption = selectedLaggingIndicator,
        entities = laggingIndicators,
        onChanged = onChangedSelectedLaggingIndicator,
        labelOptions = object : LabelOptions<LaggingIndicatorEntity> {
            override fun selectBoxLabel(entity: LaggingIndicatorEntity): String {
                return entity.name
            }

            override fun menuLabel(entity: LaggingIndicatorEntity): String {
                return entity.name
            }

            override fun noChoiceLabel(): String {
                return "Select An Indicator"
            }
        }
    )
}