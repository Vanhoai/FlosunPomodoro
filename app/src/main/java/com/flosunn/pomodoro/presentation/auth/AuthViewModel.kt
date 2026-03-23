package com.flosunn.pomodoro.presentation.auth

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosunn.pomodoro.core.constants.BIOMETRIC_CREDENTIALS_KEY
import com.flosunn.pomodoro.core.services.BiometricCredentials
import com.flosunn.pomodoro.core.services.BiometricService
import com.flosunn.pomodoro.core.services.OAuthService
import com.flosunn.pomodoro.core.utils.EncryptedStorage
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val application: Application,
    private val oauthService: OAuthService,
    private val biometricService: BiometricService,
    private val encryptedStorage: EncryptedStorage,
) : ViewModel() {

    suspend fun signInWithIdToken(
        idToken: String,
        rawNonce: String
    ): Pair<AuthResult?, Exception?> = suspendCancellableCoroutine { continuation ->
        val credential = GoogleAuthProvider.getCredential(idToken, rawNonce)
        Firebase.auth.signInWithCredential(credential)
            .addOnFailureListener { e -> continuation.resume(null to e) }
            .addOnSuccessListener { result -> continuation.resume(result to null) }
    }

    fun signInWithGoogle() = viewModelScope.launch {
        val results = oauthService.oauthGoogle()
        if (results == null) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Google sign-in failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return@launch
        }

        val (idToken, rawNonce) = results
        val (authResult, error) = signInWithIdToken(idToken, rawNonce)
        if (error != null) {
            Timber.tag("AuthViewModel").e(error, "Google sign-in failed")
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    error.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Timber.tag("AuthViewModel").d("Google sign-in successful: ${authResult?.user?.email}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun enableBiometricAuth(context: FragmentActivity) = viewModelScope.launch {
        val results = biometricService.register(
            context = context,
            credentials = BiometricCredentials(
                email = "vanhoai.adv@gmail.com",
                uuid = Uuid.random(),
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

    fun authWithBiometric(context: FragmentActivity) = viewModelScope.launch {
        val encryptedMessage = encryptedStorage.readEncryptedMessage(BIOMETRIC_CREDENTIALS_KEY)
        if (encryptedMessage == null) {
            Toast.makeText(
                application.applicationContext,
                "No biometric credentials found. Please set up biometric authentication first.",
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        val results = biometricService.authenticate(
            context = context,
            messageEncrypted = encryptedMessage,
        )

        if (results.first != null) {
            Toast.makeText(
                application.applicationContext,
                "Biometric authentication failed. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        val credentials = results.second!!
        Toast.makeText(
            application.applicationContext,
            "Biometric authentication successful. Welcome back, ${credentials.email}!",
            Toast.LENGTH_SHORT,
        ).show()
    }
}