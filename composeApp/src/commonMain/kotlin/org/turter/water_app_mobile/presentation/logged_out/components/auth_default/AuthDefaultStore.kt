package org.turter.water_app_mobile.presentation.logged_out.components.auth_default

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.usecase.auth.AuthUseCase
import org.turter.water_app_mobile.domain.usecase.auth.GetIsAuthByCodeAvailableUseCase
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultStore.Intent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultStore.Label
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.AuthDefaultStore.State

interface AuthDefaultStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickLogIn : Intent
        data object ClickToAuthByCode : Intent
    }

    data class State(
        val isFastAuthAvailable: Boolean
    )

    sealed interface Label {
        data object ClickToAuthByCode : Label
    }
}

class AuthDefaultStoreFactory(
    private val storeFactory: StoreFactory,
    private val getIsAuthByCodeAvailableUseCase: GetIsAuthByCodeAvailableUseCase,
    private val authUseCase: AuthUseCase
) {

    fun create(): AuthDefaultStore =
        object : AuthDefaultStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AuthDefaultStore",
            initialState = State(
                isFastAuthAvailable = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class IsAuthByCodeAvailableLoaded(val value: Boolean) : Action
    }

    private sealed interface Msg {
        data object AuthByCodeAvailable : Msg
        data object AuthByCodeUnavailable : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.IsAuthByCodeAvailableLoaded(getIsAuthByCodeAvailableUseCase()))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.ClickLogIn -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        authUseCase.authenticateByRedirect()
                    }
                }
                Intent.ClickToAuthByCode -> {
                    if (state().isFastAuthAvailable) publish(Label.ClickToAuthByCode)
                }
            }
        }

        override fun executeAction(action: Action) {
            when(action) {
                is Action.IsAuthByCodeAvailableLoaded -> {
                    dispatch(
                        if (action.value) Msg.AuthByCodeAvailable
                        else Msg.AuthByCodeUnavailable
                    )
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.AuthByCodeAvailable -> {
                    copy(isFastAuthAvailable = true)
                }
                Msg.AuthByCodeUnavailable -> {
                    copy(isFastAuthAvailable = false)
                }
            }
    }
}
