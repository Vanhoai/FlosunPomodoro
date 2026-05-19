package com.flosun.pomodoro.presentation.auth

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.core.constants.BIOMETRIC_CREDENTIALS_KEY
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosun.pomodoro.core.services.BiometricCredentials
import com.flosun.pomodoro.core.services.BiometricService
import com.flosun.pomodoro.core.services.OAuthService
import com.flosun.pomodoro.core.services.SupabaseService
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.EncryptedStorage
import com.flosun.pomodoro.domain.usecases.AuthParams
import com.flosun.pomodoro.domain.usecases.AuthenticateUseCase
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
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
    private val supabaseService: SupabaseService,
    private val encryptedStorage: EncryptedStorage,
    private val appStorage: AppStorage,
    private val globalLoading: GlobalLoading,
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

    suspend fun sigInWithSupabase(
        idToken: String,
        rawNonce: String
    ): Pair<AuthResult?, Exception?> {
        supabaseService.signInWithGoogle(idToken, rawNonce)
        val user = supabaseService.retrieveCurrentUser()
            ?: return null to Exception("No user found after Supabase sign-in")

        Toast.makeText(
            application.applicationContext,
            "Supabase sign-in successful. Welcome, ${user.email ?: "User"}!",
            Toast.LENGTH_SHORT
        ).show()

        return null to Exception("Supabase sign-in failed")
    }

    fun signInWithGoogle() = viewModelScope.launch {
        globalLoading.setLoading(
            isLoading = true,
            message = "Signing in with Google"
        )

        val results = oauthService.oauthGoogle()
        if (results == null) {
            globalLoading.setLoading(false)
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
            globalLoading.setLoading(false)
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
            globalLoading.setLoading(false)
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
                password = uid,
            )
        )

        if (response is BaseResult.Failure) {
            globalLoading.setLoading(false)
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

        val (result1, result2) = coroutineScope {
            val resultSaveAccount = async {
                appStorage.writeSerializable(CURRENT_ACCOUNT_KEY, account)
            }
            
            val resultSaveAccountId = async {
                appStorage.write(CURRENT_ACCOUNT_ID_KEY, account.id)
            }

            resultSaveAccount.await() to resultSaveAccountId.await()
        }

        if (!result1 || !result2) {
            globalLoading.setLoading(false)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to save account information. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        globalLoading.setLoading(false)
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