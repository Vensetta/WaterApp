package org.turter.water_app_mobile.presentation.logged_out

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeComponent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultComponent

interface LoggedOutComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data object Initial : Child
        data class AuthDefault(val component: AuthDefaultComponent) : Child
        data class AuthByCode(val component: AuthByCodeComponent) : Child
    }
}