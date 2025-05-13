package org.turter.water_app_mobile.presentation.logged_out

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeContent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultContent

@Composable
fun LoggedOutContent(component: LoggedOutComponent) {
    Children(
        stack = component.stack
    ) {
        when (val instance = it.instance) {
            is LoggedOutComponent.Child.AuthByCode -> {
                AuthByCodeContent(instance.component)
            }

            is LoggedOutComponent.Child.AuthDefault -> {
                AuthDefaultContent(instance.component)
            }

            LoggedOutComponent.Child.Initial -> {
                //TODO show loader?
            }
        }
    }
}