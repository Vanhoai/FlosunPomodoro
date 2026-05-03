package com.flosun.pomodoro.presentation.account

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun AccountView(
    navBackStack: NavBackStack<NavKey>,
    accountViewModel: AccountViewModel = hiltViewModel<AccountViewModel>()
) {
    val focusManager = LocalFocusManager.current
    val coroutine = rememberCoroutineScope()

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

            CoreButton(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                onPress = {
                    coroutine.launch {
                        val account = accountViewModel.readCurrentAccount()
                        Timber.tag("AccountView").d("Current account: $account")
                    }
                },
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}