package org.turter.water_app_mobile.presentation.logged_in.components.settings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleComponent

interface SettingsComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Options(val component: OptionsComponent) : Child
        data class DailyWaterRequirement(val component: DailyWaterRequirementComponent) : Child
        data class UserSchedule(val component: UserScheduleComponent) : Child
        data class CleanUpLog(val component: CleanUpLogComponent) : Child
    }
}