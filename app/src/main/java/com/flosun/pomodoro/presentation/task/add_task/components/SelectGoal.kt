package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.runtime.Composable
import com.flosun.pomodoro.adapters.database.entities.GoalEntity
import com.flosun.pomodoro.ui.components.shared.LabelOptions
import com.flosun.pomodoro.ui.components.shared.SelectBoxMenuOption

@Composable
fun SelectGoal(
    selectedGoal: GoalEntity? = null,
    goals: List<GoalEntity> = emptyList(),
    onChangedSelectedGoal: (GoalEntity) -> Unit = {},
) {
    SelectBoxMenuOption(
        title = "Goal",
        selectedOption = selectedGoal,
        entities = goals,
        onChanged = onChangedSelectedGoal,
        labelOptions = object : LabelOptions<GoalEntity> {
            override fun selectBoxLabel(entity: GoalEntity): String {
                return entity.name
            }

            override fun menuLabel(entity: GoalEntity): String {
                return entity.name
            }

            override fun noChoiceLabel(): String {
                return "Select Goal"
            }
        }
    )
}