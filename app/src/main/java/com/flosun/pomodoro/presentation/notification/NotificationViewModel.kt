package com.flosun.pomodoro.presentation.notification

import android.app.Application
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    application: Application,
) : BaseViewModel(application) {
}