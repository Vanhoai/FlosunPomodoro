package com.flosun.pomodoro.core.services

import android.app.Application
import com.flosunn.core.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseService @Inject constructor(
    private val application: Application,
) {
    private var client: SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY,
    ) {
        defaultSerializer = KotlinXSerializer(Json { ignoreUnknownKeys = true })

        install(Auth)
        install(Postgrest)
        install(Realtime)
    }

    suspend fun signInWithGoogle(token: String, rawNonce: String): Unit {
        val response = client.auth.signInWith(IDToken) {
            idToken = token
            provider = Google
            nonce = rawNonce
        }

        return response
    }

    fun retrieveCurrentUser(): UserInfo? {
        return client.auth.currentUserOrNull()
    }
}