package com.flosun.pomodoro.core.utils.api

import com.flosun.pomodoro.core.constants.DEBUG_TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import timber.log.Timber

abstract class BaseApi(
    protected val httpClient: HttpClient,
    protected vararg val interceptors: Interceptor,
) {
    protected suspend inline fun <reified T> run(
        method: HttpMethod,
        url: String,
        block: HttpRequestBuilder.() -> Unit = {},
    ): T {
        return try {
            val response = httpClient.request(url) {
                this.method = method
                block()
            }

            when (response.status) {
                HttpStatusCode.OK,
                HttpStatusCode.Created,
                HttpStatusCode.Accepted -> {
                    val httpResponse = response.body<T>()
                    httpResponse
                }

                else -> {
                    throw FailureApiException(
                        statusCode = response.status,
                        message = "API call failed with status code: ${response.status}",
                    )
                }
            }
        } catch (exception: Exception) {
            if (exception is FailureApiException) throw exception
            else throw FailureApiException(
                statusCode = HttpStatusCode.BadRequest,
                message = exception.message ?: "Unknown error occurred",
            )
        }
    }

    protected fun HttpRequestBuilder.applyInterceptors(vararg clazz: String) {
        interceptors.forEach { interceptor ->
            val interceptorClass = interceptor::class.java.simpleName
            if (clazz.isEmpty() || clazz.contains(interceptorClass)) {
                interceptor(this)
            }
        }
    }

    protected fun HttpRequestBuilder.withParams(params: Map<String, Any?>) {
        params.forEach { param ->
            val value = param.value
            if (value != null) {
                when (value) {
                    is Iterable<*> -> {
                        value.forEach { item ->
                            if (item != null) parameter(param.key, item)
                        }
                    }

                    else -> parameter(param.key, value)
                }
            }
        }
    }

    protected inline fun <reified T> HttpRequestBuilder.withBody(body: T) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

class FailureApiException(
    val statusCode: HttpStatusCode,
    message: String
) : Exception(message)