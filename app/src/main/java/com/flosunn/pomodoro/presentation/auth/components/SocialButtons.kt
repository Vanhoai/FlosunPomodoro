package com.flosunn.pomodoro.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavGraph
import com.flosunn.pomodoro.ui.theme.AppTheme
import com.flosunn.pomodoro.ui.theme.PomodoroTheme

@Composable
fun SocialButtons(
    signInGoogle: () -> Unit = {},
    signInGithub: () -> Unit = {},
) {
    SocialButton(
        icon = R.drawable.ic_google,
        title = "Continue with Google",
        onPress = signInGoogle,
    )

    SocialButton(
        icon = R.drawable.ic_github,
        title = "Continue with GitHub",
        onPress = signInGithub,
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
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE1E1E1),
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onPress,
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


@Preview
@Composable
fun AuthViewPreview() {
    PomodoroTheme {
        NavGraph()
    }
}