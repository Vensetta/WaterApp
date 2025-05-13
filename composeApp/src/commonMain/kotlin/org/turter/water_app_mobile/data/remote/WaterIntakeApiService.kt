package org.turter.water_app_mobile.data.remote

import kotlinx.datetime.LocalDateTime
import org.turter.water_app_mobile.data.remote.dto.AddWaterIntakePayload
import org.turter.water_app_mobile.data.remote.dto.UserWaterLogDto
import org.turter.water_app_mobile.data.remote.dto.WaterIntakeDto

interface WaterIntakeApiService {

    suspend fun loadUserLog(from: LocalDateTime, to: LocalDateTime): Result<UserWaterLogDto>

    suspend fun addWaterIntake(payload: AddWaterIntakePayload): Result<WaterIntakeDto>

    suspend fun cleanUpUserLog(): Result<Unit>

}