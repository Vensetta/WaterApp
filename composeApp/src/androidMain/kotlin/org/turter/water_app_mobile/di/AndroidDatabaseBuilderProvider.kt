package org.turter.water_app_mobile.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.turter.water_app_mobile.data.local.config.AppDatabase

class AndroidDatabaseBuilderProvider(
    private val context: Context
) : DatabaseBuilderProvider {
    override fun getBuilder(): RoomDatabase.Builder<AppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("my_room.db")
        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath,
            klass = AppDatabase::class.java
        )
    }
}