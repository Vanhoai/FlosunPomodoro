package com.flosunn.pomodoro.presentation.swipe.settings.add_new_year

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.extensions.dashBorder
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun UploadCover(
    coverUri: String? = null,
    onUpload: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onUpload() }
            )
            .height(200.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color.White)
            .dashBorder(
                strokeWidth = 1.dp,
                color = Color(0xFFCCCCCC),
                cornerRadius = 12.dp,
            ),
        contentAlignment = Alignment.Center
    ) {
        if (coverUri != null) {
            CoreAsyncImage(
                url = coverUri,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    tint = Color(0xFF797979),
                    modifier = Modifier.size(24.dp),
                )

                Text(
                    text = "upload cover",
                    fontSize = 16.sp,
                    color = Color(0xFF797979),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}