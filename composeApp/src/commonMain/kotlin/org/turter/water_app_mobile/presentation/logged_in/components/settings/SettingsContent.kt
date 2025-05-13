package org.turter.water_app_mobile.presentation.logged_in.components.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogContent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementContent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsContent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleContent

@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Children(stack = component.stack) {
            when(val instance = it.instance) {
                is SettingsComponent.Child.Options -> {
                    OptionsContent(instance.component)
                }
                is SettingsComponent.Child.DailyWaterRequirement -> {
                    DailyWaterRequirementContent(instance.component)
                }
                is SettingsComponent.Child.UserSchedule -> {
                    UserScheduleContent(instance.component)
                }
                is SettingsComponent.Child.CleanUpLog -> {
                    CleanUpLogContent(instance.component)
                }
            }
        }
    }
}