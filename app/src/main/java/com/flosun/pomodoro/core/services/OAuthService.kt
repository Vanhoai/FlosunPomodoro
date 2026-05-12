package com.flosun.pomodoro.core.services

import android.app.Application
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.flosunn.core.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuthService @Inject constructor(
    private val application: Application
) {

    fun generateGoogleRequest(): Triple<CredentialManager, GetCredentialRequest, String> {
        val credentialManager = CredentialManager.create(application.applicationContext)

        // Generate a nonce and hash it with sha-256
        // Providing a nonce is optional but recommended
        val rawNonce = UUID.randomUUID()
            .toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        // Hashed nonce to be passed to Google sign-in
        val hashedNonce = digest.fold("") { str, it ->
            str + "%02x".format(it)
        }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return Triple(credentialManager, request, rawNonce)
    }

    suspend fun oauthGoogle(): Pair<String, String>? {
        val (credentialManager, request, rawNonce) = generateGoogleRequest()
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = application.applicationContext,
            )

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            return Pair(googleIdToken, rawNonce)
        } catch (e: GetCredentialException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Error getting credential: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return null
        } catch (e: GoogleIdTokenParsingException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Error parsing Google ID token: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return null
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "An unexpected error occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return null
        }
    }
}