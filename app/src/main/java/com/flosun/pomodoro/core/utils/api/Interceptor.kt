package com.flosun.pomodoro.core.utils.api

import io.ktor.client.request.HttpRequestBuilder

interface Interceptor {

    operator fun invoke(req: HttpRequestBuilder)

}