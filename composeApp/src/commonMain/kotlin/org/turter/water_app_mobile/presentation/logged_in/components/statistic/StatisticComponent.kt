package org.turter.water_app_mobile.presentation.logged_in.components.statistic

import kotlinx.coroutines.flow.StateFlow

interface StatisticComponent {

    val model: StateFlow<StatisticStore.State>

}