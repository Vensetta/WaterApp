package org.turter.water_app_mobile.presentation.logged_in.components.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewComponent

interface HomeComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Overview(val component: OverviewComponent) : Child
        data class AddWaterIntake(val component: AddWaterIntakeComponent) : Child
    }
}