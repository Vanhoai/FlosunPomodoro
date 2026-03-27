package com.flosunn.pomodoro.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flosunn.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosunn.pomodoro.core.utils.AppStorage
import com.flosunn.pomodoro.domain.entities.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appStorage: AppStorage
) : ViewModel() {

    suspend fun readCurrentAccount(): Account? {
        val account = appStorage.readSerializable<Account?>(CURRENT_ACCOUNT_KEY, null)
        return account
    }

}