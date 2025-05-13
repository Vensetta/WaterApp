package org.turter.water_app_mobile.domain.usecase.water_intakes

import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

class ObserveCurrentDayIntakesUseCase(private val repository: WaterIntakeRepository) {
    operator fun invoke(): Flow<Result<List<WaterIntake>>> = repository.observeCurrentDayIntakes()
}