package org.turter.water_app_mobile.presentation.mapper

import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.VolumeItem

fun ConsumeWaterVolume.toVolumeItem(): VolumeItem = VolumeItem(id, volumeMl, false)

fun List<ConsumeWaterVolume>.toVolumeItemList(): List<VolumeItem> =
    this.map { it.toVolumeItem() }.toList()