package com.flosun.pomodoro.presentation.swipe.settings.biometric_authentication

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.core.constants.BIOMETRIC_CREDENTIALS_KEY
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosun.pomodoro.core.services.BiometricCredentials
import com.flosun.pomodoro.core.services.BiometricService
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.EncryptedStorage
import com.flosun.pomodoro.domain.entities.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class BiometricAuthenticationViewModel @Inject constructor(
    private val application: Application,
    private val biometricService: BiometricService,
    private val appStorage: AppStorage,
    private val encryptedStorage: EncryptedStorage,
) : ViewModel() {

    @OptIn(ExperimentalUuidApi::class)
    fun enableBiometricAuth(context: FragmentActivity) = viewModelScope.launch(Dispatchers.IO) {
        val currentAccount = appStorage.readSerializable<Account?>(CURRENT_ACCOUNT_KEY, null)
        if (currentAccount == null) {
            Toast.makeText(
                application.applicationContext,
                "Please log in to your account before setting up biometric authentication.",
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        val results = biometricService.register(
            context = context,
            credentials = BiometricCredentials(
                email = currentAccount.email,
                password = currentAccount.password,
            )
        )

        if (results.first != null) {
            Toast.makeText(
                application.applicationContext,
                "Biometric authentication setup failed. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        Toast.makeText(
            application.applicationContext,
            "Biometric authentication setup successful.",
            Toast.LENGTH_SHORT
        ).show()

        val result =
            encryptedStorage.writeEncryptedMessage(BIOMETRIC_CREDENTIALS_KEY, results.second!!)
        if (!result) {
            Toast.makeText(
                application.applicationContext,
                "Failed to save biometric credentials. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                application.applicationContext,
                "Biometric credentials saved successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}