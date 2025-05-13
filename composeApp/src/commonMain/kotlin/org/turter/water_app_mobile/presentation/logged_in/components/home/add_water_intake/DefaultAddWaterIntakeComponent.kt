package org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.DefaultConfirmDeleteWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeModalComponent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.DefaultCreateWaterVolumeModalComponent

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultAddWaterIntakeComponent(
    private val addWaterIntakeStoreFactory: AddWaterIntakeStoreFactory,
    createWaterVolumeComponentFactory: DefaultCreateWaterVolumeModalComponent.Factory,
    confirmDeleteWaterVolumeComponentFactory: DefaultConfirmDeleteWaterVolumeModalComponent.Factory,
    private val onBackRequested: () -> Unit,
    private val onWaterIntakeAdded: () -> Unit,
    componentContext: ComponentContext
) : AddWaterIntakeComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { addWaterIntakeStoreFactory.create() }
    private val scope = componentScope()

    override val model: StateFlow<AddWaterIntakeStore.State> = store.stateFlow

    override val createWaterVolumeModalComponent: CreateWaterVolumeModalComponent =
        createWaterVolumeComponentFactory.create(
            childContext("createWaterVolumeModalComponent")
        )

    override val confirmDeleteWaterVolumeModalComponent: ConfirmDeleteWaterVolumeModalComponent =
        confirmDeleteWaterVolumeComponentFactory.create(
            onConfirmDelete = { volumeItem ->
                store.accept(AddWaterIntakeStore.Intent.OnConfirmDeleteVolume(volumeItem.id))
            },
            componentContext = childContext("confirmDeleteWaterVolumeModalComponent")
        )

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    AddWaterIntakeStore.Label.OnClickBack -> {
                        onBackRequested()
                    }
                    AddWaterIntakeStore.Label.OnWaterIntakeAdded -> {
                        onWaterIntakeAdded()
                    }
                }
            }
        }
    }

    override fun onClickBack() {
        store.accept(AddWaterIntakeStore.Intent.OnClickBack)
    }

    override fun onClickAcceptAdding() {
        store.accept(AddWaterIntakeStore.Intent.OnClickAcceptAdding)
    }

    override fun onSelectVolume(volume: VolumeItem) {
        store.accept(AddWaterIntakeStore.Intent.OnSelectVolume(volume))
    }

    override fun onUnselectVolume() {
        store.accept(AddWaterIntakeStore.Intent.OnUnselectVolume)
    }

    class Factory(
        private val addWaterIntakeStoreFactory: AddWaterIntakeStoreFactory,
        private val createWaterVolumeComponentFactory: DefaultCreateWaterVolumeModalComponent.Factory,
        private val confirmDeleteWaterVolumeComponentFactory: DefaultConfirmDeleteWaterVolumeModalComponent.Factory
    ) {
        fun create(
            onClickBack: () -> Unit,
            onWaterIntakeAdded: () -> Unit,
            componentContext: ComponentContext
        ): AddWaterIntakeComponent =
            DefaultAddWaterIntakeComponent(
                addWaterIntakeStoreFactory = addWaterIntakeStoreFactory,
                createWaterVolumeComponentFactory = createWaterVolumeComponentFactory,
                confirmDeleteWaterVolumeComponentFactory = confirmDeleteWaterVolumeComponentFactory,
                onBackRequested = onClickBack,
                onWaterIntakeAdded = onWaterIntakeAdded,
                componentContext = componentContext
            )
    }
}