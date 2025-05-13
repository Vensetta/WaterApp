package org.turter.water_app_mobile.data.remote.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AddWaterIntakePayload(
    val amountMl: Int,
    val dateTime: LocalDateTime
)
