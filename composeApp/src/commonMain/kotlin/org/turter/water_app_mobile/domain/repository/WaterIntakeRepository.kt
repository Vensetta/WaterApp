package org.turter.water_app_mobile.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog

interface WaterIntakeRepository {

    fun observeCurrentDayIntakes(): Flow<Result<List<WaterIntake>>>

    suspend fun getWeeklyIntakesLog(): Result<WaterIntakesWeeklyLog>

    suspend fun addIntake(amountMl: Int, dateTime: LocalDateTime)

    suspend fun cleanUpUserLog(): Result<Unit>

}