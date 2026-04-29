package com.flosunn.pomodoro.presentation.week_year.year_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage

private val images = listOf(
    "https://i.pinimg.com/736x/58/2c/53/582c5311457ba24a584e41bb94838bfa.jpg",
    "https://i.pinimg.com/736x/b5/2b/f0/b52bf09e381dfed893e523dfc9c6b487.jpg",
    "https://i.pinimg.com/736x/15/b9/c4/15b9c449601075248eb25cbce5179be4.jpg",
)

@Composable
fun YearDetailReward() {
    Text(
        text = "Reward",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF3FA039))
            .padding(12.dp),
    ) {
        Text(
            text = "Kem bơ sầu riêng Phan Văn Hớn \uD83C\uDF66",
            color = Color.White,
            fontSize = 16.sp,
        )
    }

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