package org.turter.water_app_mobile.domain.usecase.water_intakes

import kotlinx.datetime.LocalDateTime
import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

class AddIntakeUseCase(private val repository: WaterIntakeRepository) {
    suspend operator fun invoke(amountMl: Int, dateTime: LocalDateTime) =
        repository.addIntake(amountMl, dateTime)
}