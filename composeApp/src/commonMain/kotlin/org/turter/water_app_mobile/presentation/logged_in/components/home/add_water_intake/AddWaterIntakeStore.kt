package org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.turter.water_app_mobile.domain.entity.ConsumeWaterVolume
import org.turter.water_app_mobile.domain.usecase.water_intakes.AddIntakeUseCase
import org.turter.water_app_mobile.domain.usecase.water_volumes.DeleteConsumeWaterVolumesUseCase
import org.turter.water_app_mobile.domain.usecase.water_volumes.GetConsumeWaterVolumesUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.AddWaterIntakeStore.State
import org.turter.water_app_mobile.presentation.mapper.toVolumeItemList

interface AddWaterIntakeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data object OnClickAcceptAdding : Intent

        data class OnConfirmDeleteVolume(val volumeId: Int) : Intent

        data class OnSelectVolume(val volume: VolumeItem) : Intent
        data object OnUnselectVolume : Intent
    }

    data class State(
        val volumeListState: VolumeListState,
        val selectedVolumeState: SelectedVolumeState,
        val isAdding: Boolean,
        val addingError: Throwable?
    ) {
        sealed interface VolumeListState {
            data object Initial : VolumeListState
            data object Loading : VolumeListState
            data class Loaded(val volumes: List<VolumeItem>) : VolumeListState
            data class Error(val cause: Throwable) : VolumeListState
        }

        sealed interface SelectedVolumeState {
            data object None : SelectedVolumeState
            data class Present(val volume: VolumeItem) : SelectedVolumeState
        }
    }

    sealed interface Label {
        data object OnClickBack : Label
        data object OnWaterIntakeAdded : Label
    }
}

class AddWaterIntakeStoreFactory(
    private val storeFactory: StoreFactory,
    private val getConsumeWaterVolumesUseCase: GetConsumeWaterVolumesUseCase,
    private val deleteConsumeWaterVolumesUseCase: DeleteConsumeWaterVolumesUseCase,
    private val addWaterIntakeUseCase: AddIntakeUseCase
) {

    fun create(): AddWaterIntakeStore =
        object : AddWaterIntakeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddWaterIntakeStore",
            initialState = State(
                volumeListState = State.VolumeListState.Initial,
                selectedVolumeState = State.SelectedVolumeState.None,
                isAdding = false,
                addingError = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object VolumesStartLoading : Action
        data class VolumesLoadingError(val cause: Throwable) : Action
        data class VolumesLoaded(val volumes: List<ConsumeWaterVolume>) : Action
    }

    private sealed interface Msg {
        data object VolumesStartLoading : Msg
        data class VolumesLoadingError(val cause: Throwable) : Msg
        data class VolumesLoaded(val volumes: List<VolumeItem>) : Msg

        data class OnConfirmDeleteVolume(val volumeId: Int) : Msg

        data object AddingStart : Msg
        data class AddingError(val cause: Throwable) : Msg

        data class OnSelectVolume(val volume: VolumeItem) : Msg
        data object OnUnselectVolume : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.VolumesStartLoading)
                getConsumeWaterVolumesUseCase().collect { list ->
                    try {
                        dispatch(Action.VolumesLoaded(list))
                    } catch (e: Exception) {
                        dispatch(Action.VolumesLoadingError(e))
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.OnClickBack -> {
                    publish(Label.OnClickBack)
                }

                is Intent.OnConfirmDeleteVolume -> {
                    dispatch(Msg.OnConfirmDeleteVolume(intent.volumeId))
                    scope.launch {
                        deleteConsumeWaterVolumesUseCase(intent.volumeId)
                    }
                }

                Intent.OnClickAcceptAdding -> {
                    when (val selectedVolumeState = state().selectedVolumeState) {
                        is State.SelectedVolumeState.Present -> {
                            dispatch(Msg.AddingStart)
                            scope.launch {
                                try {
                                    addWaterIntakeUseCase(
                                        selectedVolumeState.volume.volumeMl,
                                        Clock.System.now()
                                            .toLocalDateTime(TimeZone.currentSystemDefault())
                                    )
                                    publish(Label.OnWaterIntakeAdded)
                                } catch (e: Exception) {
                                    dispatch(Msg.AddingError(e))
                                }
                            }
                        }

                        State.SelectedVolumeState.None -> {}
                    }
                }

                is Intent.OnSelectVolume -> {
                    dispatch(Msg.OnSelectVolume(intent.volume))
                }

                Intent.OnUnselectVolume -> {
                    dispatch(Msg.OnUnselectVolume)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.VolumesLoaded -> {
                    dispatch(Msg.VolumesLoaded(action.volumes.toVolumeItemList()))
                }

                is Action.VolumesLoadingError -> {
                    dispatch(Msg.VolumesLoadingError(action.cause))
                }

                Action.VolumesStartLoading -> {
                    dispatch(Msg.VolumesStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.AddingStart -> {
                    copy(isAdding = true)
                }

                is Msg.AddingError -> {
                    copy(isAdding = false, addingError = message.cause)
                }

                is Msg.OnConfirmDeleteVolume -> {
                    when (val listState = volumeListState) {
                        is State.VolumeListState.Loaded -> {
                            val selectedState = selectedVolumeState
                            copy(
                                volumeListState = State.VolumeListState.Loaded(
                                    listState.volumes.map { item ->
                                        if (item.id == message.volumeId) item.copy(isRemoving = true)
                                        else item
                                    }.toList()
                                ),
                                selectedVolumeState =
                                    if (selectedState is State.SelectedVolumeState.Present
                                        && selectedState.volume.id == message.volumeId)
                                        State.SelectedVolumeState.None
                                    else selectedVolumeState
                            )
                        }

                        else -> this
                    }
                }

                is Msg.OnSelectVolume -> {
                    copy(selectedVolumeState = State.SelectedVolumeState.Present(message.volume))
                }

                Msg.OnUnselectVolume -> {
                    copy(selectedVolumeState = State.SelectedVolumeState.None)
                }

                is Msg.VolumesLoaded -> {
                    copy(volumeListState = State.VolumeListState.Loaded(message.volumes))
                }

                is Msg.VolumesLoadingError -> {
                    copy(volumeListState = State.VolumeListState.Error(message.cause))
                }

                Msg.VolumesStartLoading -> {
                    copy(volumeListState = State.VolumeListState.Loading)
                }
            }
    }
}
