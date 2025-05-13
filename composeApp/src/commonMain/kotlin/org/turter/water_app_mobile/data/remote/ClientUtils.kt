package org.turter.water_app_mobile.data.remote

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.turter.water_app_mobile.domain.exception.ClientException

suspend fun <T> proceedRequest(
    action: suspend () -> HttpResponse,
    decoder: suspend (HttpResponse) -> T
): Result<T> =
    try {
        val res = action()
        if (res.status.isSuccess()) {
            val content = decoder(res)
            Result.success(content)
        } else {
            Result.failure(ClientException.NotSuccessResponseException(res))
        }
    } catch (e: Exception) {
        when (e) {
            is HttpRequestTimeoutException -> Result.failure(e)
            else -> throw e
        }
    }