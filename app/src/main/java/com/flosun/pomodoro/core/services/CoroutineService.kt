package com.flosun.pomodoro.core.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class CoroutineService(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val job = Job()
    protected val coroutineScope = CoroutineScope(dispatcher + job)

    fun onDispose() {
        job.cancel()
    }

}