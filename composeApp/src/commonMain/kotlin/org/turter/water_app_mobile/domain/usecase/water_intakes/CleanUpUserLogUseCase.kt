package org.turter.water_app_mobile.domain.usecase.water_intakes

import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

class CleanUpUserLogUseCase(private val repository: WaterIntakeRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.cleanUpUserLog()
}