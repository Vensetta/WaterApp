package org.turter.water_app_mobile.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.turter.water_app_mobile.data.auth.OidcClientInitializer
import org.turter.water_app_mobile.data.local.config.AppDatabase
import org.turter.water_app_mobile.data.local.dao.ConsumeWaterVolumeDao
import org.turter.water_app_mobile.data.local.store.OfflineRefreshTokenStore
import org.turter.water_app_mobile.data.local.store.OfflineRefreshTokenStoreImpl
import org.turter.water_app_mobile.data.local.store.UserSettingsStore
import org.turter.water_app_mobile.data.local.store.UserSettingsStoreImpl
import org.turter.water_app_mobile.data.remote.ClientFactory
import org.turter.water_app_mobile.data.remote.WaterIntakeApiService
import org.turter.water_app_mobile.data.remote.WaterIntakeApiServiceImpl
import org.turter.water_app_mobile.data.repository.AuthRepositoryImpl
import org.turter.water_app_mobile.data.repository.ConsumeWaterVolumeRepositoryImpl
import org.turter.water_app_mobile.data.repository.UserSettingsRepositoryImpl
import org.turter.water_app_mobile.data.repository.WaterIntakeRepositoryImpl
import org.turter.water_app_mobile.di.platform.getKtorEngine
import org.turter.water_app_mobile.domain.repository.AuthRepository
import org.turter.water_app_mobile.domain.repository.ConsumeWaterVolumeRepository
import org.turter.water_app_mobile.domain.repository.UserSettingsRepository
import org.turter.water_app_mobile.domain.repository.WaterIntakeRepository

val dbModule = module {
    singleOf(::provideRoomDatabase).bind<AppDatabase>()
    singleOf(AppDatabase::consumeWaterVolumeDao).bind<ConsumeWaterVolumeDao>()
}

val storeModule = module {
    singleOf(::Settings)
    singleOf(::OfflineRefreshTokenStoreImpl).bind<OfflineRefreshTokenStore>()
    singleOf(::UserSettingsStoreImpl).bind<UserSettingsStore>()
}

@OptIn(ExperimentalOpenIdConnect::class)
val remoteModule = module {
    singleOf(::getKtorEngine)
    singleOf(::ClientFactory)

    singleOf(::OidcClientInitializer)
    singleOf(OidcClientInitializer::client)

    singleOf(ClientFactory::apiClient).bind<HttpClient>()

    singleOf(::WaterIntakeApiServiceImpl).bind<WaterIntakeApiService>()
}

@OptIn(ExperimentalOpenIdConnect::class)
val repositoryModule = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::ConsumeWaterVolumeRepositoryImpl).bind<ConsumeWaterVolumeRepository>()
    singleOf(::UserSettingsRepositoryImpl).bind<UserSettingsRepository>()
    singleOf(::WaterIntakeRepositoryImpl).bind<WaterIntakeRepository>()
}

val dataModule = module {
    includes(
        dbModule,
        storeModule,
        remoteModule,
        repositoryModule
    )
}