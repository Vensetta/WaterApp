package org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.VolumeItem
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalStore.State

interface ConfirmDeleteWaterVolumeModalStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnOpen(val target: VolumeItem) : Intent
        data object OnClose : Intent
        data object OnConfirm : Intent
    }

    data class State(
        val isOpened: Boolean,
        val target: VolumeItem?
    )

    sealed interface Label {
        data class DeleteConfirmed(val target: VolumeItem) : Label
    }
}

class ConfirmDeleteWaterVolumeModalStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): ConfirmDeleteWaterVolumeModalStore =
        object : ConfirmDeleteWaterVolumeModalStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ConfirmDeleteWaterVolumeModalStore",
            initialState = State(
                isOpened = false,
                target = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class Open(val target: VolumeItem) : Msg
        data object Close : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                is Intent.OnOpen -> {
                    dispatch(Msg.Open(intent.target))
                }
                Intent.OnClose -> {
                    dispatch(Msg.Close)
                }
                Intent.OnConfirm -> {
                    state().target?.let { target ->
                        publish(Label.DeleteConfirmed(target))
                        dispatch(Msg.Close)
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.Open -> {
                    copy(isOpened = true, target = message.target)
                }
                Msg.Close -> {
                    copy(isOpened = false, target = null)
                }
            }
    }
}
