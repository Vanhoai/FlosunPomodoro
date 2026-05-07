package com.flosun.pomodoro.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CoreFloatingButton(
    options: List<MenuItem> = emptyList(),
    onOptionSelected: (MenuItem) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(end = 20.dp, bottom = 20.dp)
            .size(60.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.primaryColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { expanded = true },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_grid),
            contentDescription = null,
            tint = Color.White,
        )
        
        CoreMenuOptions(
            expanded = expanded,
            onClose = { expanded = false },
            options = options,
            onSelected = {
                scope.launch {
                    expanded = false
                    delay(500L) // Add a delay to allow the menu to close before performing the action
                    onOptionSelected(it)
                }
            }
        )
    }
}