package org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.usecase.water_intakes.CleanUpUserLogUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.CleanUpLogStore.State

interface CleanUpLogStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data object OnClickConfirm : Intent
    }

    data class State(
        val isProcessing: Boolean,
        val error: Throwable?
    )

    sealed interface Label {
        data object OnClickBack : Label
        data object OnCleanedUp : Label
    }
}

class CleanUpLogStoreFactory(
    private val storeFactory: StoreFactory,
    private val cleanUpUserLogUseCase: CleanUpUserLogUseCase
) {

    fun create(): CleanUpLogStore =
        object : CleanUpLogStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CleanUpLogStore",
            initialState = State(
                isProcessing = false,
                error = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data object StartProcessing : Msg
        data class ProcessingError(val cause: Throwable) : Msg
        data object ProcessingSucceed : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.OnClickBack -> {
                    publish(Label.OnClickBack)
                }
                Intent.OnClickConfirm -> {
                    dispatch(Msg.StartProcessing)
                    scope.launch {
                        cleanUpUserLogUseCase()
                            .onSuccess {
                                publish(Label.OnCleanedUp)
                                dispatch(Msg.ProcessingSucceed)
                            }
                            .onFailure { cause ->
                                dispatch(Msg.ProcessingError(cause))
                            }
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
                Msg.StartProcessing -> {
                    copy(isProcessing = true, error = null)
                }
                is Msg.ProcessingError -> {
                    copy(isProcessing = false, error = message.cause)
                }
                Msg.ProcessingSucceed -> {
                    copy(isProcessing = false, error = null)
                }
            }
    }
}
