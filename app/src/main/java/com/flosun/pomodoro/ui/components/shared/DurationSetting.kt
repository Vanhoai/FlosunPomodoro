package com.flosun.pomodoro.ui.components.shared

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datepicker.DatePicker
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.datetime.LocalDate

private enum class PickTimeType {
    START_TIME,
    END_TIME,
}

@Composable
fun DurationSetting(
    isEnabled: Boolean = true,
    startTimeMilliseconds: Long, // Milliseconds since epoch
    endTimeMilliseconds: Long, // Milliseconds since epoch
    onStartTimeChanged: (Long) -> Unit,
    onEndTimeChanged: (Long) -> Unit,
) {
    val context = LocalContext.current

    var isShowDatePicker by remember { mutableStateOf(false) }
    var currentDate by remember { mutableStateOf<LocalDate?>(null) }
    var pickTimeType by remember { mutableStateOf<PickTimeType?>(null) }

    val startTimeString = TimeFuncs.formatTimeString(startTimeMilliseconds)
    val endTimeString = TimeFuncs.formatTimeString(endTimeMilliseconds)

    Text(
        text = "Duration",
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.borderColor,
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                )
                .rippleEffectClickable {
                    if (!isEnabled) return@rippleEffectClickable

                    val dateTime = TimeFuncs.dateTimeFromMilliseconds(startTimeMilliseconds)
                    currentDate = dateTime.date
                    isShowDatePicker = true
                    pickTimeType = PickTimeType.START_TIME
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = startTimeString,
                fontSize = 16.sp,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .height(1.dp)
                .weight(1f),
            color = Color(0xFFCCCCCC)
        )

        Box(
            modifier = Modifier
                .height(48.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.borderColor,
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                )
                .rippleEffectClickable {
                    if (!isEnabled) return@rippleEffectClickable

                    val dateTime = TimeFuncs.dateTimeFromMilliseconds(endTimeMilliseconds)
                    currentDate = dateTime.date
                    isShowDatePicker = true
                    pickTimeType = PickTimeType.END_TIME
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = endTimeString,
                fontSize = 16.sp,
            )
        }
    }

    if (isShowDatePicker) DatePicker(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        date = currentDate,
        onDismiss = {
            isShowDatePicker = false
            pickTimeType = null
        },
        onDateSelected = { year, month, day ->
            if (pickTimeType == null) return@DatePicker

            if (pickTimeType == PickTimeType.START_TIME) {
                val selectedTime = TimeFuncs.convertToMilliseconds(
                    year = year,
                    month = month,
                    day = day,
                    hour = 0,
                    minute = 0,
                    second = 0
                )

                onStartTimeChanged(selectedTime)
                pickTimeType = null
                return@DatePicker
            }

            // validate end time must be after start time and the period between them must be 12 weeks
            val selectedTime = TimeFuncs.convertToMilliseconds(
                year = year,
                month = month,
                day = day,
                hour = 23,
                minute = 59,
                second = 59
            )

            onEndTimeChanged(selectedTime)
            pickTimeType = null
        }
    )
}