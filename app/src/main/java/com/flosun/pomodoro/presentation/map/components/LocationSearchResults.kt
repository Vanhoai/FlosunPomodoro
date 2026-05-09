package com.flosun.pomodoro.presentation.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.domain.values.Location
import com.flosunn.core.extensions.rippleEffectClickable

@Composable
fun ColumnScope.LocationSearchResults(
    locations: List<Location>,
    onSelectedLocation: (Location) -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(top = 12.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            itemsIndexed(
                items = locations,
                key = { index, location ->
                    "$index-${location.latitude},${location.longitude}"
                }
            ) { index, location ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .rippleEffectClickable { onSelectedLocation(location) }
                        .padding(12.dp)
                ) {
                    Text(
                        text = location.address!!,
                        color = Color(0xFF333333),
                        modifier = Modifier.align(Alignment.CenterStart),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                if (index < locations.size - 1) HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                )
            }
        }
    }
}