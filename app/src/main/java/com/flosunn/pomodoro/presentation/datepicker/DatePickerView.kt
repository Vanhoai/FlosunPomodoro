package com.flosunn.pomodoro.presentation.datepicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme
import timber.log.Timber
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Message(
    val id: String,
    val title: String,
    val description: String,
) {
    fun copy(
        id: String = this.id,
        title: String = this.title,
        description: String = this.description,
    ) = Message(id, title, description)
}

@OptIn(ExperimentalUuidApi::class)
val message = Message(
    id = Uuid.toString(),
    title = "Event has been created",
    description = "Sunday, December 24, 2026 at 09:00 AM"
)

@OptIn(ExperimentalUuidApi::class)
val messages = listOf(
    message,
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
)

@OptIn(ExperimentalUuidApi::class)
@Composable
fun DatePickerView(navBackStack: NavBackStack<NavKey>) {
    var messages by remember { mutableStateOf(emptyList<Message>()) }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter,
        ) {
            FlashStackMessage(messages = messages)

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CoreButton(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onPress = {
                        val newMessage = Message(
                            id = Uuid.toString(),
                            title = "Event has been created",
                            description = "Sunday, December 24, 2026 at 09:00 AM"
                        )

                        messages = messages + newMessage
                    }
                ) {
                    Text(
                        text = "Add Flash Message",
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun FlashStackMessage(
    messages: List<Message>,
    modifier: Modifier = Modifier,
) {
    val maxVisible = 3
    val visible = messages.takeLast(maxVisible)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 40.dp),
    ) {
        visible.forEachIndexed { indexFromOldest, message ->
            val fromTop = visible.size - 1 - indexFromOldest
            val scaleX = 1f - fromTop * 0.04f
            val offsetY = (fromTop * -8).dp

            Timber.tag(DEBUG_TAG).d("Message ${message.id}")
            Timber.tag(DEBUG_TAG).d("  scaleX: $scaleX")
            Timber.tag(DEBUG_TAG).d("  offsetY: $offsetY")


            key(message.id) {
                AnimatedFlashMessageCard(
                    message = message,
                    scaleX = scaleX,
                    offsetY = offsetY,
                    zIndex = indexFromOldest.toFloat(),
                    isTop = fromTop == 0
                )
            }
        }
    }
}

@Composable
fun AnimatedFlashMessageCard(
    message: Message,
    scaleX: Float,
    offsetY: androidx.compose.ui.unit.Dp,
    zIndex: Float,
    isTop: Boolean
) {
    val animatedScale by animateFloatAsState(
        targetValue = scaleX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Scale_${message.id}"
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "OffsetY_${message.id}"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isTop) 1f else 0.7f,
        animationSpec = tween(300),
        label = "Alpha_${message.id}"
    )

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(200)),
        modifier = Modifier
            .zIndex(zIndex)
            .offset(y = animatedOffsetY)
    ) {
        FlashMessageCard(
            message = message,
            modifier = Modifier
                .fillMaxWidth()
                .scale(scaleX = animatedScale, scaleY = 1f)
                .graphicsLayer { alpha = animatedAlpha }
        )
    }
}

@Composable
fun FlashMessageCard(
    message: Message,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFA8C83A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flash_message),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = message.title,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )

                Text(
                    text = message.description,
                    fontSize = 14.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
