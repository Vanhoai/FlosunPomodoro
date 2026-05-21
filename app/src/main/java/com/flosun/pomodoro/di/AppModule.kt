package com.flosun.pomodoro.di

import android.R.attr.level
import android.app.Application
import androidx.room.Room
import com.flosun.pomodoro.adapters.database.AppDatabase
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.core.constants.POMODORO_DATABASE
import com.flosun.pomodoro.core.cryptography.Cryptography
import com.flosun.pomodoro.core.cryptography.implementations.AESCryptography
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DataConversion.install
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                })
            }
            install(Logging) { level = LogLevel.ALL }
            install(WebSockets.Plugin) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            context = application.applicationContext,
            klass = AppDatabase::class.java,
            name = POMODORO_DATABASE,
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun providePomodoroDatabase(appDatabase: AppDatabase): PomodoroDatabase {
        return PomodoroDatabase(appDatabase)
    }

    @Provides
    @Singleton
    fun provideCryptography(application: Application): Cryptography {
        return AESCryptography(application)
    }

}