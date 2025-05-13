package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.entity.WaterIntake
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveDailyWaterRequirementUseCase
import org.turter.water_app_mobile.domain.usecase.water_intakes.ObserveCurrentDayIntakesUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarStore.State

interface TodayProgressBarStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickViewDetails : Intent
    }

    data class State(
        val progressBarState: ProgressBarState
    ) {
        sealed interface ProgressBarState {
            data object Initial : ProgressBarState
            data object Loading : ProgressBarState
            data object Error : ProgressBarState
            data class Loaded(val currentConsumedMl: Int, val targetMl: Int) : ProgressBarState
        }
    }

    sealed interface Label {
        data object OnClickViewDetails : Label
    }
}

class TodayProgressBarStoreFactory(
    private val storeFactory: StoreFactory,
    private val observeDailyWaterRequirementUseCase: ObserveDailyWaterRequirementUseCase,
    private val observeCurrentDayIntakesUseCase: ObserveCurrentDayIntakesUseCase
) {

    fun create(): TodayProgressBarStore =
        object : TodayProgressBarStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TodayProgressBarStore",
            initialState = State(progressBarState = State.ProgressBarState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object TodayProgressStartLoading : Action
        data object TodayProgressLoadingError : Action
        data class TodayProgressLoaded(val currentConsumedMl: Int, val targetMl: Int) : Action
    }

    private sealed interface Msg {
        data object TodayProgressStartLoading : Msg
        data object TodayProgressLoadingError : Msg
        data class TodayProgressLoaded(val currentConsumedMl: Int, val targetMl: Int) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.TodayProgressStartLoading)
                combine<Result<List<WaterIntake>>, Int, Action>(
                    observeCurrentDayIntakesUseCase(),
                    observeDailyWaterRequirementUseCase()
                ) { currentListResult, target ->
                    try {
                        currentListResult.fold(
                            onSuccess = { list ->
                                Action.TodayProgressLoaded(list.sumOf { it.amountMl }, target)
                            },
                            onFailure = {
                                Action.TodayProgressLoadingError
                            }
                        )
                    } catch (e: Exception) {
                        Action.TodayProgressLoadingError
                    }
                }.collect { dispatch(it) }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.OnClickViewDetails -> {
                    publish(Label.OnClickViewDetails)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.TodayProgressLoaded -> {
                    dispatch(Msg.TodayProgressLoaded(action.currentConsumedMl, action.targetMl))
                }

                Action.TodayProgressLoadingError -> {
                    dispatch(Msg.TodayProgressLoadingError)
                }

                Action.TodayProgressStartLoading -> {
                    dispatch(Msg.TodayProgressStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.TodayProgressLoaded -> {
                    copy(
                        progressBarState = State.ProgressBarState.Loaded(
                            message.currentConsumedMl,
                            message.targetMl
                        )
                    )
                }

                Msg.TodayProgressLoadingError -> {
                    copy(progressBarState = State.ProgressBarState.Error)
                }

                Msg.TodayProgressStartLoading -> {
                    copy(progressBarState = State.ProgressBarState.Loading)
                }
            }
    }
}
