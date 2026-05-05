package com.flosun.pomodoro.presentation.task.add_task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable


@Composable
fun SelectTags(
    tags: List<String> = emptyList(),
    selectedTags: List<String> = emptyList(),
    onSelect: (String, Boolean) -> Unit = { _, _ -> },
) {
    Text(
        text = "Tags",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(tags) { index, tag ->
            val isSelected = selectedTags.contains(tag)

            key("$tag-$index") {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) AppTheme.colors.primaryColor else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) AppTheme.colors.primaryColor else AppTheme.colors.borderColor,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .rippleEffectClickable { onSelect(tag, !isSelected) }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = tag,
                        fontSize = 16.sp,
                        color = if (isSelected) Color.White else Color.DarkGray,
                    )
                }
            }
        }
    }
}