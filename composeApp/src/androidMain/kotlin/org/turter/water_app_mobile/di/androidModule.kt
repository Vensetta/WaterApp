package org.turter.water_app_mobile.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.tokenstore.AndroidSettingsTokenStore
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore

@OptIn(ExperimentalOpenIdConnect::class)
val androidModule = module {
    singleOf(::AndroidDatabaseBuilderProvider).bind<DatabaseBuilderProvider>()
    singleOf(::AndroidSettingsTokenStore).bind<TokenStore>()
}