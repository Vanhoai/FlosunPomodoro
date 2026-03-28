package com.flosunn.pomodoro.presentation.auth

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosunn.pomodoro.core.constants.BIOMETRIC_CREDENTIALS_KEY
import com.flosunn.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosunn.pomodoro.core.services.BiometricCredentials
import com.flosunn.pomodoro.core.services.BiometricService
import com.flosunn.pomodoro.core.services.OAuthService
import com.flosunn.pomodoro.core.utils.AppStorage
import com.flosunn.pomodoro.core.utils.BaseResult
import com.flosunn.pomodoro.core.utils.EncryptedStorage
import com.flosunn.pomodoro.domain.usecases.AuthParams
import com.flosunn.pomodoro.domain.usecases.AuthenticateUseCase
import com.flosunn.pomodoro.ui.components.shared.GlobalLoading
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class AuthNavigationEvent {
    NavigateToMainScreen,
    NavigateToFaceRecognition,
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val application: Application,
    private val oauthService: OAuthService,
    private val biometricService: BiometricService,
    private val encryptedStorage: EncryptedStorage,
    private val appStorage: AppStorage,
    private val authenticateUseCase: AuthenticateUseCase,
) : ViewModel() {

    private val _navigationEvent = Channel<AuthNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    suspend fun checkAuthentication(): Boolean = suspendCancellableCoroutine { continuation ->
        val currentUser = Firebase.auth.currentUser
        continuation.resume(currentUser != null)
    }

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
            return@launch
        }

        val name = authResult?.user?.displayName
        val uid = authResult?.user?.uid
        val email = authResult?.user?.email

        if (name == null || uid == null || email == null) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to retrieve user information. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        val response = authenticateUseCase(
            AuthParams(
                name = name,
                email = email,
                password = uid, // Using UID as a password for simplicity, but in a real app, you should handle this more securely.
            )
        )

        if (response is BaseResult.Failure) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Authentication failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        val account = (response as BaseResult.Success).data

        withContext(Dispatchers.Main) {
            Toast.makeText(
                application.applicationContext,
                "Welcome, ${account.name}!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val result = appStorage.writeSerializable(CURRENT_ACCOUNT_KEY, account)
        if (!result) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to save account information. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        delay(1000)
        _navigationEvent.send(AuthNavigationEvent.NavigateToMainScreen)
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