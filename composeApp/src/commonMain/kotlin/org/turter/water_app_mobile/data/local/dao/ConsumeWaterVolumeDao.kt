package org.turter.water_app_mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.data.local.entity.ConsumeWaterVolumeDbModel

@Dao
interface ConsumeWaterVolumeDao {

    @Query("SELECT * FROM consume_water_volumes")
    fun getAllVolumes(): Flow<List<ConsumeWaterVolumeDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVolume(volume: ConsumeWaterVolumeDbModel)

    @Query("DELETE FROM consume_water_volumes WHERE id =:volumeId")
    suspend fun deleteVolume(volumeId: Int)
}