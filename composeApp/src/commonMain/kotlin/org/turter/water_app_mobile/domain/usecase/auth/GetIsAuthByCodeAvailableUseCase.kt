package org.turter.water_app_mobile.domain.usecase.auth

import org.turter.water_app_mobile.domain.repository.AuthRepository

class GetIsAuthByCodeAvailableUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(): Boolean = authRepository.isAuthByCodeAvailable()

}