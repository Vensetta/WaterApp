package org.turter.water_app_mobile.data.local.store

interface OfflineRefreshTokenStore {

    fun isAuthByCodeAvailable(): Boolean

    fun getOfflineRefreshToken(): String

    fun checkAccessCode(code: Int): Boolean

    suspend fun setAccessCode(code: Int)

    suspend fun setOfflineRefreshToken(token: String)
}