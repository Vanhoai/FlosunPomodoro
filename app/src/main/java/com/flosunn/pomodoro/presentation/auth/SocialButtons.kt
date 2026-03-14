package com.flosunn.pomodoro.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun SocialButtons() {
    SocialButton(
        icon = R.drawable.ic_google,
        title = "Continue with Google",
    )

    SocialButton(
        icon = R.drawable.ic_github,
        title = "Continue with GitHub",
    )
}

@Composable
private fun SocialButton(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onPress: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(52.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE1E1E1),
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
        )

        Text(
            text = title,
            fontSize = 16.sp,
        )
    }
}