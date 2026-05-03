package com.flosun.pomodoro.presentation.week_year.year_detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun YearDetailBanner(
    coverUri: String,
    startTimeMilliseconds: Long = 0L,
    endTimeMilliseconds: Long = 0L,
    currentTimeMilliseconds: Long = 0L,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val progress =
        (currentTimeMilliseconds - startTimeMilliseconds).toFloat() / (endTimeMilliseconds - startTimeMilliseconds)
    val containerWidth = screenWidth - (20.dp * 2) - (12.dp * 2)

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        CoreAsyncImage(
            url = coverUri,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(12.dp),
        ) {
            Text(
                text = "The Beginning",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = TimeFuncs.formatTimeString(currentTimeMilliseconds),
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .width(containerWidth)
                            .height(6.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(containerWidth * progress)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF3FA039)),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (containerWidth * progress) - 6.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF76B173)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3FA039)),
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = TimeFuncs.formatTimeString(startTimeMilliseconds),
                        fontSize = 16.sp,
                        color = Color.White,
                    )

                    Text(
                        text = TimeFuncs.formatTimeString(endTimeMilliseconds),
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}