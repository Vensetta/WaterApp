package org.turter.water_app_mobile.presentation.logged_in.components.home.overview

import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartComponent

interface OverviewComponent {

    val todayProgressBarComponent: TodayProgressBarComponent

    val weekWaterBalanceChartComponent: WeekWaterBalanceChartComponent

    fun onClickWaterIntakeAdd()

}