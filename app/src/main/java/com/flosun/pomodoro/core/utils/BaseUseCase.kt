package com.flosun.pomodoro.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object NoParams

interface UseCase<in P, out R> {
    suspend operator fun invoke(params: P): BaseResult<R>
}

interface FlowUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<BaseResult<R>>
}

abstract class BaseUseCase<in P, out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<P, R> {
    override suspend fun invoke(params: P): BaseResult<R> {
        return try {
            withContext(dispatcher) {
                BaseResult.Success(execute(params))
            }
        } catch (exception: Exception) {
            BaseResult.Failure(exception)
        }
    }

    protected abstract suspend fun execute(params: P): R
}

abstract class BaseFlowUseCase<in P, out R>(
    private val dispatcher: CoroutineDispatcher
) : FlowUseCase<P, R> {

    override fun invoke(params: P): Flow<BaseResult<R>> {
        return execute(params)
            .catch { emit(BaseResult.Failure(it as Exception)) }
            .flowOn(dispatcher)
    }

    protected abstract fun execute(params: P): Flow<BaseResult<R>>
}

