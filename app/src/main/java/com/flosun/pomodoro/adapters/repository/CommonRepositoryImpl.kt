package com.flosun.pomodoro.adapters.repository

import com.flosun.pomodoro.adapters.apis.MapTilerApi
import com.flosun.pomodoro.domain.repositories.CommonRepository
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val mapTilerApi: MapTilerApi,
) : CommonRepository {

    override suspend fun geocodingForward(address: String) = mapTilerApi.geocodingForward(address)

    override suspend fun geocodingReverse(
        longitude: Double,
        latitude: Double
    ) = mapTilerApi.geocodingReverse(longitude, latitude)

}