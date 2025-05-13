package org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement

import kotlinx.coroutines.flow.StateFlow

interface DailyWaterRequirementComponent {

    val model: StateFlow<DailyWaterRequirementStore.State>

    fun onClickBack()

    fun setAmountMl(amountMl: Int)

    fun onClickSave()

}