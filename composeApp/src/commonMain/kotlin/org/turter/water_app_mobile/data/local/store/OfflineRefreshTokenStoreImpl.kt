package org.turter.water_app_mobile.data.local.store

import co.touchlab.kermit.Logger
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set

@OptIn(ExperimentalSettingsApi::class)
class OfflineRefreshTokenStoreImpl(
    private val settings: Settings
) : OfflineRefreshTokenStore {
    companion object {
        private const val CODE_KEY = "code"
        private const val OFFLINE_ACCESS_TOKEN_KEY = "offlineAccessToken"
    }

    private val log = Logger.withTag("OfflineRefreshTokenStoreImpl")

    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    override fun isAuthByCodeAvailable(): Boolean {
        return !settings.getStringOrNull(CODE_KEY).isNullOrBlank()
                && !settings.getStringOrNull(OFFLINE_ACCESS_TOKEN_KEY).isNullOrBlank()
    }

    override fun getOfflineRefreshToken(): String {
        return settings.getString(OFFLINE_ACCESS_TOKEN_KEY, "")
    }

    override fun checkAccessCode(code: Int): Boolean {
        return settings.getIntOrNull(CODE_KEY) == code
    }

    override suspend fun setAccessCode(code: Int) {
        flowSettings.putInt(CODE_KEY, code)
    }

    override suspend fun setOfflineRefreshToken(token: String) {
        flowSettings.putString(OFFLINE_ACCESS_TOKEN_KEY, token)
    }
}