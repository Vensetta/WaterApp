package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.entity.WaterIntakesWeeklyLog
import org.turter.water_app_mobile.domain.usecase.water_intakes.GetWeeklyIntakesLogUseCase
import org.turter.water_app_mobile.presentation.mapper.toWeekLogItem
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartStore.State

interface WeekWaterBalanceChartStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val weekLogState: WeekLogDataState
    ) {
        sealed interface WeekLogDataState {
            data object Initial : WeekLogDataState
            data object Loading : WeekLogDataState
            data object Error : WeekLogDataState
            data class Loaded(val weekLog: WeekLogItem) : WeekLogDataState
        }
    }

    sealed interface Label {
    }
}

class WeekWaterBalanceChartStoreFactory(
    private val storeFactory: StoreFactory,
    private val getWeeklyIntakesLogUseCase: GetWeeklyIntakesLogUseCase
) {

    fun create(): WeekWaterBalanceChartStore =
        object : WeekWaterBalanceChartStore, Store<Intent, State, Label> by storeFactory.create(
            name = "WeekWaterBalanceChartStore",
            initialState = State(weekLogState = State.WeekLogDataState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object WeekLogStartLoading : Action
        data object WeekLogLoadingError : Action
        data class WeekLogLoaded(val weekLog: WaterIntakesWeeklyLog) : Action
    }

    private sealed interface Msg {
        data object WeekLogStartLoading : Msg
        data object WeekLogLoadingError : Msg
        data class WeekLogLoaded(val weekLog: WeekLogItem) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.WeekLogStartLoading)
                getWeeklyIntakesLogUseCase()
                    .onSuccess { data -> dispatch(Action.WeekLogLoaded(data)) }
                    .onFailure { dispatch(Action.WeekLogLoadingError) }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
            when(action) {
                is Action.WeekLogLoaded -> {
                    dispatch(Msg.WeekLogLoaded(action.weekLog.toWeekLogItem()))
                }
                Action.WeekLogLoadingError -> {
                    dispatch(Msg.WeekLogLoadingError)
                }
                Action.WeekLogStartLoading -> {
                    dispatch(Msg.WeekLogStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.WeekLogLoaded -> {
                    copy(weekLogState = State.WeekLogDataState.Loaded(message.weekLog))
                }
                Msg.WeekLogLoadingError -> {
                    copy(weekLogState = State.WeekLogDataState.Error)
                }
                Msg.WeekLogStartLoading -> {
                    copy(weekLogState = State.WeekLogDataState.Loading)
                }
            }
    }
}
