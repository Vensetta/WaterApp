package org.turter.water_app_mobile.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume

interface ConsumeWaterVolumeRepository {

    fun getVolumes(): Flow<List<ConsumeWaterVolume>>

    suspend fun addVolume(volumeMl: Int)

    suspend fun deleteVolume(volumeId: Int)

}