package com.flosunn.pomodoro.ui.components.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CoreAsyncImage(
    modifier: Modifier = Modifier,
    url: Any? = null,
    onPress: () -> Unit = {},
) {
    AsyncImage(
        model = url ?: "https://i.pinimg.com/736x/73/25/8c/73258c9455ed7295530b7e5e30f10a19.jpg",
        contentDescription = null,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { onPress() }
            ),
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        onLoading = {},
    )
}