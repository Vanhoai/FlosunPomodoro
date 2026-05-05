package com.flosun.pomodoro.ui.components.core

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.cropVertical

data class MenuItem(
    val icon: Int,
    val name: String,
)

@Composable
fun CoreMenuOptions(
    expanded: Boolean,
    onClose: () -> Unit = {},
    options: List<MenuItem> = emptyList(),
    onSelected: (MenuItem) -> Unit = {},
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onClose() },
        containerColor = Color.White,
        offset = DpOffset(x = 0.dp, y = (-20).dp),
        modifier = Modifier.cropVertical(8.dp),
    ) {
        options.forEachIndexed { index, item ->
            key("$index-${item.name}-${item.icon}") {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            tint = Color(0xFF616161),
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    text = {
                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            style = AppTheme.typography.body
                        )
                    },
                    onClick = { onSelected(item) },
                )
            }
        }
    }
}