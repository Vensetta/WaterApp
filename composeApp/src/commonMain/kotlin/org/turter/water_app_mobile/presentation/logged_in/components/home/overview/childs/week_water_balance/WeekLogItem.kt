package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance

import kotlinx.datetime.LocalDate

data class WeekLogItem(
    val start: LocalDate,
    val finish: LocalDate,
    val days: List<DayRecord>
) {
    data class DayRecord(
        val date: LocalDate,
        val amountMl: Int
    )
}
