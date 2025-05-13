package org.turter.water_app_mobile.presentation.logged_in.components.home.overview

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.OverviewStore.State

interface OverviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickWaterIntakeAdd : Intent
    }

    data class State(val todo: Unit)

    sealed interface Label {
        data object OnClickWaterIntakeAdd : Label
    }
}

class OverviewStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): OverviewStore =
        object : OverviewStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = State(Unit),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.OnClickWaterIntakeAdd -> {
                    publish(Label.OnClickWaterIntakeAdd)
                }
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                else -> this
            }
    }
}
