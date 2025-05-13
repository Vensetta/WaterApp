package org.turter.water_app_mobile.presentation.mapper

import org.turter.water_app_mobile.domain.entity.WaterIntakesDailyLog
import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekLogItem

fun WaterIntakesWeeklyLog.toWeekLogItem(): WeekLogItem =
    WeekLogItem(
        start = from,
        finish = to,
        days = dailyLogs.toWeekLogItemDayList()
    )

fun List<WaterIntakesDailyLog>.toWeekLogItemDayList(): List<WeekLogItem.DayRecord> =
    this.map { dailyLog ->
        WeekLogItem.DayRecord(
            date = dailyLog.date,
            amountMl = dailyLog.intakes.sumOf { it.amountMl }
        )
    }