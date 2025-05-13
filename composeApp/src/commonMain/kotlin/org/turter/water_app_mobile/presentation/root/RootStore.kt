package org.turter.water_app_mobile.presentation.root

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.entity.AuthState
import org.turter.water_app_mobile.domain.usecase.auth.ObserveAuthUseCase
import org.turter.water_app_mobile.presentation.root.RootStore.Intent
import org.turter.water_app_mobile.presentation.root.RootStore.Label
import org.turter.water_app_mobile.presentation.root.RootStore.State

interface RootStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val authState: AuthState
    ) 

    sealed interface Label {
    }
}

class RootStoreFactory(
    private val storeFactory: StoreFactory,
    private val observeAuthUseCase: ObserveAuthUseCase
) {

    fun create(): RootStore =
        object : RootStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RootStore",
            initialState = State(
                authState = AuthState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class SetAuthState(val authState: AuthState) : Action
    }

    private sealed interface Msg {
        data class SetAuthState(val authState: AuthState) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch { 
                observeAuthUseCase().collect { authState ->
                    dispatch(Action.SetAuthState(authState))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
            when(action) {
                is Action.SetAuthState -> {
                    dispatch(Msg.SetAuthState(action.authState))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.SetAuthState -> {
                    copy(authState = message.authState)
                }
            }
    }
}
