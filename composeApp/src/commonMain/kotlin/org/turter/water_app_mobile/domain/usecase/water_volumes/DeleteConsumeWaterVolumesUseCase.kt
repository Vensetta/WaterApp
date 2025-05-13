package org.turter.water_app_mobile.domain.usecase.water_volumes

import org.turter.water_app_mobile.domain.repository.ConsumeWaterVolumeRepository

class DeleteConsumeWaterVolumesUseCase(private val repository: ConsumeWaterVolumeRepository) {
    suspend operator fun invoke(volumeId: Int) = repository.deleteVolume(volumeId)
}