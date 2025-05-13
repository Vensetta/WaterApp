package org.turter.water_app_mobile.presentation.logged_in.components.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeContent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewContent

@Composable
fun HomeContent(component: HomeComponent, modifier: Modifier = Modifier) {
//    Scaffold(
//        modifier = modifier
//    ) {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is HomeComponent.Child.Overview -> {
                    OverviewContent(instance.component, modifier)
                }

                is HomeComponent.Child.AddWaterIntake -> {
                    AddWaterIntakeContent(instance.component, modifier)
                }
            }
        }
//    }
}