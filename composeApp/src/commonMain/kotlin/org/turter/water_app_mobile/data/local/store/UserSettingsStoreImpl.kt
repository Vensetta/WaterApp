package org.turter.water_app_mobile.data.local.store

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.domain.entity.UserSchedule

@OptIn(ExperimentalSettingsApi::class)
class UserSettingsStoreImpl(
    private val settings: Settings
) : UserSettingsStore {
    companion object {
        private const val IS_FIRST_LOGGED_IN_KEY = "isFirstLogin"
        private const val DAILY_WATER_REQUIREMENT_KEY = "dailyWaterRequirement"
        private const val WAKE_UP_TIME_KEY = "wakeUpTime"
        private const val BED_TIME_KEY = "bedTime"
    }

    private val log = Logger.withTag("UserSettingsStoreImpl")

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    override fun observeIsFirstLoggedIn(): Flow<Boolean> {
        return flowSettings.getBooleanFlow(IS_FIRST_LOGGED_IN_KEY, false)
            .onEach { log.d { "Fetched is first logged in: $it" } }
    }

    override suspend fun setIsFirstLoggedIn(value: Boolean) {
        log.d { "Putting is first logged in: $value" }
        flowSettings.putBoolean(IS_FIRST_LOGGED_IN_KEY, value)
    }

    override fun observeDailyWaterRequirement(): Flow<Int> {
        return flowSettings.getIntFlow(DAILY_WATER_REQUIREMENT_KEY, -1)
            .onEach { log.d { "Fetched daily water requirement: $it ml" } }
    }

    override suspend fun setDailyWaterRequirement(amountMl: Int) {
        log.d { "Putting daily water requirement: $amountMl ml" }
        flowSettings.putInt(DAILY_WATER_REQUIREMENT_KEY, amountMl)
    }

    override fun observeUserSchedule(): Flow<UserSchedule> {
        return combine(
            flowSettings.getIntFlow(WAKE_UP_TIME_KEY, 0),
            flowSettings.getIntFlow(BED_TIME_KEY, 0)
        ) { wakeUpTimeSecondsOfDay, bedTimeSecondsOfDay ->
            UserSchedule(
                wakeUpTime = LocalTime.fromSecondOfDay(wakeUpTimeSecondsOfDay),
                bedTime = LocalTime.fromSecondOfDay(bedTimeSecondsOfDay)
            )
        }.onEach {
            log.d { "Fetched user schedule: $it" }
        }
    }

    override fun getWakeUpTime(): LocalTime {
        log.d { "Fetching wake up time" }
        return LocalTime.fromSecondOfDay(settings.getInt(WAKE_UP_TIME_KEY, 0))
    }

    override fun getBedTime(): LocalTime {
        log.d { "Fetching bed time" }
        return LocalTime.fromSecondOfDay(settings.getInt(BED_TIME_KEY, 0))
    }

    override suspend fun setWakeUpTime(time: LocalTime) {
        log.d { "Putting wake up time: $time" }
        flowSettings.putInt(WAKE_UP_TIME_KEY, time.toSecondOfDay())
    }

    override suspend fun setBedTime(time: LocalTime) {
        log.d { "Putting bed time: $time" }
        flowSettings.putInt(BED_TIME_KEY, time.toSecondOfDay())
    }
}