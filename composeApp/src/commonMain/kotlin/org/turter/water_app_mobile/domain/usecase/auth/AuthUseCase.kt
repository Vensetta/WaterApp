package org.turter.water_app_mobile.domain.usecase.auth

import org.turter.water_app_mobile.domain.repository.AuthRepository

class AuthUseCase(private val authRepository: AuthRepository) {

    suspend fun authenticateByRedirect(): Result<Unit> = authRepository.authenticateByRedirect()

    suspend fun authenticateByCode(code: Int): Result<Unit> =
        authRepository.authenticateByCode(code)

}