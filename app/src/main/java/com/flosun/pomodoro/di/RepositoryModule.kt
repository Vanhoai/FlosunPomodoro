package com.flosun.pomodoro.di

import com.flosun.pomodoro.adapters.repository.AccountRepositoryImpl
import com.flosun.pomodoro.adapters.repository.CommonRepositoryImpl
import com.flosun.pomodoro.domain.repositories.AccountRepository
import com.flosun.pomodoro.domain.repositories.CommonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCommonRepository(commonRepositoryImpl: CommonRepositoryImpl): CommonRepository

}
