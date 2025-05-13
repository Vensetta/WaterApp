package org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.VolumeItem
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultConfirmDeleteWaterVolumeModalComponent(
    private val storeFactory: ConfirmDeleteWaterVolumeModalStoreFactory,
    private val onConfirmDelete: (VolumeItem) -> Unit,
    componentContext: ComponentContext
): ConfirmDeleteWaterVolumeModalComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    override val model: StateFlow<ConfirmDeleteWaterVolumeModalStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    is ConfirmDeleteWaterVolumeModalStore.Label.DeleteConfirmed -> {
                        onConfirmDelete(label.target)
                    }
                }
            }
        }
    }

    override fun open(target: VolumeItem) {
        store.accept(ConfirmDeleteWaterVolumeModalStore.Intent.OnOpen(target))
    }

    override fun close() {
        store.accept(ConfirmDeleteWaterVolumeModalStore.Intent.OnClose)
    }

    override fun confirmDelete() {
        store.accept(ConfirmDeleteWaterVolumeModalStore.Intent.OnConfirm)
    }

    class Factory(
        private val storeFactory: ConfirmDeleteWaterVolumeModalStoreFactory
    ) {
        fun create(
            onConfirmDelete: (VolumeItem) -> Unit,
            componentContext: ComponentContext
        ): ConfirmDeleteWaterVolumeModalComponent =
            DefaultConfirmDeleteWaterVolumeModalComponent(
                storeFactory = storeFactory,
                onConfirmDelete = onConfirmDelete,
                componentContext = componentContext
            )
    }
}