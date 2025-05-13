package org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log

import kotlinx.coroutines.flow.StateFlow

interface CleanUpLogComponent {

    val model: StateFlow<CleanUpLogStore.State>

    fun onClickBack()

    fun onClickConfirm()
}