package com.flosun.pomodoro.presentation.auth.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.core.CoreTextField
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.isValidEmail

@Composable
fun AuthForm(navigateToSwipe: () -> Unit = {}) {
    var isHidePasswordField by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isHiddenPassword by remember { mutableStateOf(true) }

    CoreTextField(
        modifier = Modifier.padding(vertical = 10.dp),
        value = email,
        onValueChanged = { email = it },
        placeholder = "example@example.com",
        keyboardType = KeyboardType.Email,
        prefix = {
            Icon(
                painter = painterResource(R.drawable.ic_mail),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp),
                tint = Color(0xFF727272),
            )
        },
        suffix = {
            Icon(
                painter = painterResource(R.drawable.ic_checked),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(20.dp),
                tint = if (email.isValidEmail()) Color(0xFF3FA039) else Color(0xFF727272),
            )
        },
    )

    if (!isHidePasswordField) CoreTextField(
        modifier = Modifier.padding(vertical = 10.dp),
        value = password,
        onValueChanged = { password = it },
        placeholder = "Password",
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isHiddenPassword) PasswordVisualTransformation() else VisualTransformation.None,
        prefix = {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp),
                tint = Color(0xFF727272),
            )
        },
        suffix = {
            Icon(
                painter = painterResource(if (isHiddenPassword) R.drawable.ic_eye_off else R.drawable.ic_eye_on),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { isHiddenPassword = !isHiddenPassword }
                    },
                tint = Color(0xFF727272),
            )
        }
    )

    CoreButton(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp),
        isLoading = false,
        isEnabled = true,
        onPress = { navigateToSwipe() }
    ) {
        Text(
            style = AppTheme.typography.body,
            text = "Continue",
            color = Color.White
        )
    }
}