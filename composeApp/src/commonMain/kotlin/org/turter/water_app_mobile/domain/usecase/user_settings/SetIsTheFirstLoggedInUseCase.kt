package org.turter.water_app_mobile.domain.usecase.user_settings

import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

class SetIsTheFirstLoggedInUseCase(private val repository: UserSettingsRepository) {
    suspend fun setNoMoreFirstLoggedIn() = repository.setIsFirstLoggedIn(false)
}