package org.turter.water_app_mobile.data.remote

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.turter.water_app_mobile.data.remote.dto.AddWaterIntakePayload
import org.turter.water_app_mobile.data.remote.dto.UserWaterLogDto
import org.turter.water_app_mobile.data.remote.dto.WaterIntakeDto

class WaterIntakeApiServiceImpl(
    private val client: HttpClient
) : WaterIntakeApiService {
    private val log = Logger.withTag("WaterIntakeApiServiceImpl")

    override suspend fun loadUserLog(
        from: LocalDateTime,
        to: LocalDateTime
    ): Result<UserWaterLogDto> {
        return proceedRequest(
            action = {
                client.get(ApiEndpoint.userLogByPeriod) {
                    url {
                        parameters.append("from", from.toString())
                        parameters.append("to", to.toString())
                    }
                }
            },
            decoder = {
                Json.decodeFromString(it.body())
            }
        )
    }

    override suspend fun addWaterIntake(payload: AddWaterIntakePayload): Result<WaterIntakeDto> {
        return proceedRequest(
            action = {
                client.post(ApiEndpoint.addUserWaterIntake) {
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }
            },
            decoder = {
                Json.decodeFromString(it.body())
            }
        )
    }

    override suspend fun cleanUpUserLog(): Result<Unit> {
        return proceedRequest(
            action = {
                client.delete(ApiEndpoint.cleanUpUserLog)
            },
            decoder = {  }
        )
    }
}