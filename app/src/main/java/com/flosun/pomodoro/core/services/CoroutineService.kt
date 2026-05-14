package com.flosun.pomodoro.core.services

import android.app.Application
import android.widget.Toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CoroutineService(
    val application: Application,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val job = Job()
    protected val coroutineScope = CoroutineScope(dispatcher + job)

    open fun ioRun(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            block()
        }
    }

    open suspend fun showToast(message: String) = withContext(Dispatchers.Main) {
        Toast.makeText(
            application.applicationContext,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    open fun onDispose() {
        job.cancel()
    }
    
}