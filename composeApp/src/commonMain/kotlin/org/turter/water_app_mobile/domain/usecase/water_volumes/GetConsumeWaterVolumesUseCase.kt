package org.turter.water_app_mobile.domain.usecase.water_volumes

import kotlinx.coroutines.flow.Flow
import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume
import org.turter.water_app_mobile.domain.repository.ConsumeWaterVolumeRepository

class GetConsumeWaterVolumesUseCase(private val repository: ConsumeWaterVolumeRepository) {
    operator fun invoke(): Flow<List<ConsumeWaterVolume>> = repository.getVolumes()
}