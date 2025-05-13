package org.turter.water_app_mobile.data.mapper

import org.turter.water_app_mobile.data.local.entity.ConsumeWaterVolumeDbModel
import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume

fun ConsumeWaterVolumeDbModel.toEntity(): ConsumeWaterVolume =
    ConsumeWaterVolume(
        id = id,
        volumeMl = volumeMl
    )

fun List<ConsumeWaterVolumeDbModel>.toEntityList(): List<ConsumeWaterVolume> =
    this.map { it.toEntity() }.toList()