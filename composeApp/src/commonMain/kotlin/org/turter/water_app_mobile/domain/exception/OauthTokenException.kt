package org.turter.water_app_mobile.domain.exception

class OauthTokenException(message: String) : RuntimeException("Token exception. $message") {
}