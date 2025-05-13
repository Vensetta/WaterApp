package org.turter.water_app_mobile.data.local.config

import androidx.room.Database
import androidx.room.RoomDatabase
import org.turter.water_app_mobile.data.local.dao.ConsumeWaterVolumeDao
import org.turter.water_app_mobile.data.local.entity.ConsumeWaterVolumeDbModel

@Database(entities = [ConsumeWaterVolumeDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun consumeWaterVolumeDao(): ConsumeWaterVolumeDao
}