package org.turter.water_app_mobile.presentation.logged_in.components.statistic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultStatisticComponent(
    private val statisticStoreFactory: StatisticStoreFactory,
    componentContext: ComponentContext
) : StatisticComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { statisticStoreFactory.create() }

    override val model: StateFlow<StatisticStore.State> = store.stateFlow

    class Factory(
        private val statisticStoreFactory: StatisticStoreFactory
    ) {
        fun create(
            componentContext: ComponentContext
        ): StatisticComponent =
            DefaultStatisticComponent(
                statisticStoreFactory = statisticStoreFactory,
                componentContext = componentContext
            )
    }
}