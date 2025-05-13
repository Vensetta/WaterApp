package org.turter.water_app_mobile.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.turter.water_app_mobile.presentation.logged_in.DefaultLoggedInComponent
import org.turter.water_app_mobile.presentation.logged_in.LoggedInStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.home.DefaultHomeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.HomeStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.DefaultAddWaterIntakeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.DefaultOverviewComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.DefaultTodayProgressBarComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.DefaultWeekWaterBalanceChartComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.DefaultConfirmDeleteWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.DefaultCreateWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.DefaultSettingsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.SettingsStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.DefaultCleanUpLogComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DefaultDailyWaterRequirementComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.DefaultOptionsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.DefaultUserScheduleComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleStoreFactory
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.DefaultStatisticComponent
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticStoreFactory
import org.turter.water_app_mobile.presentation.logged_out.DefaultLoggedOutComponent
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutStoreFactory
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeStoreFactory
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.DefaultAuthByCodeComponent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultStoreFactory
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.DefaultAuthDefaultComponent
import org.turter.water_app_mobile.presentation.root.DefaultRootComponent
import org.turter.water_app_mobile.presentation.root.RootComponent
import org.turter.water_app_mobile.presentation.root.RootStoreFactory

val storeFactoryModule = module {
    singleOf(::DefaultStoreFactory).bind<StoreFactory>()

    singleOf(::RootStoreFactory)

    singleOf(::LoggedOutStoreFactory)
    singleOf(::AuthDefaultStoreFactory)
    singleOf(::AuthByCodeStoreFactory)

    singleOf(::LoggedInStoreFactory)

    singleOf(::HomeStoreFactory)

    singleOf(::OverviewStoreFactory)
    singleOf(::TodayProgressBarStoreFactory)
    singleOf(::WeekWaterBalanceChartStoreFactory)

    singleOf(::AddWaterIntakeStoreFactory)
    singleOf(::CreateWaterVolumeStoreFactory)
    singleOf(::ConfirmDeleteWaterVolumeModalStoreFactory)

    singleOf(::StatisticStoreFactory)

    singleOf(::SettingsStoreFactory)
    singleOf(::OptionsStoreFactory)
    singleOf(::DailyWaterRequirementStoreFactory)
    singleOf(::UserScheduleStoreFactory)
    singleOf(::CleanUpLogStoreFactory)
}

val componentFactoryModule = module {
    singleOf(DefaultRootComponent::Factory)

    singleOf(DefaultLoggedOutComponent::Factory)
    singleOf(DefaultAuthDefaultComponent::Factory)
    singleOf(DefaultAuthByCodeComponent::Factory)

    singleOf(DefaultLoggedInComponent::Factory)

    singleOf(DefaultHomeComponent::Factory)

    singleOf(DefaultOverviewComponent::Factory)
    singleOf(DefaultTodayProgressBarComponent::Factory)
    singleOf(DefaultWeekWaterBalanceChartComponent::Factory)

    singleOf(DefaultAddWaterIntakeComponent::Factory)
    singleOf(DefaultCreateWaterVolumeModalComponent::Factory)
    singleOf(DefaultConfirmDeleteWaterVolumeModalComponent::Factory)

    singleOf(DefaultStatisticComponent::Factory)

    singleOf(DefaultSettingsComponent::Factory)
    singleOf(DefaultOptionsComponent::Factory)
    singleOf(DefaultDailyWaterRequirementComponent::Factory)
    singleOf(DefaultUserScheduleComponent::Factory)
    singleOf(DefaultCleanUpLogComponent::Factory)
}

val presentationModule = module {
    includes(storeFactoryModule, componentFactoryModule)
}

