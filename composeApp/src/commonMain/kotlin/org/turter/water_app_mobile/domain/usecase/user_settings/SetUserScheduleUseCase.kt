package org.turter.water_app_mobile.domain.usecase.user_settings

import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

class SetUserScheduleUseCase(private val repository: UserSettingsRepository) {
    suspend fun setWakeUpTime(time: LocalTime) = repository.setWakeUpTime(time)

    suspend fun setBedTime(time: LocalTime) = repository.setBedTime(time)
}