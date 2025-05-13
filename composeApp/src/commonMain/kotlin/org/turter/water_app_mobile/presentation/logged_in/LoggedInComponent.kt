package org.turter.water_app_mobile.presentation.logged_in

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import org.turter.water_app_mobile.presentation.logged_in.components.home.HomeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.SettingsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticComponent

interface LoggedInComponent {

    val stack: Value<ChildStack<*, Child>>

    val model: StateFlow<LoggedInStore.State>

    sealed interface Child {
        data class Home(val component: HomeComponent) : Child
        data class Statistic(val component: StatisticComponent) : Child
        data class Settings(val component: SettingsComponent) : Child
    }

    fun openHome()

    fun openStatistic()

    fun openSettings()

}