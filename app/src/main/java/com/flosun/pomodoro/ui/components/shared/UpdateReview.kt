package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture

@Composable
fun UpdateReview(
    stars: Int,
    review: String,
    onChangedStars: (Int) -> Unit,
    onChangedReview: (String) -> Unit,
) {
    Text(
        text = "Review",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (i in 1..11) key("star-$i") {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (i <= stars) Color(0xFFFFC107) else Color(0xFFD0D0D0),
                modifier = Modifier
                    .size(24.dp)
                    .tapGesture { onChangedStars(i) },
            )
        }
    }

    CoreTextField(
        value = review,
        onValueChanged = { onChangedReview(it) },
        maxLines = 4,
        singleLine = false,
        height = 100.dp,
        placeholder = "e.g., This year is amazing, etc.",
        padding = PaddingValues(horizontal = 20.dp)
    )
}