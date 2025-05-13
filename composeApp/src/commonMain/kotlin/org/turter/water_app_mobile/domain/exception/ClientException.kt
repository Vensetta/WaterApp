package org.turter.water_app_mobile.domain.exception

import io.ktor.client.statement.HttpResponse

sealed class ClientException(message: String? = null) : RuntimeException(message) {

    data class NotSuccessResponseException(val response: HttpResponse) :
        ClientException("Api response not 2xx: ${response.status}")

}