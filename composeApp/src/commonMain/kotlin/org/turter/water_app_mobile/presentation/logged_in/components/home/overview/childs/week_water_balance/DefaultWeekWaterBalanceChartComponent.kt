package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeekWaterBalanceChartComponent(
    private val weekWaterBalanceChartStoreFactory: WeekWaterBalanceChartStoreFactory,
    componentContext: ComponentContext
) : WeekWaterBalanceChartComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { weekWaterBalanceChartStoreFactory.create() }

    override val model: StateFlow<WeekWaterBalanceChartStore.State> = store.stateFlow

    class Factory(
        private val weekWaterBalanceChartStoreFactory: WeekWaterBalanceChartStoreFactory
    ) {
        fun create(
            componentContext: ComponentContext
        ): DefaultWeekWaterBalanceChartComponent =
            DefaultWeekWaterBalanceChartComponent(
                weekWaterBalanceChartStoreFactory = weekWaterBalanceChartStoreFactory,
                componentContext = componentContext
            )
    }
}