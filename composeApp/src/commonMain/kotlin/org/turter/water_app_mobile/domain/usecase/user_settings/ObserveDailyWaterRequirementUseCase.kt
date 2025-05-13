package org.turter.water_app_mobile.domain.usecase.user_settings

import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

class ObserveDailyWaterRequirementUseCase(private val repository: UserSettingsRepository) {
    operator fun invoke(): Flow<Int> = repository.observeDailyWaterRequirement()
}