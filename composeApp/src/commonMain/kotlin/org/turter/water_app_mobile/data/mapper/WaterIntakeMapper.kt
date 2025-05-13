package org.turter.water_app_mobile.data.mapper

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import org.turter.water_app_mobile.data.remote.dto.UserWaterLogDto
import org.turter.water_app_mobile.data.remote.dto.WaterIntakeDto
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.entity.WaterIntakesDailyLog
import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog

fun WaterIntakeDto.toEntity(): WaterIntake =
    WaterIntake(id = id, amountMl = amountMl, dateTime = dateTime)

fun UserWaterLogDto.Intake.toWaterIntake(): WaterIntake =
    WaterIntake(id = id, amountMl = amountMl, dateTime = dateTime)

fun List<UserWaterLogDto.Intake>.toWaterIntakeList(): List<WaterIntake> =
    this.map { it.toWaterIntake() }.toList()

fun UserWaterLogDto.toWaterIntakesWeeklyLog(wakeUpTime: LocalTime): WaterIntakesWeeklyLog {
    val dailyLogs = this.intakes.toWaterIntakeList()
        .groupBy { intake ->
            val date = intake.dateTime.date

            if (intake.dateTime.time > wakeUpTime) date
            else date.minus(1, DateTimeUnit.DAY)
        }
        .map { (date, intakes) ->
            WaterIntakesDailyLog(date, intakes)
        }
        .sortedBy { it.date }
    return WaterIntakesWeeklyLog(this.from.date, this.to.date, dailyLogs)
}