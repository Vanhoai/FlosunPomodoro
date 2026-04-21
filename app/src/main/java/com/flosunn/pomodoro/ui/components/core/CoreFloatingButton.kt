package com.flosunn.pomodoro.ui.components.core

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.swipe.tasks.cropVertical

@Composable
fun CoreFloatingButton() {
    var expanded by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .padding(end = 20.dp)
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFFF6767))
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White,
            offset = DpOffset(x = 0.dp, y = (-20).dp),
            modifier = Modifier.cropVertical(8.dp),
        ) {
            Column(
                modifier = Modifier.width(200.dp),
            ) {
                MenuOption(icon = R.drawable.ic_add, name = "Add Task")
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFFF6F6F6),
                )

                MenuOption(icon = R.drawable.ic_tag, name = "Manage Tags")
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFFF6F6F6),
                )

                MenuOption(icon = R.drawable.ic_database, name = "Manage Goals")
            }
        }
    }
}


@Composable
fun MenuOption(
    icon: Int,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {}
            )
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFF616161),
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = name,
            fontSize = 16.sp,
            color = Color(0xFF616161),
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}