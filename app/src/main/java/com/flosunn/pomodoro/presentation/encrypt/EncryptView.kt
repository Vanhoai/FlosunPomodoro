package com.flosunn.pomodoro.presentation.encrypt

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.pomodoro.ui.components.core.CoreButton

@Composable
fun EncryptView(
    encryptViewModel: EncryptViewModel = hiltViewModel<EncryptViewModel>()
) {
    val focusManager = LocalFocusManager.current
    val plainText = encryptViewModel.message.collectAsState().value
    val encryptedText = encryptViewModel.encryptedMessage.collectAsState().value
    val decryptedText = encryptViewModel.decryptedMessage.collectAsState().value

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "AES Encryption Example",
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            TextField(
                value = plainText,
                onValueChange = { encryptViewModel.updatePlainText(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Encrypted Text: ${encryptedText?.ciphertext.toString()}",
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Text(
                text = "Decrypted Text: $decryptedText",
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            CoreButton(
                modifier = Modifier.padding(vertical = 20.dp),
                onPress = { encryptViewModel.encrypt() }
            ) {
                Text(
                    text = "Encrypt",
                    color = Color.White
                )
            }

            CoreButton(
                modifier = Modifier,
                onPress = { encryptViewModel.decrypt() }
            ) {
                Text(
                    text = "Decrypt",
                    color = Color.White
                )
            }
        }
    }
}