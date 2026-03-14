package com.flosunn.pomodoro.ui.components.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CoreAsyncImage(
    modifier: Modifier = Modifier,
    url: Any? = null,
) {
    AsyncImage(
        model = url ?: "https://i.pinimg.com/736x/73/25/8c/73258c9455ed7295530b7e5e30f10a19.jpg",
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        onLoading = {},
    )
}