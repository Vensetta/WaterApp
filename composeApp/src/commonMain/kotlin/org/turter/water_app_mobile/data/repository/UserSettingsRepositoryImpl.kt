package org.turter.water_app_mobile.data.repository

import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.data.local.store.UserSettingsStore
import org.turter.water_app_mobile.domain.entity.UserSchedule
import org.turter.water_app_mobile.domain.repository.UserSettingsRepository

@OptIn(ExperimentalSettingsApi::class)
class UserSettingsRepositoryImpl(
    private val userSettingsStore: UserSettingsStore
) : UserSettingsRepository {

    override fun observeIsFirstLoggedIn(): Flow<Boolean> {
        return userSettingsStore.observeIsFirstLoggedIn()
    }

    override suspend fun setIsFirstLoggedIn(value: Boolean) {
        userSettingsStore.setIsFirstLoggedIn(value)
    }

    override fun observeDailyWaterRequirement(): Flow<Int> {
        return userSettingsStore.observeDailyWaterRequirement()
    }

    override suspend fun setDailyWaterRequirement(amountMl: Int) {
        userSettingsStore.setDailyWaterRequirement(amountMl)
    }

    override fun observeUserSchedule(): Flow<UserSchedule> {
        return userSettingsStore.observeUserSchedule()
    }

    override suspend fun setWakeUpTime(time: LocalTime) {
        userSettingsStore.setWakeUpTime(time)
    }

    override suspend fun setBedTime(time: LocalTime) {
        userSettingsStore.setBedTime(time)
    }
}