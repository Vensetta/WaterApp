package org.turter.water_app_mobile.domain.entity

import kotlinx.datetime.LocalDate

data class WaterIntakesDailyLog(
    val date: LocalDate,
    val intakes: List<WaterIntake>
)
