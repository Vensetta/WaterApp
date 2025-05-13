package org.turter.water_app_mobile.data.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.turter.water_app_mobile.data.local.store.UserSettingsStore
import org.turter.water_app_mobile.data.mapper.toWaterIntakeList
import org.turter.water_app_mobile.data.mapper.toWaterIntakesWeeklyLog
import org.turter.water_app_mobile.data.remote.WaterIntakeApiService
import org.turter.water_app_mobile.data.remote.dto.AddWaterIntakePayload
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog
import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

class WaterIntakeRepositoryImpl(
    private val waterIntakeApiService: WaterIntakeApiService,
    private val userSettingsStore: UserSettingsStore
) : WaterIntakeRepository {
    private val log = Logger.withTag("WaterIntakeRepositoryImpl")

    private val checkIntakesEventFlow = MutableSharedFlow<Unit>(replay = 1)

    private val _currentDayIntakesFlow = flow<Result<List<WaterIntake>>> {
        checkIntakesEventFlow.emit(Unit)
        checkIntakesEventFlow.collect {
            val period = calcCurrentUserActivityPeriod()

            log.d { "Start fetching daily water intakes for period: $period" }

            val result = waterIntakeApiService.loadUserLog(from = period.from, to = period.to)
                .map { dto -> dto.intakes.toWaterIntakeList() }
                .onSuccess { list ->
                    log.d { "Success fetched daily water intakes. Size: ${list.size}" }
                }
                .onFailure { cause ->
                    log.e(cause) { "Failure fetching daily water intakes" }
                }

            emit(result)
        }
    }

    override fun observeCurrentDayIntakes(): Flow<Result<List<WaterIntake>>> {
        return _currentDayIntakesFlow
    }

    override suspend fun getWeeklyIntakesLog(): Result<WaterIntakesWeeklyLog> {
        val (from, to) = calcCurrentUserActivityPeriod()

        return waterIntakeApiService.loadUserLog(
            from = from.date.minus(7, DateTimeUnit.DAY).atTime(from.time),
            to = to
        )
            .map { it.toWaterIntakesWeeklyLog(from.time) }
    }

    override suspend fun addIntake(amountMl: Int, dateTime: LocalDateTime) {
        waterIntakeApiService.addWaterIntake(AddWaterIntakePayload(amountMl, dateTime))
            .onSuccess {
                log.d { "Success added intake: $it" }
                checkIntakesEventFlow.emit(Unit)
            }
            .onFailure {
                log.e(it) { "Failure adding intake for amount: $amountMl ml and dateTime: $dateTime" }
            }
    }

    override suspend fun cleanUpUserLog(): Result<Unit> {
        return waterIntakeApiService.cleanUpUserLog()
            .onSuccess { checkIntakesEventFlow.emit(Unit) }
    }

    private fun calcCurrentUserActivityPeriod(): UserActivityPeriod {
        val wakeUpTime = userSettingsStore.getWakeUpTime()
        val bedTime = userSettingsStore.getBedTime()

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentDate = currentDateTime.date

        var from: LocalDateTime
        var to: LocalDateTime

        if (bedTime > wakeUpTime) {
            from = currentDate.atTime(wakeUpTime)
            to = currentDate.atTime(bedTime)
        } else {
            if (currentDateTime.time < bedTime) {
                from = currentDate.minus(1, DateTimeUnit.DAY).atTime(wakeUpTime)
                to = currentDate.atTime(bedTime)
            } else {
                from = currentDate.atTime(wakeUpTime)
                to = currentDate.plus(1, DateTimeUnit.DAY).atTime(bedTime)
            }
        }

        return UserActivityPeriod(from = from, to = to)
    }
}

private data class UserActivityPeriod(val from: LocalDateTime, val to: LocalDateTime)