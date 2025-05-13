package org.turter.water_app_mobile.presentation.logged_in

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.turter.water_app_mobile.presentation.logged_in.LoggedInStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.LoggedInStore.Label
import org.turter.water_app_mobile.presentation.logged_in.LoggedInStore.State

interface LoggedInStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnTabClick(val tab: Tab) : Intent
    }

    data class State(val activeTab: Tab)

    sealed interface Label {
    }
}

class LoggedInStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): LoggedInStore =
        object : LoggedInStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoggedInStore",
            initialState = State(Tab.HOME),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class OnTabClick(val tab: Tab) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                is Intent.OnTabClick -> {
                    val tab = intent.tab
                    if (state().activeTab != tab) {
                        dispatch(Msg.OnTabClick(tab))
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
                is Msg.OnTabClick -> {
                    copy(activeTab = message.tab)
                }
            }
    }
}
