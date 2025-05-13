package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance

import kotlinx.coroutines.flow.StateFlow

interface WeekWaterBalanceChartComponent {

    val model: StateFlow<WeekWaterBalanceChartStore.State>

}