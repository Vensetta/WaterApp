package org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.domain.entity.UserSchedule
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveUserScheduleUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.SetUserScheduleUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.UserScheduleStore.State

interface UserScheduleStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data object OnClickSave : Intent
        data class SetWakeUpTime(val time: LocalTime) : Intent
        data class SetBedTime(val time: LocalTime) : Intent
    }

    data class State(
        val scheduleState: ScheduleState,
        val isSaving: Boolean,
        val savingError: Throwable?
    ) {
        sealed interface ScheduleState {
            data object Initial : ScheduleState
            data object LoadingCurrent : ScheduleState
            data class Mutable(val wakeUpTime: LocalTime, val bedTime: LocalTime) : ScheduleState
        }
    }

    sealed interface Label {
        data object OnClickBack : Label
        data object ScheduleSaved : Label
    }
}

class UserScheduleStoreFactory(
    private val storeFactory: StoreFactory,
    private val observeUserScheduleUseCase: ObserveUserScheduleUseCase,
    private val setUserScheduleUseCase: SetUserScheduleUseCase
) {

    fun create(): UserScheduleStore =
        object : UserScheduleStore, Store<Intent, State, Label> by storeFactory.create(
            name = "UserScheduleStore",
            initialState = State(
                scheduleState = State.ScheduleState.Initial,
                isSaving = false,
                savingError = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object CurrentScheduleStartLoading : Action
        data object CurrentScheduleLoadingError : Action
        data class CurrentScheduleLoaded(val schedule: UserSchedule) : Action
    }

    private sealed interface Msg {
        data object CurrentScheduleStartLoading : Msg
        data object CurrentScheduleLoadingError : Msg
        data class CurrentScheduleLoaded(val schedule: UserSchedule) : Msg

        data class SetWakeUpTime(val time: LocalTime) : Msg
        data class SetBedTime(val time: LocalTime) : Msg

        data object StartSaving : Msg
        data class SavingError(val cause: Throwable) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.CurrentScheduleStartLoading)
            scope.launch {
                try {
                    dispatch(Action.CurrentScheduleLoaded(observeUserScheduleUseCase().first()))
                } catch (e: Exception) {
                    dispatch(Action.CurrentScheduleLoadingError)
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

                is Intent.SetWakeUpTime -> {
                    dispatch(Msg.SetWakeUpTime(intent.time))
                }

                is Intent.SetBedTime -> {
                    dispatch(Msg.SetBedTime(intent.time))
                }

                Intent.OnClickSave -> {
                    when (val scheduleState = state().scheduleState) {
                        is State.ScheduleState.Mutable -> {
                            dispatch(Msg.StartSaving)
                            scope.launch {
                                try {
                                    setUserScheduleUseCase.setWakeUpTime(scheduleState.wakeUpTime)
                                    setUserScheduleUseCase.setBedTime(scheduleState.bedTime)
                                    publish(Label.ScheduleSaved)
                                } catch (e: Exception) {
                                    dispatch(Msg.SavingError(e))
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.CurrentScheduleStartLoading -> {
                    dispatch(Msg.CurrentScheduleStartLoading)
                }

                Action.CurrentScheduleLoadingError -> {
                    dispatch(Msg.CurrentScheduleLoadingError)
                }

                is Action.CurrentScheduleLoaded -> {
                    dispatch(Msg.CurrentScheduleLoaded(action.schedule))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        private val DEFAULT_WAKE_UP_TIME = LocalTime(6, 0)
        private val DEFAULT_BED_TIME = LocalTime(22, 0)

        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.CurrentScheduleStartLoading -> {
                    copy(scheduleState = State.ScheduleState.LoadingCurrent)
                }

                Msg.CurrentScheduleLoadingError -> {
                    copy(
                        scheduleState = State.ScheduleState.Mutable(
                            wakeUpTime = DEFAULT_WAKE_UP_TIME,
                            bedTime = DEFAULT_BED_TIME
                        )
                    )
                }

                is Msg.CurrentScheduleLoaded -> {
                    val (wakeUpTime, bedTime) = message.schedule
                    copy(scheduleState = State.ScheduleState.Mutable(wakeUpTime, bedTime))
                }

                is Msg.SetWakeUpTime -> {
                    mutateScheduleState {
                        copy(wakeUpTime = message.time)
                    }
                }

                is Msg.SetBedTime -> {
                    mutateScheduleState {
                        copy(bedTime = message.time)
                    }
                }

                Msg.StartSaving -> {
                    copy(isSaving = true, savingError = null)
                }

                is Msg.SavingError -> {
                    copy(isSaving = false, savingError = message.cause)
                }
            }
    }
}

private fun State.mutateScheduleState(
    action: State.ScheduleState.Mutable.() -> State.ScheduleState.Mutable
): State {
    return when (scheduleState) {
        is State.ScheduleState.Mutable -> {
            copy(scheduleState = scheduleState.action())
        }

        else -> this
    }
}