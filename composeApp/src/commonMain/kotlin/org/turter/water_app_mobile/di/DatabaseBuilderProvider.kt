package org.turter.water_app_mobile.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.turter.water_app_mobile.data.local.config.AppDatabase

interface DatabaseBuilderProvider {
    fun getBuilder(): RoomDatabase.Builder<AppDatabase>
}

fun provideRoomDatabase(
    provider: DatabaseBuilderProvider
): AppDatabase {
    return provider
        .getBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}