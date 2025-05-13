package org.turter.water_app_mobile.presentation.logged_out.components.auth_default

import kotlinx.coroutines.flow.StateFlow

interface AuthDefaultComponent {

    val model: StateFlow<AuthDefaultStore.State>

    fun onClickLogIn()

    fun onClickToAuthByCode()

}