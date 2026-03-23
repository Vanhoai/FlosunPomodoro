package com.flosunn.pomodoro.ui.components.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage

@Composable
fun SharedSwipeHeading(onPressAvatar: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(48.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Pomodoro",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Good morning 🌤️",
                fontSize = 14.sp,
            )
        }

        CoreAsyncImage(
            url = "https://i.pinimg.com/736x/af/eb/d6/afebd6b97c70b03546199db58732d1df.jpg",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp)),
            onPress = { onPressAvatar() }
        )
    }
}