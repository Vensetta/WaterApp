package org.turter.water_app_mobile.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.turter.water_app_mobile.domain.entity.AuthState

interface AuthRepository {

    fun observeAuthState(): StateFlow<AuthState>

    fun isAuthByCodeAvailable(): Boolean

    suspend fun authenticateByRedirect(): Result<Unit>

    suspend fun authenticateByCode(code: Int): Result<Unit>

    suspend fun setupAuthByCode(code: Int): Result<Unit>

}