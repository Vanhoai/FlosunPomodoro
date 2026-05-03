package com.flosun.pomodoro.presentation.week_year.add_new_year.components

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
import com.flosunn.core.extensions.dashed
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.theme.AppTheme
import timber.log.Timber

@Composable
fun UploadCover(
    coverUri: String? = null,
    onUpload: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color(0xFFF9F9F9))
            .rippleEffectClickable { onUpload() }
    ) {
        if (coverUri != null) CoreAsyncImage(
            url = coverUri,
            modifier = Modifier.fillMaxSize(),
            onPress = { onUpload() }
        ) else Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .dashed(
                    strokeWidth = 1.dp,
                    color = Color(0xFFCCCCCC),
                    cornerRadius = 12.dp,
                ),
            contentAlignment = Alignment.Center
        ) {

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