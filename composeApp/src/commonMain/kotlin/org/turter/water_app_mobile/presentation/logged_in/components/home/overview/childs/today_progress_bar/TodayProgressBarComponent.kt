package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar

import kotlinx.coroutines.flow.StateFlow

interface TodayProgressBarComponent {

    val model: StateFlow<TodayProgressBarStore.State>

    fun onViewDetailsClick()
}