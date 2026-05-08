package com.flosun.pomodoro.adapters.apis.interceptors

import com.flosun.pomodoro.BuildConfig
import com.flosun.pomodoro.core.utils.api.Interceptor
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapTilerInterceptor @Inject constructor() : Interceptor {

    override fun invoke(req: HttpRequestBuilder) {
        val apiKey = BuildConfig.MAP_TILER_API_KEY
        req.parameter("key", apiKey)
    }

}