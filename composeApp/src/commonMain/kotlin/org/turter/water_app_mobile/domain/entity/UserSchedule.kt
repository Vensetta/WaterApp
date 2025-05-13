package org.turter.water_app_mobile.domain.entity

import kotlinx.datetime.LocalTime

data class UserSchedule(
    val wakeUpTime: LocalTime,
    val bedTime: LocalTime
)
