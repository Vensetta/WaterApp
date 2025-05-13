package org.turter.water_app_mobile.di.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun getKtorEngine(): HttpClientEngine {
    return OkHttp.create()
}