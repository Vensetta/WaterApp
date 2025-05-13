package org.turter.water_app_mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consume_water_volumes")
data class ConsumeWaterVolumeDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val volumeMl: Int
)