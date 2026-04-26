package com.flosunn.core.libraries.datepicker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.R
import com.flosunn.core.extensions.noRippleEffectClickable
import com.flosunn.core.ui.AnimatedFadeVisibility

@Composable
fun DatePickerHeading(
    height: Dp,
    title: String,
    isMonthYearViewVisible: Boolean,
    onPressMonthYear: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
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

        AnimatedFadeVisibility(!isMonthYearViewVisible) {
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