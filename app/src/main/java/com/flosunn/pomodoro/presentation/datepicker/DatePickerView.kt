package com.flosunn.pomodoro.presentation.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class Message(
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
private val message = Message(
    id = Uuid.toString(),
    title = "Event has been created",
    description = "Sunday, December 24, 2026 at 09:00 AM"
)

@OptIn(ExperimentalUuidApi::class)
private val messages = listOf(
    message,
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
    message.copy(id = Uuid.toString()),
)

@Composable
fun DatePickerView(navBackStack: NavBackStack<NavKey>) {
    Scaffold(containerColor = Color.White) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter,
        ) {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(messages) { message ->
                    FlashMessageCard(message)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CoreButton(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onPress = {}
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
private fun FlashMessageCard(message: Message) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .dropShadow(
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
                block = {
                    radius = 12f
                    color = Color(0xFF000000).copy(alpha = 0.3f)
                    offset = Offset(0f, 4f)
                }
            )
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFEEEEEE),
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(52.dp)
                    .background(
                        color = Color(0xFFCBDD62),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flash_message),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White,
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = message.title,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = message.description,
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
