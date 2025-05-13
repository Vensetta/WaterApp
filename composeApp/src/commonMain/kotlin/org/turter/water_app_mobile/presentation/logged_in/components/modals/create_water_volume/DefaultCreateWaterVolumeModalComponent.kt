package org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultCreateWaterVolumeModalComponent(
    private val createWaterVolumeStoreFactory: CreateWaterVolumeStoreFactory,
    componentContext: ComponentContext
) : CreateWaterVolumeModalComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { createWaterVolumeStoreFactory.create() }

    override val model: StateFlow<CreateWaterVolumeStore.State> = store.stateFlow

    override fun open() {
        store.accept(CreateWaterVolumeStore.Intent.OnOpenModal)
    }

    override fun close() {
        store.accept(CreateWaterVolumeStore.Intent.OnCloseModal)
    }

    override fun setVolumeMl(volumeMl: Int) {
        store.accept(CreateWaterVolumeStore.Intent.SetVolumeMl(volumeMl))
    }

    override fun confirm() {
        store.accept(CreateWaterVolumeStore.Intent.OnConfirm)
    }

    class Factory(
        private val createWaterVolumeStoreFactory: CreateWaterVolumeStoreFactory
    ) {
        fun create(
            componentContext: ComponentContext
        ): DefaultCreateWaterVolumeModalComponent =
            DefaultCreateWaterVolumeModalComponent(
                createWaterVolumeStoreFactory = createWaterVolumeStoreFactory,
                componentContext = componentContext
            )
    }
}