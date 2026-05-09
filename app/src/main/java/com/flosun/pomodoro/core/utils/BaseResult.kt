package com.flosun.pomodoro.core.utils


sealed class BaseResult<out T> {
    data class Success<T>(val data: T) : BaseResult<T>()
    data class Failure(val exception: Exception) : BaseResult<Nothing>()
    data object Loading : BaseResult<Nothing>()
}

fun BaseResult<*>.isFailure(): Boolean = this is BaseResult.Failure
fun BaseResult<*>.isSuccess(): Boolean = this is BaseResult.Success