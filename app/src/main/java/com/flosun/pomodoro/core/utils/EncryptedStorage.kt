package com.flosun.pomodoro.core.utils

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.flosun.pomodoro.core.cryptography.Cryptography
import com.flosun.pomodoro.core.cryptography.EncryptedMessage
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedStorage @Inject constructor(
    private val application: Application,
    private val cryptography: Cryptography,
) {
    val json = Json { ignoreUnknownKeys = true }

    suspend fun writeEncryptedMessage(
        key: Preferences.Key<String>,
        encryptedMessage: EncryptedMessage
    ): Boolean {
        return try {
            val jsonValue = json.encodeToString(
                EncryptedMessage.serializer(),
                encryptedMessage
            )
            application.encryptedStore.edit { it[key] = jsonValue }
            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun readEncryptedMessage(key: Preferences.Key<String>): EncryptedMessage? {
        return try {
            val jsonValue = application.encryptedStore[key] ?: return null
            val encryptedMessage = json.decodeFromString(
                EncryptedMessage.serializer(),
                jsonValue
            )

            encryptedMessage
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    suspend fun write(key: Preferences.Key<String>, value: String): Boolean {
        return try {
            val encryptedMessage = cryptography.encrypt(value)
            writeEncryptedMessage(key, encryptedMessage)
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun read(key: Preferences.Key<String>): String? {
        return try {
            val encryptedMessage = readEncryptedMessage(key) ?: return null
            cryptography.decrypt(encryptedMessage)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    suspend fun delete(key: Preferences.Key<String>): Boolean {
        return try {
            application.encryptedStore.edit { it.remove(key) }
            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend fun clear(): Boolean {
        return try {
            application.encryptedStore.edit { it.clear() }
            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend fun contains(key: Preferences.Key<String>): Boolean {
        return try {
            application.encryptedStore.data.firstOrNull()?.contains(key) ?: false
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }
}