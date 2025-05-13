package org.turter.water_app_mobile.domain.usecase.water_intakes

import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog
import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

class GetWeeklyIntakesLogUseCase(private val repository: WaterIntakeRepository) {
    suspend operator fun invoke(): Result<WaterIntakesWeeklyLog> = repository.getWeeklyIntakesLog()
}