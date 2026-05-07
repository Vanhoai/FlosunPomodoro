package com.flosun.pomodoro.presentation.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.tapGesture

@Composable
fun BottomTabs(
    currentBottomBarScreen: BottomNavRoute,
    onClick: (BottomNavRoute) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        bottomNavRoutes.forEach { destination ->
            if (destination == BottomNavRoute.More) {
                return@forEach Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = -40.dp.toPx()
                        }
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primaryColor)
                        .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                color = AppTheme.colors.primaryColor,
                                alpha = 0.6f,
                                offset = DpOffset(0.dp, 4.dp),
                                radius = 16.dp,
                                spread = 20.dp,
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.title,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .tapGesture { onClick(destination) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.title,
                        tint = if (currentBottomBarScreen == destination) AppTheme.colors.primaryColor else Color(
                            0xFF8C8C8C
                        ),
                        modifier = Modifier.size(26.dp)
                    )

                    Text(
                        text = destination.title,
                        color = if (currentBottomBarScreen == destination) AppTheme.colors.primaryColor else Color(
                            0xFF8C8C8C
                        ),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}