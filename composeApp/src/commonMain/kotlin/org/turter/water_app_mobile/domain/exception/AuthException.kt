package org.turter.water_app_mobile.domain.exception

sealed class AuthException(message: String) : RuntimeException(message) {

    data object RefreshTokenException : AuthException("Fail while refreshing tokens")

    data object InvalidAccessCodeException : AuthException(
        "Invalid code for access to offline_access token"
    )

    data object EmptyOfflineAccessTokenException : AuthException(
        "Empty offline_access token"
    )

}