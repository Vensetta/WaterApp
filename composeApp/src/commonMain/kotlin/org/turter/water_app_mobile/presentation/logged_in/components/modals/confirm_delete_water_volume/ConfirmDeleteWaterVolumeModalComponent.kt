package org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume

import kotlinx.coroutines.flow.StateFlow
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.VolumeItem

interface ConfirmDeleteWaterVolumeModalComponent {

    val model: StateFlow<ConfirmDeleteWaterVolumeModalStore.State>

    fun open(target: VolumeItem)

    fun close()

    fun confirmDelete()
}