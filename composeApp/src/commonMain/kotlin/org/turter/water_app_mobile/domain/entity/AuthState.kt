package org.turter.water_app_mobile.domain.entity

sealed class AuthState {

    data object Initial : AuthState()

    data object Loading : AuthState()

    data class Authenticated(val user: User) : AuthState()

    data object NotAuthenticated : AuthState()

}