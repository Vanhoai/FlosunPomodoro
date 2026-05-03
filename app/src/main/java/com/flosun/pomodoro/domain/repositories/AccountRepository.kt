package com.flosun.pomodoro.domain.repositories

import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.domain.entities.Account


interface AccountRepository {
    suspend fun findByEmail(email: String): Account?
    suspend fun create(account: Account): Account
}