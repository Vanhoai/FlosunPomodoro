package com.flosun.pomodoro.presentation.goals.update_goal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading

@Composable
fun UpdateGoalView() {
    Scaffold(containerColor = Color.White) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                CommonBackHeading(
                    title = "Week 1",
                    actions = {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFFFF8B8B),
                        )
                    }
                )
            }
        }
    }
}