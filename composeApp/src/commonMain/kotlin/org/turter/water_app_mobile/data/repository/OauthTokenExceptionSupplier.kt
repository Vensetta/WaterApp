package org.turter.water_app_mobile.data.repository

import org.turter.water_app_mobile.domain.exception.OauthTokenException

fun getTokenIsInvalidException(token: String): OauthTokenException =
    OauthTokenException("Token invalid: $token")

fun getUserExtractionFromTokenException(token: String): OauthTokenException =
    OauthTokenException("Can`t extract user from token: $token")

fun getEmptyTokenStoreException(): OauthTokenException =
    OauthTokenException("No tokens in store for authentication")

fun getRefreshTokenNotFoundException(): OauthTokenException =
    OauthTokenException("Refresh token not found")

fun getTokenExpireException(exp: Long, cur: Long): OauthTokenException =
    OauthTokenException("Token expired. Exp: $exp. Current: $cur")