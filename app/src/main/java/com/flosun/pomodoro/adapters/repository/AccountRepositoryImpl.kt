package com.flosun.pomodoro.adapters.repository

import com.flosun.pomodoro.adapters.database.AppDatabase
import com.flosun.pomodoro.adapters.database.DatabaseDao
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.DatabaseException
import com.flosun.pomodoro.domain.entities.Account
import com.flosun.pomodoro.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    appDatabase: AppDatabase,
) : AccountRepository {
    private val databaseDao = appDatabase.dao

    override suspend fun findByEmail(email: String): Account? {
        val accountEntity = databaseDao.findAccountByEmail(email)
        return accountEntity?.toEntity()
    }

    override suspend fun create(account: Account): Account {
        val accountEntity = AccountEntity.fromEntity(account)

        val rowAffected = databaseDao.insertAccount(accountEntity)
        if (rowAffected <= 0) {
            throw DatabaseException("Failed to create account")
        }

        return account
    }
}