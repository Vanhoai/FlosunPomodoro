package com.flosunn.pomodoro.domain.repositories

import com.flosunn.pomodoro.core.utils.BaseResult
import com.flosunn.pomodoro.domain.entities.Account


interface AccountRepository {
    suspend fun findByEmail(email: String): Account?
    suspend fun create(account: Account): Account
}