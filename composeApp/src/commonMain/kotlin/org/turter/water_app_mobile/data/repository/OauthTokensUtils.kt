package org.turter.water_app_mobile.data.repository

import kotlinx.datetime.Clock
import org.publicvalue.multiplatform.oidc.tokenstore.OauthTokens
import org.publicvalue.multiplatform.oidc.types.Jwt
import org.turter.water_app_mobile.domain.entity.User

fun OauthTokens.isAccessTokenValid(): Result<String> = accessToken.isTokenValid()

fun OauthTokens.isRefreshTokenValid(): Result<String> =
    refreshToken?.isTokenValid() ?: Result.failure(getRefreshTokenNotFoundException())

fun String.isTokenValid(): Result<String> {
    val exp =
        Jwt.parse(this).payload.exp ?: return Result.failure(getTokenIsInvalidException(this))
    val currentTime = Clock.System.now().epochSeconds
    val res = currentTime < exp
    return if (res) Result.success(this) else Result.failure(
        getTokenExpireException(
            exp = exp,
            cur = currentTime
        )
    )
}

//TODO remove
fun OauthTokens.extractUser(): User =
    accessToken.let {
        val payload = Jwt.parse(it).payload
        val id = payload.sub
        val username = payload.additionalClaims["preferred_username"] as String?
        if (id != null && username != null) {
            User(id = id, username = username)
        } else {
            throw getUserExtractionFromTokenException(it)
        }
    }

fun String.extractUserFromToken(): User {
    val payload = Jwt.parse(this).payload
    val id = payload.sub
    val username = payload.additionalClaims["preferred_username"] as String?
    return if (id != null && username != null) {
        User(id = id, username = username)
    } else {
        throw getUserExtractionFromTokenException(this)
    }
}