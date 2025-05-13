package org.turter.water_app_mobile.data.remote.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserWaterLogDto(
    val userId: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val intakes: List<Intake>
) {
    @Serializable
    data class Intake(
        val id: String,
        val amountMl: Int,
        val dateTime: LocalDateTime
    )
}
