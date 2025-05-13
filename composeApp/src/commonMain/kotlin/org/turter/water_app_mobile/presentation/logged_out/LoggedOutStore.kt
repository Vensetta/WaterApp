package org.turter.water_app_mobile.presentation.logged_out

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.turter.water_app_mobile.domain.usecase.auth.GetIsAuthByCodeAvailableUseCase
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutStore.Intent
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutStore.Label
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutStore.State

interface LoggedOutStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val authTypeState: UserInitialAuthTypeState
    ) {
        sealed interface UserInitialAuthTypeState {
            data object Initial : UserInitialAuthTypeState
            data object Loading : UserInitialAuthTypeState
            data class Loaded(val type: UserInitialAuthType) : UserInitialAuthTypeState
        }
    }

    sealed interface Label {
    }
}

class LoggedOutStoreFactory(
    private val storeFactory: StoreFactory,
    private val getIsAuthByCodeAvailableUseCase: GetIsAuthByCodeAvailableUseCase
) {

    fun create(): LoggedOutStore =
        object : LoggedOutStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoggedOutStore",
            initialState = State(
                authTypeState = State.UserInitialAuthTypeState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object UserInitialAuthTypeStartLoading : Action
        data object UserInitialAuthTypeLoadingError : Action
        data class UserInitialAuthTypeLoaded(val type: UserInitialAuthType) : Action
    }

    private sealed interface Msg {
        data object UserInitialAuthTypeStartLoading : Msg
        data object UserInitialAuthTypeLoadingError : Msg
        data class UserInitialAuthTypeLoaded(val type: UserInitialAuthType) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.UserInitialAuthTypeStartLoading)
            try {
                dispatch(
                    Action.UserInitialAuthTypeLoaded(
                        if (getIsAuthByCodeAvailableUseCase()) UserInitialAuthType.BY_CODE
                        else UserInitialAuthType.DEFAULT
                    )
                )
            } catch (e: Exception) {
                dispatch(Action.UserInitialAuthTypeLoadingError)
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.UserInitialAuthTypeStartLoading -> {
                    dispatch(Msg.UserInitialAuthTypeStartLoading)
                }

                Action.UserInitialAuthTypeLoadingError -> {
                    dispatch(Msg.UserInitialAuthTypeLoadingError)
                }

                is Action.UserInitialAuthTypeLoaded -> {
                    dispatch(Msg.UserInitialAuthTypeLoaded(action.type))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.UserInitialAuthTypeLoaded -> {
                    copy(authTypeState = State.UserInitialAuthTypeState.Loaded(message.type))
                }
                Msg.UserInitialAuthTypeLoadingError -> {
                    copy(authTypeState = State.UserInitialAuthTypeState.Loaded(UserInitialAuthType.DEFAULT))
                }
                Msg.UserInitialAuthTypeStartLoading -> {
                    copy(authTypeState = State.UserInitialAuthTypeState.Loading)
                }
            }
    }
}
