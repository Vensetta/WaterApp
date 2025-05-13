package org.turter.water_app_mobile.data.repository

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onErrorReturn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore
import org.publicvalue.multiplatform.oidc.tokenstore.removeTokens
import org.publicvalue.multiplatform.oidc.tokenstore.saveTokens
import org.publicvalue.multiplatform.oidc.tokenstore.tokensFlow
import org.turter.water_app_mobile.data.local.store.OfflineRefreshTokenStore
import org.turter.water_app_mobile.domain.entity.AuthState
import org.turter.water_app_mobile.domain.exception.AuthException
import org.turter.water_app_mobile.domain.repository.AuthRepository
import waterappmobile.composeapp.generated.resources.Res

@OptIn(ExperimentalOpenIdConnect::class)
class AuthRepositoryImpl(
    private val authFlowFactory: CodeAuthFlowFactory,
    private val oidcClient: OpenIdConnectClient,
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
    private val offlineRefreshTokenStore: OfflineRefreshTokenStore
) : AuthRepository {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val log = Logger.withTag("AuthRepositoryImpl")

    private val authFlow = authFlowFactory.createAuthFlow(oidcClient)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)

    init {
        scope.launch {
            _authState.value = AuthState.Loading
            combine(
                tokenStore.accessTokenFlow,
                tokenStore.refreshTokenFlow,
                tokenStore.idTokenFlow
            ) { accessToken, refreshToken, idToken ->
                if (accessToken != null && refreshToken != null && idToken != null) {
                    accessToken.isTokenValid()
                        .fold(
                            onSuccess = { token ->
                                log.d { "Access token is valid - start user info extraction" }
                                val user = token.extractUserFromToken()
                                log.d { "Extracted user: ${user.username}" }
                                AuthState.Authenticated(user)
                            },
                            onFailure = { cause ->
                                log.d { "Access token is invalid. Cause: $cause" }

                                refreshToken.isTokenValid()
                                    .fold(
                                        onSuccess = { token ->
                                            log.d { "Refresh token is valid - refreshing tokens" }
                                            async { refreshTokens(token) }
                                            _authState.value

                                        },
                                        onFailure = { cause ->
                                            log.d { "Refresh token is invalid. Cause: $cause" }
                                            AuthState.NotAuthenticated
                                        }
                                    )

                            }
                        )
                } else {
                    log.d { "Some/all tokens from tokenStore is null" }
                    AuthState.NotAuthenticated
                }
            }.catch { exception ->
                log.e(exception) { "Catch exception while combine tokens flow." }
                emit(AuthState.NotAuthenticated)
            }.collect { authState ->
                log.d { "New auth state: $authState" }
                _authState.value = authState
            }
        }
    }

    override fun observeAuthState(): StateFlow<AuthState> = _authState.asStateFlow()

    override fun isAuthByCodeAvailable(): Boolean {
        return offlineRefreshTokenStore.isAuthByCodeAvailable()
    }

    override suspend fun authenticateByRedirect(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            log.d { "Start authentication" }
            _authState.value = AuthState.Loading
            try {
                val res = authFlow.getAccessToken()
                tokenStore.saveTokens(res)
                log.d { "Authentication complete, new tokens is saved. Tokens: $res" }
                Result.success(Unit)
            } catch (e: Exception) {
                log.e { "Catch exception while authenticate. Exception: $e" }
                e.printStackTrace()
                _authState.value = AuthState.NotAuthenticated
                Result.failure(e)
            }
        }
    }

    override suspend fun authenticateByCode(code: Int): Result<Unit> {
        try {
            log.d { "Start authentication by code" }
            _authState.value = AuthState.Loading

            if (!offlineRefreshTokenStore.checkAccessCode(code)) {
                log.d { "Code not verified" }
                return Result.failure(AuthException.InvalidAccessCodeException)
            }

            log.d { "Code verified - calling auth server for new tokens" }
            val offlineRefreshToken = offlineRefreshTokenStore.getOfflineRefreshToken()

            if (offlineRefreshToken.isBlank()) {
                return Result.failure(AuthException.EmptyOfflineAccessTokenException)
            }

            val response = oidcClient.refreshToken(offlineRefreshToken)
            tokenStore.saveTokens(response)
            log.d { "Authentication by code complete, new tokens is saved. Tokens: $response" }
            return Result.success(Unit)
        } catch (e: Exception) {
            log.e(e) { "Catch exception while authentication by code" }
            _authState.value = AuthState.NotAuthenticated
            return Result.failure(e)
        }
    }

    override suspend fun setupAuthByCode(code: Int): Result<Unit> {
        try {
            log.d { "Start setup authentication by code" }
            val currentRefreshToken = tokenStore.getRefreshToken()
            if (currentRefreshToken != null) {
                log.d { "Calling auth server for token with offline_access" }
                val response = oidcClient.refreshToken(currentRefreshToken) {
                    url {
                        parameters.append("scope", "offline_access")
                    }
                }
                response.refresh_token
                    ?.let { token ->
                        log.d { "Received offline_access token: $token" }
                        offlineRefreshTokenStore.setOfflineRefreshToken(token)
                        offlineRefreshTokenStore.setAccessCode(code)
                        return Result.success(Unit)
                    }
                log.d { "Fail to get offline_access token from auth server. Response: $response" }
                return Result.failure(AuthException.RefreshTokenException)
            }
            log.d { "Stop setup authentication by code: current refresh token not found" }
            return Result.failure(getRefreshTokenNotFoundException())
        } catch (e: Exception) {
            log.e(e) { "Catch exception while setup authentication by code" }
            return Result.failure(e)
        }
    }

    private suspend fun refreshTokens(refreshToken: String) {
        log.d { "Start refreshing tokens" }
        try {
            val res = oidcClient.refreshToken(refreshToken)
            log.d { "Result of tokens refreshing: $res" }
            tokenStore.saveTokens(
                accessToken = res.access_token,
                refreshToken = res.refresh_token,
                idToken = res.id_token
            )
        } catch (e: Exception) {
            log.e { "Catching exception while refreshing tokens - $e" }
            log.d { "Start removing tokens" }
            tokenStore.removeTokens()
        }
    }
}