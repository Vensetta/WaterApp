package org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.domain.usecase.user_settings.ObserveDailyWaterRequirementUseCase
import org.turter.water_app_mobile.domain.usecase.user_settings.SetDailyWaterRequirementUseCase
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementStore.Intent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementStore.Label
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementStore.State

interface DailyWaterRequirementStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data class SetAmountMl(val amountMl: Int) : Intent
        data object OnClickSave : Intent
    }

    data class State(
        val amountState: AmountState,
        val isSaving: Boolean,
        val savingError: Throwable?
    ) {
        sealed interface AmountState {
            data object Initial : AmountState
            data object CurrentLoading : AmountState
            data class Mutable(val amountMl: Int) : AmountState
        }
    }

    sealed interface Label {
        data object OnClickBack : Label
        data object OnSaved : Label
    }
}

class DailyWaterRequirementStoreFactory(
    private val storeFactory: StoreFactory,
    private val observeDailyWaterRequirementUseCase: ObserveDailyWaterRequirementUseCase,
    private val setDailyWaterRequirementUseCase: SetDailyWaterRequirementUseCase
) {

    fun create(): DailyWaterRequirementStore =
        object : DailyWaterRequirementStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DailyWaterRequirementStore",
            initialState = State(
                amountState = State.AmountState.Initial,
                isSaving = false,
                savingError = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object CurrentRequirementStartLoading : Action
        data object CurrentRequirementLoadingError : Action
        data class CurrentRequirementLoaded(val requirementMl: Int) : Action
    }

    private sealed interface Msg {
        data object CurrentRequirementStartLoading : Msg
        data object CurrentRequirementLoadingError : Msg
        data class CurrentRequirementLoaded(val requirementMl: Int) : Msg

        data object StartSaving : Msg
        data class SetAmountMl(val amountMl: Int) : Msg
        data class SavingError(val cause: Throwable) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.CurrentRequirementStartLoading)
            scope.launch {
                try {
                    dispatch(
                        Action.CurrentRequirementLoaded(
                            observeDailyWaterRequirementUseCase.invoke().first()
                        )
                    )
                } catch (e: Exception) {
                    dispatch(Action.CurrentRequirementLoadingError)
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

                is Intent.SetAmountMl -> {
                    val amountMl = intent.amountMl
                    if (amountMl > 0) dispatch(Msg.SetAmountMl(amountMl))
                }

                Intent.OnClickSave -> {
                    when (val requirementState = state().amountState) {
                        is State.AmountState.Mutable -> {
                            val amountMl = requirementState.amountMl
                            if (amountMl > 0) {
                                dispatch(Msg.StartSaving)
                                scope.launch {
                                    try {
                                        setDailyWaterRequirementUseCase(amountMl)
                                        publish(Label.OnSaved)
                                    } catch (e: Exception) {
                                        dispatch(Msg.SavingError(e))
                                    }
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
                Action.CurrentRequirementStartLoading -> {
                    dispatch(Msg.CurrentRequirementStartLoading)
                }

                Action.CurrentRequirementLoadingError -> {
                    dispatch(Msg.CurrentRequirementLoadingError)
                }

                is Action.CurrentRequirementLoaded -> {
                    dispatch(Msg.SetAmountMl(action.requirementMl))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.CurrentRequirementStartLoading -> {
                    copy(amountState = State.AmountState.CurrentLoading)
                }
                Msg.CurrentRequirementLoadingError -> {
                    copy(amountState = State.AmountState.Mutable(0))
                }
                is Msg.CurrentRequirementLoaded -> {
                    copy(amountState = State.AmountState.Mutable(message.requirementMl))
                }
                is Msg.SetAmountMl -> {
                    copy(
                        amountState = State.AmountState.Mutable(message.amountMl),
                        savingError = null
                    )
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
