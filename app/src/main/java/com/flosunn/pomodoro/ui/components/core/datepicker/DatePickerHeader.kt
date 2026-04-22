package com.flosunn.pomodoro.ui.components.core.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.extensions.noRippleEffectClickable
import com.flosunn.pomodoro.ui.components.core.AnimatedFadeVisibility
import com.flosunn.pomodoro.ui.components.core.CoreButton
import timber.log.Timber


@Composable
fun DatePickerHeader(
    height: Dp,
    title: String,
    isMonthYearViewVisible: Boolean,
    onPressMonthYear: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    onOk: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.noRippleEffectClickable { onPressMonthYear() },
        )

        if (isMonthYearViewVisible) {
            CoreButton(
                modifier = Modifier.width(80.dp),
                cornerRadius = 4.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE9E9E9),
                        Color(0xFFE9E9E9)
                    )
                ),
                onPress = onOk,
            ) {
                Text(
                    text = "Ok",
                    color = Color.Black,
                    fontSize = 16.sp,
                )
            }
        } else {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .noRippleEffectClickable { onPrevious() },
                )

                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_next_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .noRippleEffectClickable { onNext() },
                )
            }
        }
    }
}


