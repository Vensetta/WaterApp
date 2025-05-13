package org.turter.water_app_mobile.domain.usecase.water_volumes

import org.turter.water_app_mobile.domain.repository.ConsumeWaterVolumeRepository

class AddConsumeWaterVolumesUseCase(private val repository: ConsumeWaterVolumeRepository) {
    suspend operator fun invoke(volumeMl: Int) = repository.addVolume(volumeMl)
}