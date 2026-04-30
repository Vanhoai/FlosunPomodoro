package com.flosunn.pomodoro.core.utils

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStorage @Inject constructor(val application: Application) {
    val json = Json { ignoreUnknownKeys = true }

    suspend fun <T> write(
        key: Preferences.Key<T>,
        value: T,
        isCryptoStore: Boolean = false
    ): Boolean {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            datastore.edit { preferences ->
                when (value) {
                    is String -> preferences[key] = value
                    is Int -> preferences[key] = value
                    is Boolean -> preferences[key] = value
                    is Float -> preferences[key] = value
                    is Long -> preferences[key] = value
                    is Double -> preferences[key] = value
                    else -> throw IllegalArgumentException("Unsupported type: ${value!!::class.java}")
                }
            }

            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend inline fun <reified T> writeSerializable(
        key: Preferences.Key<String>,
        value: T,
        isCryptoStore: Boolean = false
    ): Boolean {
        return try {
            val jsonString = json.encodeToString(value)
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            datastore.edit { preferences ->
                preferences[key] = jsonString
            }

            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend inline fun <reified T> readSerializable(
        key: Preferences.Key<String>,
        defaultValue: T,
        isCryptoStore: Boolean = false
    ): T {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            val preferences = datastore.data.first()
            val jsonString = preferences[key]

            if (jsonString != null) {
                json.decodeFromString<T>(jsonString)
            } else {
                defaultValue
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    suspend fun <T> read(
        key: Preferences.Key<T>,
        defaultValue: T,
        isCryptoStore: Boolean = false
    ): T {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            val preferences = datastore.data.first()
            preferences[key] ?: defaultValue
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    suspend fun clear(isCryptoStore: Boolean = false): Boolean {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            datastore.edit { preferences ->
                preferences.clear()
            }

            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend fun <T> contains(
        key: Preferences.Key<T>,
        isCryptoStore: Boolean = false
    ): Boolean {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            val preferences = datastore.data.first()
            preferences.contains(key)
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend fun <T> remove(
        key: Preferences.Key<T>,
        isCryptoStore: Boolean = false
    ): Boolean {
        return try {
            val datastore = if (isCryptoStore) application.encryptedStore else application.datastore
            datastore.edit { preferences ->
                preferences.remove(key)
            }

            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }
}