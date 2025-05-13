package org.turter.water_app_mobile.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.domain.entity.AuthState
import org.turter.water_app_mobile.domain.repository.AuthRepository

class ObserveAuthUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(): Flow<AuthState> = authRepository.observeAuthState()

}