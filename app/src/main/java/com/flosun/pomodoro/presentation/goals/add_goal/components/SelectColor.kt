package com.flosun.pomodoro.presentation.goals.add_goal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.core.functions.ViewFuncs


private val colors = listOf(
    Color(0xFF75D06A),
    Color(0xFF9956DE),
    Color(0xFF1FA7E1),
    Color(0xFF6ED1CF),
    Color(0xFF7274ED),
    Color(0xFFFFB356),
    Color(0xFFFF8B8B),
    Color(0xFFFB96BB),
    Color(0xFFFFA189),
    Color(0xFFCBDD62),
    Color(0xFF89C4FF),
    Color(0xFF3BB076),
)


@Composable
fun SelectColor(onChangedSelectedColor: (Color) -> Unit = {}) {
    val screenWidth = ViewFuncs.screenWidthDp()
    val colorSize = (screenWidth - 40 * 2) / 6

    Text(
        text = "Color",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        colors.subList(0, 6).forEach { color ->
            Box(
                modifier = Modifier
                    .size(colorSize.dp)
                    .clip(CircleShape)
                    .background(color)
                    .rippleEffectClickable { onChangedSelectedColor(color) },
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        colors.subList(6, 12).forEach { color ->
            Box(
                modifier = Modifier
                    .size(colorSize.dp)
                    .clip(CircleShape)
                    .background(color)
                    .rippleEffectClickable { onChangedSelectedColor(color) },
            )
        }
    }
}