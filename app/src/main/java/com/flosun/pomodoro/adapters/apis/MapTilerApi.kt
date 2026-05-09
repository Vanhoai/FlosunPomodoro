package com.flosun.pomodoro.adapters.apis

import com.flosun.pomodoro.adapters.apis.interceptors.MapTilerInterceptor
import com.flosun.pomodoro.adapters.apis.models.FeatureCollection
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.DomainException
import com.flosun.pomodoro.core.utils.NetworkException
import com.flosun.pomodoro.core.utils.api.BaseApi
import com.flosun.pomodoro.core.utils.api.FailureApiException
import com.flosun.pomodoro.domain.values.Location
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.maplibre.compose.expressions.dsl.feature
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapTilerApi @Inject constructor(
    httpClient: HttpClient,
    mapTilerInterceptor: MapTilerInterceptor,
) : BaseApi(httpClient, mapTilerInterceptor) {

    private val baseUrl = "https://api.maptiler.com/geocoding"

    fun geocodingForward(address: String): Flow<BaseResult<List<Location>>> = flow {
        emit(BaseResult.Loading)
        try {
            val featureCollection = run<FeatureCollection>(
                method = HttpMethod.Get,
                url = "$baseUrl/$address.json",
            ) {
                applyInterceptors(MapTilerInterceptor::class.java.simpleName)
                parameter("country", "vn")
                parameter("language", "vi")
            }

            emit(BaseResult.Success(mapFeatureCollectionToLocations(featureCollection)))
        } catch (exception: FailureApiException) {
            emit(BaseResult.Failure(NetworkException(exception.message)))
        }
    }

    fun geocodingReverse(longitude: Double, latitude: Double): Flow<BaseResult<String?>> = flow {
        emit(BaseResult.Loading)
        try {
            val featureCollection = run<FeatureCollection>(
                method = HttpMethod.Get,
                url = "$baseUrl/$longitude,$latitude.json",
            ) {
                applyInterceptors(MapTilerInterceptor::class.java.simpleName)
                parameter("country", "vn")
                parameter("language", "vi")
            }


            val locations = mapFeatureCollectionToLocations(featureCollection)
            if (locations.isEmpty()) {
                emit(BaseResult.Success(null))
                return@flow
            }

            emit(BaseResult.Success(locations.first().address))
        } catch (exception: FailureApiException) {
            emit(BaseResult.Failure(NetworkException(exception.message)))
        }
    }

    private fun mapFeatureCollectionToLocations(featureCollection: FeatureCollection): List<Location> {
        if (featureCollection.features.isEmpty()) return emptyList()
        return featureCollection.features.map { feature ->
            Location(
                address = feature.placeName,
                longitude = feature.geometry.coordinates[0],
                latitude = feature.geometry.coordinates[1],
            )
        }
    }
}