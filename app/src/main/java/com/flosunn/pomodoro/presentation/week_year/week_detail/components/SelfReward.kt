package com.flosunn.pomodoro.presentation.week_year.week_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage
import com.flosunn.pomodoro.ui.components.core.CoreTextField


private val images = listOf(
    "https://i.pinimg.com/736x/58/2c/53/582c5311457ba24a584e41bb94838bfa.jpg",
    "https://i.pinimg.com/736x/b5/2b/f0/b52bf09e381dfed893e523dfc9c6b487.jpg",
    "https://i.pinimg.com/736x/15/b9/c4/15b9c449601075248eb25cbce5179be4.jpg",
)


@Composable
fun SelfReward() {
    var rewardName by remember { mutableStateOf("Kem bơ sầu riêng Phan Văn Hớn 🍦") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Self-Reward",
            fontSize = 16.sp,
            color = Color.Black,
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color(0xFF3FA039), shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_checked),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp),
            )
        }
    }

    CoreTextField(
        value = rewardName,
        onValueChanged = { rewardName = it },
        maxLines = 4,
        singleLine = false,
        height = 100.dp,
        placeholder = "e.g., Go to travel, buy a new phone, etc.",
        padding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(images) { image ->
            CoreAsyncImage(
                url = image,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}