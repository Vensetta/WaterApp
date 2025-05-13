package org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume

import kotlinx.coroutines.flow.StateFlow

interface CreateWaterVolumeModalComponent {

    val model: StateFlow<CreateWaterVolumeStore.State>

    fun open()

    fun close()

    fun setVolumeMl(volumeMl: Int)

    fun confirm()
}