package com.flosunn.pomodoro.presentation.encrypt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosunn.pomodoro.core.cryptography.Cryptography
import com.flosunn.pomodoro.core.cryptography.EncryptedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EncryptViewModel @Inject constructor(
    private val cryptography: Cryptography,
) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _encryptedMessage = MutableStateFlow<EncryptedMessage?>(null)
    val encryptedMessage = _encryptedMessage.asStateFlow()

    private val _decryptedMessage = MutableStateFlow<String?>(null)
    val decryptedMessage = _decryptedMessage.asStateFlow()

    fun updatePlainText(newText: String) {
        _message.value = newText
    }

    fun encrypt() = viewModelScope.launch(Dispatchers.IO) {
        val encrypted = cryptography.encrypt(plainText = _message.value)
        withContext(Dispatchers.Main) {
            _encryptedMessage.value = encrypted
        }
    }

    fun decrypt() = viewModelScope.launch(Dispatchers.IO) {
        if (_encryptedMessage.value == null) return@launch
        val decrypted = cryptography.decrypt(_encryptedMessage.value!!)
        withContext(Dispatchers.Main) {
            _decryptedMessage.value = decrypted
        }
    }
}