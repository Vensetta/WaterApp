package org.turter.water_app_mobile.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.turter.water_app_mobile.presentation.logged_in.LoggedInComponent
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object Initial : Child
        data object Loading : Child
        data class LoggedIn(val component: LoggedInComponent) : Child
        data class LoggedOut(val component: LoggedOutComponent) : Child
    }
}