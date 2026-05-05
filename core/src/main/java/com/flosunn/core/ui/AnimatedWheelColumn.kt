package com.flosunn.core.ui

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlin.math.abs

private val ITEM_HEIGHT = 40.dp
private const val VISIBLE_ITEMS = 5

@Composable
fun AnimatedWheelColumn(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    val itemHeightPx = with(LocalDensity.current) { ITEM_HEIGHT.toPx() }
    val listState = rememberLazyListState(selectedIndex)
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val centerIndex = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo

            if (visibleItems.isEmpty()) return@derivedStateOf selectedIndex
            val centerY = layoutInfo.viewportEndOffset / 2

            visibleItems.minByOrNull { item ->
                abs(item.offset + item.size / 2 - centerY)
            }?.index ?: selectedIndex
        }
    }

    LaunchedEffect(centerIndex.value) {
        val realIndex = centerIndex.value - 2
        if (realIndex in items.indices && realIndex != selectedIndex) onSelectedIndex(realIndex)
    }

    LazyColumn(
        state = listState,
        flingBehavior = snapBehavior,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.height(ITEM_HEIGHT * VISIBLE_ITEMS),
    ) {
        items(2) {
            Spacer(modifier = Modifier.height(ITEM_HEIGHT))
        }

        items(items.size) { index ->
            WheelItem(
                text = items[index],
                index = index + 2,
                listState = listState,
                textAlign = textAlign,
                itemHeightPx = itemHeightPx,
            )
        }

        items(2) {
            Spacer(modifier = Modifier.height(ITEM_HEIGHT))
        }
    }
}


@Composable
fun WheelItem(
    text: String,
    index: Int,
    listState: LazyListState,
    itemHeightPx: Float,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(ITEM_HEIGHT)
            .graphicsLayer {
                val layoutInfo = listState.layoutInfo
                val visibleItems = layoutInfo.visibleItemsInfo
                val itemInfo = visibleItems.firstOrNull { it.index == index }

                if (itemInfo == null) return@graphicsLayer

                val centerY = layoutInfo.viewportEndOffset / 2f
                val itemCenterY = itemInfo.offset + itemInfo.size / 2f

                // calculate distance from center
                val distanceFromCenter = abs(itemCenterY - centerY) / itemHeightPx

                // scale: center = 1.0, 1 item away = 0.8, 2 items away = 0.6
                val scale = lerp(1f, 0.6f, distanceFromCenter.coerceIn(0f, 2f) / 2f)

                // alpha: center = 1.0, 1 item away = 0.7, 2 items away = 0.4
                val itemAlpha = lerp(1f, 0.4f, distanceFromCenter.coerceIn(0f, 2f) / 2f)

                scaleX = scale
                scaleY = scale
                alpha = itemAlpha
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            textAlign = textAlign,
            maxLines = 1,
            modifier = Modifier,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
