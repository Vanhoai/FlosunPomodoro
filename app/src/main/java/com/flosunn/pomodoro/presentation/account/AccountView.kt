package com.flosunn.pomodoro.presentation.account

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun AccountView(navBackStack: NavBackStack<NavKey>) {
    val focusManager = LocalFocusManager.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            CommonBackHeading(
                title = "",
                onBack = { navBackStack.removeLastOrNull() }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    CoreAsyncImage(
                        url = "https://i.pinimg.com/736x/8f/09/89/8f0989819d480ca10f335aff379e73f7.jpg",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(
                                width = 4.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }

            Text(
                text = "Full Name",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 8.dp),
            )

            CoreTextField(
                value = fullName,
                onValueChanged = { fullName = it },
                placeholder = "Enter your full name"
            )

            Text(
                text = "Email",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 8.dp),
            )

            CoreTextField(
                value = email,
                onValueChanged = { email = it },
                placeholder = "Enter your email"
            )

            CoreButton(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}