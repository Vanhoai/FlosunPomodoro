package com.flosunn.pomodoro.domain.usecases

import com.flosunn.pomodoro.core.cryptography.PasswordHasher
import com.flosunn.pomodoro.core.utils.BaseResult
import com.flosunn.pomodoro.core.utils.BaseUseCase
import com.flosunn.pomodoro.core.utils.ValidationException
import com.flosunn.pomodoro.domain.entities.Account
import com.flosunn.pomodoro.domain.repositories.AccountRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class AuthParams(
    val name: String,
    val email: String,
    val password: String,
)

@Singleton
class AuthenticateUseCase @Inject constructor(
    private val passwordHasher: PasswordHasher,
    private val accountRepository: AccountRepository
) : BaseUseCase<AuthParams, Account>() {

    override suspend fun execute(params: AuthParams): Account {
        val account = accountRepository.findByEmail(params.email)
        return if (account == null) createNewAccount(params)
        else checkPassword(account, params)
    }

    private suspend fun createNewAccount(params: AuthParams): Account {
        val hashedPassword = passwordHasher.hash(params.password)

        val newAccount = Account(
            id = UUID.randomUUID().toString(),
            name = params.name,
            email = params.email,
            password = hashedPassword,
        )

        val createdAccount = accountRepository.create(account = newAccount)
        return createdAccount
    }

    private fun checkPassword(account: Account, params: AuthParams): Account {
        val isPasswordValid = passwordHasher.verify(params.password, account.password)
        if (!isPasswordValid) {
            throw ValidationException("Invalid password")
        }

        return account
    }
}