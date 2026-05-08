package com.flosun.pomodoro.core.utils.result_store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalResultStore = compositionLocalOf<ResultStore> {
    error("No ResultStore provided")
}

val currentResultStore: ResultStore
    @Composable
    get() = LocalResultStore.current
