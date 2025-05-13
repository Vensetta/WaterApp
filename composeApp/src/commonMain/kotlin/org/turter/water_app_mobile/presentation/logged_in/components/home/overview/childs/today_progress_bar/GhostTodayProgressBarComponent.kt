package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GhostTodayProgressBarComponent : TodayProgressBarComponent {
    override val model: StateFlow<TodayProgressBarStore.State> =
        MutableStateFlow(
            TodayProgressBarStore.State(
                TodayProgressBarStore.State.ProgressBarState.Loaded(
                    currentConsumedMl = 200,
                    targetMl = 400
                )
            )
        )

    override fun onViewDetailsClick() {

    }
}