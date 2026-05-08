package com.flosun.pomodoro.core.utils.result_store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable


class ResultStore {

    private val results = mutableMapOf<Any, Any?>()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: Any): T? = results[key] as? T

    fun <T> set(key: Any, value: T) {
        results[key] = value
    }

    fun remove(key: Any) {
        results.remove(key)
    }

    companion object {
        val saver = Saver<ResultStore, Map<Any, Any?>>(
            save = { it.results.toMap() },
            restore = { ResultStore().apply { results.putAll(it) } }
        )
    }
}

@Composable
fun rememberResultStore() = rememberSaveable(saver = ResultStore.saver) {
    ResultStore()
}