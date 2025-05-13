package org.turter.water_app_mobile.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.turter.water_app_mobile.domain.usecase.auth.AuthUseCase
import org.turter.water_app_mobile.domain.usecase.auth.GetIsAuthByCodeAvailableUseCase
import org.turter.water_app_mobile.domain.usecase.auth.ObserveAuthUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveDailyWaterRequirementUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveIsTheFirstLoggedInUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveUserScheduleUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.SetDailyWaterRequirementUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.SetIsTheFirstLoggedInUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.SetUserScheduleUseCase
import org.turter.water_app_mobile.domain.usecase.water_intakes.AddIntakeUseCase
import org.turter.water_app_mobile.domain.usecase.water_intakes.CleanUpUserLogUseCase
import org.turter.water_app_mobile.domain.usecase.water_intakes.GetWeeklyIntakesLogUseCase
import org.turter.water_app_mobile.domain.usecase.water_intakes.ObserveCurrentDayIntakesUseCase
import org.turter.water_app_mobile.domain.usecase.water_volumes.AddConsumeWaterVolumesUseCase
import org.turter.water_app_mobile.domain.usecase.water_volumes.DeleteConsumeWaterVolumesUseCase
import org.turter.water_app_mobile.domain.usecase.water_volumes.GetConsumeWaterVolumesUseCase

val useCaseModule = module {
    singleOf(::AuthUseCase)
    singleOf(::GetIsAuthByCodeAvailableUseCase)
    singleOf(::ObserveAuthUseCase)

    singleOf(::ObserveDailyWaterRequirementUseCase)
    singleOf(::ObserveIsTheFirstLoggedInUseCase)
    singleOf(::ObserveUserScheduleUseCase)
    singleOf(::SetDailyWaterRequirementUseCase)
    singleOf(::SetIsTheFirstLoggedInUseCase)
    singleOf(::SetUserScheduleUseCase)

    singleOf(::AddIntakeUseCase)
    singleOf(::CleanUpUserLogUseCase)
    singleOf(::GetWeeklyIntakesLogUseCase)
    singleOf(::ObserveCurrentDayIntakesUseCase)

    singleOf(::AddConsumeWaterVolumesUseCase)
    singleOf(::DeleteConsumeWaterVolumesUseCase)
    singleOf(::GetConsumeWaterVolumesUseCase)
}