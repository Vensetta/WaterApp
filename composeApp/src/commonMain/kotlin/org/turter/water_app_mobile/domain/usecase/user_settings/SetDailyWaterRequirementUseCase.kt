package org.turter.water_app_mobile.domain.usecase.user_settings

import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

class SetDailyWaterRequirementUseCase(private val repository: UserSettingsRepository) {
    suspend operator fun invoke(amountMl: Int) = repository.setDailyWaterRequirement(amountMl)
}