package org.turter.water_app_mobile.di.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun getKtorEngine(): HttpClientEngine {
    return Darwin.create {
        configureSession {
            timeoutIntervalForResource = Double.MAX_VALUE
        }
    }
}