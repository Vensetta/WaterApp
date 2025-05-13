package org.turter.water_app_mobile.domain.entity

import kotlinx.datetime.LocalDateTime

data class WaterIntake(
    val id: String,
    val amountMl: Int,
    val dateTime: LocalDateTime
)
