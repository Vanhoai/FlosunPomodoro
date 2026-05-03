package com.flosun.pomodoro.di

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
import javax.inject.Singleton

@Module(
    includes = [
        RepositoryModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            context = application.applicationContext,
            klass = AppDatabase::class.java,
            name = POMODORO_DATABASE,
        ).build()
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