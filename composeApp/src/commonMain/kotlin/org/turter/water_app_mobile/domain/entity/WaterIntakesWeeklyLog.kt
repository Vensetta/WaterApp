package org.turter.water_app_mobile.domain.entity

import kotlinx.datetime.LocalDate

data class WaterIntakesWeeklyLog(
    val from: LocalDate,
    val to: LocalDate,
    val dailyLogs: List<WaterIntakesDailyLog>
)
