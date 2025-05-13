package org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.usecase.auth.AuthUseCase
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeStore.Intent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeStore.Label
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.AuthByCodeStore.State

interface AuthByCodeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickDigit(val digit: Char) : Intent
        data object OnClickDelete : Intent
        data object OnClickToDefaultAuth : Intent
    }

    data class State(
        val code: AuthCodeItem,
        val resultState: ResultState
    ) {
        sealed interface ResultState {
            data object Initial : ResultState
            data object Success : ResultState
            data object Error : ResultState
        }
    }

    sealed interface Label {
        data object OnClickToDefaultAuth : Label
    }
}

class AuthByCodeStoreFactory(
    private val authUseCase: AuthUseCase,
    private val storeFactory: StoreFactory
) {

    fun create(): AuthByCodeStore =
        object : AuthByCodeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AuthByCodeStore",
            initialState = State(
                code = AuthCodeItem(),
                resultState = State.ResultState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class CodeUpdated(val code: AuthCodeItem) : Msg
        data object AuthSuccess : Msg
        data object AuthFailure : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.OnClickDelete -> {
                    dispatch(Msg.CodeUpdated(state().code.apply { removeLast() }))
                }
                is Intent.OnClickDigit -> {
                    val code = state().code.apply { addLast(intent.digit) }

                    if (code.isComplete()) scope.launch {
                        authUseCase.authenticateByCode(code.getCode())
                            .onSuccess { dispatch(Msg.AuthSuccess) }
                            .onFailure { dispatch(Msg.AuthFailure) }
                    }

                    dispatch(Msg.CodeUpdated(code))
                }
                Intent.OnClickToDefaultAuth -> {
                    publish(Label.OnClickToDefaultAuth)
                }
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.AuthFailure -> {
                    copy(
                        code = code.apply { cleanUp() },
                        resultState = State.ResultState.Error
                    )
                }
                Msg.AuthSuccess -> {
                    copy(resultState = State.ResultState.Success)
                }
                is Msg.CodeUpdated -> {
                    copy(
                        code = message.code,
                        resultState = State.ResultState.Initial
                    )
                }
            }
    }
}
