package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.functions.ViewFuncs

@Composable
fun GoalCard(
    name: String,
    progress: Int,
    modifier: Modifier = Modifier,
) {
    val screenWidth = ViewFuncs.screenWidthDp()
    val containerProgressWidth = screenWidth - 40 * 2 - 12 * 2 - 50 - 12

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF75D06A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_grid),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White,
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .width(containerProgressWidth.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFEBEBEB)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width((containerProgressWidth * (progress / 100f)).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF75D06A)),
                    )
                }

                Text(
                    text = "${progress}%",
                    fontSize = 14.sp,
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}