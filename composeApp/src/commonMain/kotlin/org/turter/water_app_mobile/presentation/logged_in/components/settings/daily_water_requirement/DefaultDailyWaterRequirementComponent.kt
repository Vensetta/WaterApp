package org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultDailyWaterRequirementComponent(
    private val storeFactory: DailyWaterRequirementStoreFactory,
    private val onBackRequested: () -> Unit,
    private val onSavedRequested: () -> Unit,
    componentContext: ComponentContext
) : DailyWaterRequirementComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    DailyWaterRequirementStore.Label.OnClickBack -> onBackRequested()
                    DailyWaterRequirementStore.Label.OnSaved -> onSavedRequested()
                }
            }
        }
    }

    override val model: StateFlow<DailyWaterRequirementStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DailyWaterRequirementStore.Intent.OnClickBack)
    }

    override fun setAmountMl(amountMl: Int) {
        store.accept(DailyWaterRequirementStore.Intent.SetAmountMl(amountMl))
    }

    override fun onClickSave() {
        store.accept(DailyWaterRequirementStore.Intent.OnClickSave)
    }

    class Factory(
        private val storeFactory: DailyWaterRequirementStoreFactory
    ) {
        fun create(
            onBack: () -> Unit,
            onSaved: () -> Unit,
            componentContext: ComponentContext
        ): DailyWaterRequirementComponent =
            DefaultDailyWaterRequirementComponent(
                storeFactory = storeFactory,
                onBackRequested = onBack,
                onSavedRequested = onSaved,
                componentContext = componentContext
            )
    }
}