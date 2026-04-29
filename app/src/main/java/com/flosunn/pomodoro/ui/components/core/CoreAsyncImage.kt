package com.flosunn.pomodoro.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CoreAsyncImage(
    modifier: Modifier = Modifier,
    url: Any? = null,
    onPress: () -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        if (isLoading) Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Gray.copy(alpha = 0.3f))
        )

        AsyncImage(
            model = url
                ?: "https://i.pinimg.com/736x/73/25/8c/73258c9455ed7295530b7e5e30f10a19.jpg",
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = { onPress() }
                ),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
        )
    }
}