package org.turter.water_app_mobile.presentation.logged_in.components.statistic

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.usecase.water_intakes.ObserveCurrentDayIntakesUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticStore.State

interface StatisticStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val waterIntakesState: WaterIntakesState
    ) {
        sealed interface WaterIntakesState {
            data object Initial : WaterIntakesState
            data object Loading : WaterIntakesState
            data class Error(val cause: Throwable) : WaterIntakesState
            data class Loaded(val list: List<WaterIntake>) : WaterIntakesState
        }
    }

    sealed interface Label {
    }
}

class StatisticStoreFactory(
    private val storeFactory: StoreFactory,
    private val observeCurrentDayIntakesUseCase: ObserveCurrentDayIntakesUseCase
) {

    fun create(): StatisticStore =
        object : StatisticStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StatisticStore",
            initialState = State(
                waterIntakesState = State.WaterIntakesState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object WaterIntakesStartLoading : Action
        data class WaterIntakesLoadingError(val cause: Throwable) : Action
        data class WaterIntakesLoaded(val data: List<WaterIntake>) : Action
    }

    private sealed interface Msg {
        data object WaterIntakesStartLoading : Msg
        data class WaterIntakesLoadingError(val cause: Throwable) : Msg
        data class WaterIntakesLoaded(val data: List<WaterIntake>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.WaterIntakesStartLoading)
            scope.launch {
                observeCurrentDayIntakesUseCase().collect { result ->
                    result
                        .onSuccess { list ->
                            dispatch(Action.WaterIntakesLoaded(list))
                        }
                        .onFailure { cause ->
                            dispatch(Action.WaterIntakesLoadingError(cause))
                        }
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
            when(action) {
                is Action.WaterIntakesLoaded -> {
                    dispatch(Msg.WaterIntakesLoaded(action.data))
                }
                is Action.WaterIntakesLoadingError -> {
                    dispatch(Msg.WaterIntakesLoadingError(action.cause))
                }
                Action.WaterIntakesStartLoading -> {
                    dispatch(Msg.WaterIntakesStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.WaterIntakesLoaded -> {
                    copy(waterIntakesState = State.WaterIntakesState.Loaded(message.data))
                }
                is Msg.WaterIntakesLoadingError -> {
                    copy(waterIntakesState = State.WaterIntakesState.Error(message.cause))
                }
                Msg.WaterIntakesStartLoading -> {
                    copy(waterIntakesState = State.WaterIntakesState.Loading)
                }
            }
    }
}
