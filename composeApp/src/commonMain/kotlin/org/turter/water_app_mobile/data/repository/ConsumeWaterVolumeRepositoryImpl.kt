package org.turter.water_app_mobile.data.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.turter.water_app_mobile.data.local.dao.ConsumeWaterVolumeDao
import org.turter.water_app_mobile.data.local.entity.ConsumeWaterVolumeDbModel
import org.turter.water_app_mobile.data.mapper.toEntityList
import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume
import org.turter.water_app_mobile.domain.repository.ConsumeWaterVolumeRepository

class ConsumeWaterVolumeRepositoryImpl(
    private val consumeWaterVolumeDao: ConsumeWaterVolumeDao
) : ConsumeWaterVolumeRepository {
    private val log = Logger.withTag("ConsumeWaterVolumeRepositoryImpl")

    override fun getVolumes(): Flow<List<ConsumeWaterVolume>> {
        return consumeWaterVolumeDao.getAllVolumes()
            .map { it.toEntityList() }
            .onEach { log.d { "Fetching from dao consume water volumes. List size: ${it.size}" } }
    }

    override suspend fun addVolume(volumeMl: Int) {
        log.d { "Adding volume for $volumeMl ml" }
        consumeWaterVolumeDao.addVolume(ConsumeWaterVolumeDbModel(volumeMl = volumeMl))
    }

    override suspend fun deleteVolume(volumeId: Int) {
        log.d { "Deleting volume with id: $volumeId" }
        consumeWaterVolumeDao.deleteVolume(volumeId)
    }
}