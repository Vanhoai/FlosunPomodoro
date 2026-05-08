package com.flosun.pomodoro.domain.repositories

import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.domain.values.Location
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun geocodingForward(address: String): Flow<BaseResult<List<Location>>>
    suspend fun geocodingReverse(longitude: Double, latitude: Double): Flow<BaseResult<String?>>
}