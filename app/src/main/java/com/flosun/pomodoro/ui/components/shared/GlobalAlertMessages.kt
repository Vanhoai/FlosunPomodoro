package com.flosun.pomodoro.ui.components.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.tapGesture
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class AlertMessage @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val title: String,
    val description: String,
    val icon: Int? = null,
    val onCancel: (() -> Unit)? = null,
    val onConfirm: (() -> Unit)? = null,
)

@Singleton
class AlertMessageManager @Inject constructor() {
    private val _message = mutableStateOf<AlertMessage?>(null)
    val message: AlertMessage? get() = _message.value

    fun showMessage(
        title: String = "Notification",
        description: String,
        icon: Int? = R.drawable.ic_message,
        onCancel: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
    ) {
        _message.value = AlertMessage(
            title = title,
            description = description,
            icon = icon,
            onCancel = onCancel,
            onConfirm = onConfirm,
        )
    }

    fun dismissMessage() {
        _message.value = null
    }
}

val LocalAlertMessageManager = compositionLocalOf<AlertMessageManager> {
    error("No AlertMessageManager provided")
}

@Composable
fun rememberAlertMessageManager(): AlertMessageManager {
    return LocalAlertMessageManager.current
}

@Composable
fun GlobalAlertMessages(modifier: Modifier = Modifier) {
    val alertMessageManager = rememberAlertMessageManager()

    AnimatedVisibility(
        visible = alertMessageManager.message != null,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
                .zIndex(Float.MAX_VALUE)
                // This animated will be applied to the content inside the Box
                .animateEnterExit(
                    enter = slideInVertically(initialOffsetY = { -40 }),
                    exit = slideOutVertically(targetOffsetY = { 40 })
                ),
            contentAlignment = Alignment.Center
        ) {
            val message = alertMessageManager.message ?: return@Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(20.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (message.icon != null) Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 20.dp)
                            .size(80.dp)
                            .dropShadow(
                                shape = CircleShape,
                                shadow = Shadow(
                                    radius = 10.dp,
                                    spread = 6.dp,
                                    color = AppTheme.colors.primaryColor.copy(alpha = 0.3f),
                                    offset = DpOffset(x = 2.dp, 2.dp)
                                )
                            )
                            .background(
                                color = AppTheme.colors.primaryColor,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(message.icon),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White,
                        )
                    }

                    Text(
                        text = message.title,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = message.description,
                        fontSize = 14.sp,
                        color = Color(0xFF676767),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        textAlign = TextAlign.Center,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Ok",
                                fontSize = 16.sp,
                                color = AppTheme.colors.primaryColor,
                                modifier = Modifier
                                    .width(80.dp)
                                    .tapGesture {
                                        alertMessageManager.dismissMessage()
                                        message.onConfirm?.invoke()
                                    },
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text = "Cancel",
                                fontSize = 16.sp,
                                color = Color(0xFF676767),
                                modifier = Modifier.tapGesture {
                                    alertMessageManager.dismissMessage()
                                    message.onCancel?.invoke()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}