package com.flosun.pomodoro.adapters.database

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.sqlite.db.SupportSQLiteOpenHelper

class PomodoroDatabase(
    private val delegate: AppDatabase,
) : DatabaseDao by delegate.dao {
    val openHelper: SupportSQLiteOpenHelper
        get() = delegate.openHelper

    fun query(block: PomodoroDatabase.() -> Unit) = with(delegate) {
        queryExecutor.execute {
            block(this@PomodoroDatabase)
        }
    }

    fun transaction(block: PomodoroDatabase.() -> Unit) = with(delegate) {
        transactionExecutor.execute {
            runInTransaction {
                block(this@PomodoroDatabase)
            }
        }
    }

    fun close() = delegate.close()
}

val LocalDatabase = staticCompositionLocalOf<PomodoroDatabase> {
    error("Please provide Pomodoro Database before accessing it")
}