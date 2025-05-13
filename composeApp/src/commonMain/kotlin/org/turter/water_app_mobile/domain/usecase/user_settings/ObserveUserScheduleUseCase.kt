package org.turter.water_app_mobile.domain.usecase.user_settings

import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.domain.entity.UserSchedule
import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

class ObserveUserScheduleUseCase(private val repository: UserSettingsRepository) {
    operator fun invoke(): Flow<UserSchedule> = repository.observeUserSchedule()
}