package org.turter.water_app_mobile.presentation.logged_in.components.settings.options

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

class DefaultOptionsComponent(
    private val storeFactory: OptionsStoreFactory,
    private val onDailyWaterRequirement: () -> Unit,
    private val onUserSchedule: () -> Unit,
    private val onCleanUpLog: () -> Unit,
    componentContext: ComponentContext
) : OptionsComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    OptionsStore.Label.OnClickDailyWaterRequirement -> onDailyWaterRequirement()
                    OptionsStore.Label.OnClickUserSchedule -> onUserSchedule()
                    OptionsStore.Label.OnClickCleanUpLog -> onCleanUpLog()
                }
            }
        }
    }

    override fun onClickDailyWaterRequirement() {
        store.accept(OptionsStore.Intent.OnClickDailyWaterRequirement)
    }

    override fun onClickUserSchedule() {
        store.accept(OptionsStore.Intent.OnClickUserSchedule)
    }

    override fun onClickCleanUpLog() {
        store.accept(OptionsStore.Intent.OnClickCleanUpLog)
    }

    class Factory(
        private val storeFactory: OptionsStoreFactory,
    ) {
        fun create(
            onDailyWaterRequirement: () -> Unit,
            onUserSchedule: () -> Unit,
            onCleanUpLog: () -> Unit,
            componentContext: ComponentContext
        ): OptionsComponent =
            DefaultOptionsComponent(
                storeFactory = storeFactory,
                onDailyWaterRequirement = onDailyWaterRequirement,
                onUserSchedule = onUserSchedule,
                onCleanUpLog = onCleanUpLog,
                componentContext = componentContext
            )
    }
}