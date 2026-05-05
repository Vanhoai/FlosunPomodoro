package com.flosun.pomodoro.core.utils

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseViewModel @Inject constructor(private val application: Application) : ViewModel() {

    fun ioRun(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

    suspend fun showToast(message: String) = withContext(Dispatchers.Main) {
        Toast.makeText(
            application.applicationContext,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getCurrentAccountId(): String? {
        val accountId = application.datastore[CURRENT_ACCOUNT_ID_KEY]
        return if (accountId.isNullOrEmpty()) {
            null
        } else {
            accountId
        }
    }

    fun getCurrentYearDirectory(yearId: String): String? {
        val accountId = getCurrentAccountId() ?: return null
        val directory = application.applicationContext.filesDir
        return "${directory.absolutePath}/AC-$accountId/YE-$yearId"
    }

    fun getCurrentYearDirectory(accountId: String, yearId: String): String {
        val directory = application.applicationContext.filesDir
        return "${directory.absolutePath}/AC-$accountId/YE-$yearId"
    }

}