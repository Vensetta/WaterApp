package org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.usecase.water_volumes.AddConsumeWaterVolumesUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeStore.State

interface CreateWaterVolumeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnOpenModal : Intent
        data class SetVolumeMl(val volumeMl: Int) : Intent
        data object OnConfirm : Intent
        data object OnCloseModal : Intent
    }

    data class State(
        val isOpen: Boolean,
        val volumeMl: Int,
        val isProcessing: Boolean
    )

    sealed interface Label {
    }
}

class CreateWaterVolumeStoreFactory(
    private val storeFactory: StoreFactory,
    private val addConsumeWaterVolumesUseCase: AddConsumeWaterVolumesUseCase
) {

    fun create(): CreateWaterVolumeStore =
        object : CreateWaterVolumeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CreateWaterVolumeStore",
            initialState = State(
                isOpen = false,
                volumeMl = 0,
                isProcessing = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data object OpenModal : Msg
        data class SetVolumeMl(val volumeMl: Int) : Msg
        data object StartProcessing : Msg
        data object CloseModal : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.OnOpenModal -> {
                    dispatch(Msg.OpenModal)
                }
                is Intent.SetVolumeMl -> {
                    val volumeMl = intent.volumeMl
                    if (volumeMl > 0) dispatch(Msg.SetVolumeMl(volumeMl))
                }
                Intent.OnConfirm -> {
                    val volumeMl = state().volumeMl
                    if (volumeMl > 0) {
                        dispatch(Msg.StartProcessing)
                        scope.launch {
                            addConsumeWaterVolumesUseCase(volumeMl)
                            dispatch(Msg.CloseModal)
                        }
                    }
                }
                Intent.OnCloseModal -> {
                    dispatch(Msg.CloseModal)
                }
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.OpenModal -> {
                    copy(isOpen = true, volumeMl = 0, isProcessing = false)
                }
                is Msg.SetVolumeMl -> {
                    copy(volumeMl = message.volumeMl)
                }
                Msg.StartProcessing -> {
                    copy(isProcessing = true)
                }
                Msg.CloseModal -> {
                    copy(isOpen = false, volumeMl = 0, isProcessing = false)
                }
            }
    }
}
