package com.flosun.pomodoro.presentation.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosunn.core.extensions.rippleEffectClickable

@Composable
fun MapBottomActions(
    selectedAddress: String,
    onChangedSelectedAddress: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onOk: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
    ) {
        CoreTextField(
            value = selectedAddress,
            onValueChanged = onChangedSelectedAddress,
            maxLines = 2,
            singleLine = false,
            height = 80.dp,
            backgroundColor = Color.White,
            placeholder = "e.g., 123 Main St, Springfield",
            onFocusChanged = onFocusChanged,
        )

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .rippleEffectClickable { onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                )
            }

            VerticalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .rippleEffectClickable { onOk() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Ok",
                    fontSize = 16.sp,
                )
            }
        }
    }

}