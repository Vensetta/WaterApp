package org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake

import kotlinx.coroutines.flow.StateFlow
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeModalComponent

interface AddWaterIntakeComponent {

    val model: StateFlow<AddWaterIntakeStore.State>

    val createWaterVolumeModalComponent: CreateWaterVolumeModalComponent

    val confirmDeleteWaterVolumeModalComponent: ConfirmDeleteWaterVolumeModalComponent

    fun onClickBack()

    fun onClickAcceptAdding()

    fun onSelectVolume(volume: VolumeItem)

    fun onUnselectVolume()
}