package com.flosun.pomodoro.domain.usecases

import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.AccountEntity
import com.flosun.pomodoro.globals.events.GlobalEvent
import com.flosun.pomodoro.globals.events.GlobalEventBus
import com.flosun.pomodoro.core.cryptography.PasswordHasher
import com.flosun.pomodoro.core.utils.BaseUseCase
import com.flosun.pomodoro.core.utils.ValidationException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi

data class AuthParams(
    val name: String,
    val email: String,
    val password: String,
    val avatar: String,
)

@Singleton
class AuthenticateUseCase @Inject constructor(
    private val passwordHasher: PasswordHasher,
    private val globalEventBus: GlobalEventBus,
    private val database: PomodoroDatabase,
) : BaseUseCase<AuthParams, AccountEntity>() {

    override suspend fun execute(params: AuthParams): AccountEntity {
        val account = database.findAccountByEmail(params.email)
        return if (account == null) createNewAccount(params)
        else checkPassword(account, params)
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun createNewAccount(params: AuthParams): AccountEntity {
        val hashedPassword = passwordHasher.hash(params.password)

        val newAccount = AccountEntity(
            name = params.name,
            email = params.email,
            password = hashedPassword,
            avatar = params.avatar,
        )

        val createdResult = database.insertAccount(newAccount)
        if (createdResult <= 0L) throw ValidationException("Failed to create account")

        globalEventBus.sendEvent(GlobalEvent.CreateSetting(accountId = newAccount.id))
        return newAccount
    }

    private fun checkPassword(account: AccountEntity, params: AuthParams): AccountEntity {
        val isPasswordValid = passwordHasher.verify(params.password, account.password)
        if (!isPasswordValid) throw ValidationException("Invalid password")

        return account
    }
}