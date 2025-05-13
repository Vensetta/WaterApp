package org.turter.water_app_mobile.data.local.store

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.domain.entity.UserSchedule

interface UserSettingsStore {

    fun observeIsFirstLoggedIn(): Flow<Boolean>

    suspend fun setIsFirstLoggedIn(value: Boolean)

    fun observeDailyWaterRequirement(): Flow<Int>

    suspend fun setDailyWaterRequirement(amountMl: Int)

    fun observeUserSchedule(): Flow<UserSchedule>

    fun getWakeUpTime(): LocalTime

    fun getBedTime(): LocalTime

    suspend fun setWakeUpTime(time: LocalTime)

    suspend fun setBedTime(time: LocalTime)
}