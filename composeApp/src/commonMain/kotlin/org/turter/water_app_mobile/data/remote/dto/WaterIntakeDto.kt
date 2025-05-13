package org.turter.water_app_mobile.data.remote.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class WaterIntakeDto(
    val id: String,
    val amountMl: Int,
    val dateTime: LocalDateTime,
    val userId: String
)
