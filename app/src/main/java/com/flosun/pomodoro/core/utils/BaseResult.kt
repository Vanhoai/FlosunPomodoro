package com.flosun.pomodoro.core.utils

sealed class BaseResult<out T> {
    data class Success<T>(val data: T) : BaseResult<T>()
    data class Failure(val exception: Throwable) : BaseResult<Nothing>()
    data object Loading : BaseResult<Nothing>()
}