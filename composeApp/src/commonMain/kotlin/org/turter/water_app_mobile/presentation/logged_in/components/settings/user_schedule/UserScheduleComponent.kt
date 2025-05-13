package org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule

import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalTime

interface UserScheduleComponent {

    val model: StateFlow<UserScheduleStore.State>

    fun onClickBack()

    fun onClickSave()

    fun setWakeUpTime(time: LocalTime)

    fun setBedTime(time: LocalTime)

}