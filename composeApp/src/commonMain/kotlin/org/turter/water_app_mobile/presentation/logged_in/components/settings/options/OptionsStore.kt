package org.turter.water_app_mobile.presentation.logged_in.components.settings.options

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.OptionsStore.State

interface OptionsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickDailyWaterRequirement : Intent
        data object OnClickUserSchedule : Intent
        data object OnClickCleanUpLog : Intent
    }

    data class State(val todo: Unit)

    sealed interface Label {
        data object OnClickDailyWaterRequirement : Label
        data object OnClickUserSchedule : Label
        data object OnClickCleanUpLog : Label
    }
}

class OptionsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): OptionsStore =
        object : OptionsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "OptionsStore",
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
                Intent.OnClickDailyWaterRequirement -> {
                    publish(Label.OnClickDailyWaterRequirement)
                }
                Intent.OnClickUserSchedule -> {
                    publish(Label.OnClickUserSchedule)
                }
                Intent.OnClickCleanUpLog -> {
                    publish(Label.OnClickCleanUpLog)
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
