package com.flosunn.pomodoro.core.services

import android.app.Application
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.flosunn.pomodoro.core.cryptography.Cryptography
import com.flosunn.pomodoro.core.cryptography.EncryptedMessage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.Serializable
import timber.log.Timber
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.json.Json

@Serializable
data class BiometricCredentials @OptIn(ExperimentalUuidApi::class) constructor(
    val email: String,
    val password: String,
)


@Singleton
class BiometricService @Inject constructor(
    private val application: Application,
    private val cryptography: Cryptography,
) {
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(application.applicationContext)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    // Create BiometricPrompt.PromptInfo with customized display text
    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setDescription("Please authenticate to access your credentials")
            .setConfirmationRequired(false)
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun createBiometricPrompt(
        activity: FragmentActivity,
        onAuthFailed: (String) -> Unit = { _ -> },
        onAuthSucceed: (BiometricPrompt.AuthenticationResult) -> Unit,
    ): BiometricPrompt {
        val biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                // Handle successful authentication
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Timber.tag("AuthViewModel")
                        .d("Authentication Succeeded: ${result.cryptoObject}")
                    // Execute custom action on successful authentication
                    onAuthSucceed(result)
                }

                // Handle authentication errors
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Timber.tag("AuthViewModel").d("Authentication Error: $errorCode $errString")
                    onAuthFailed(errString.toString())
                }

                // Handle authentication failures
                override fun onAuthenticationFailed() {
                    Timber.tag("AuthViewModel").d("Authentication Failed")
                    onAuthFailed("Authentication Failed")
                }
            }
        )

        return biometricPrompt
    }

    private fun authenticateBiometric(
        context: FragmentActivity,
        cipher: Cipher,
        onAuthFailed: (String) -> Unit = { _ -> },
        onAuthSuccess: (Cipher) -> Unit
    ) {
        val biometricPrompt = createBiometricPrompt(context, onAuthFailed) { authResult ->
            authResult.cryptoObject?.cipher.let { cipher ->
                if (cipher != null) {
                    onAuthSuccess(cipher)
                } else {
                    onAuthFailed("Cipher is null")
                }
            }
        }

        val promptInfo = createBiometricPromptInfo()
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    suspend fun register(
        context: FragmentActivity,
        credentials: BiometricCredentials,
    ): Pair<String?, EncryptedMessage?> = suspendCancellableCoroutine { continuation ->
        val cipher = cryptography.initializeCipherEncryption()
        authenticateBiometric(
            context,
            cipher,
            onAuthFailed = { continuation.resumeWith(Result.success(it to null)) }
        ) { cipher ->
            val json = Json.encodeToString(
                serializer = BiometricCredentials.serializer(),
                value = credentials,
            )

            val message = cryptography.encryptWithCipher(cipher, json)
            continuation.resumeWith(Result.success(null to message))
        }
    }

    suspend fun authenticate(
        context: FragmentActivity,
        messageEncrypted: EncryptedMessage
    ): Pair<String?, BiometricCredentials?> = suspendCancellableCoroutine { continuation ->
        val cipher = cryptography.initializeCipherDecryption(messageEncrypted.extraBytes)
        authenticateBiometric(
            context,
            cipher,
            onAuthFailed = { continuation.resumeWith(Result.success(it to null)) }
        ) { cipher ->
            val decryptedText = cryptography.decryptWithCipher(cipher, messageEncrypted)
            val credentials = Json.decodeFromString<BiometricCredentials>(decryptedText)
            continuation.resumeWith(Result.success(null to credentials))
        }
    }
}