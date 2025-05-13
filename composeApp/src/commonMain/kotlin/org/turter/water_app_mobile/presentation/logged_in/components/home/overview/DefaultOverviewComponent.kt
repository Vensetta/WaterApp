package org.turter.water_app_mobile.presentation.logged_in.components.home.overview

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.DefaultTodayProgressBarComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.DefaultWeekWaterBalanceChartComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartComponent

class DefaultOverviewComponent(
    private val overviewStoreFactory: OverviewStoreFactory,
    private val todayProgressBarComponentFactory: DefaultTodayProgressBarComponent.Factory,
    private val weekWaterBalanceChartComponentFactory: DefaultWeekWaterBalanceChartComponent.Factory,
    private val addWaterIntakeRequested: () -> Unit,
    private val onViewDetailsClick: () -> Unit,
    componentContext: ComponentContext
) : OverviewComponent, ComponentContext by componentContext {
    private val log = Logger.withTag("DefaultOverviewComponent")

    private val store = instanceKeeper.getStore { overviewStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    OverviewStore.Label.OnClickWaterIntakeAdd -> {
                        log.d { "Collect label: OnClickWaterIntakeAdd" }
                        try {
                            log.d { "Calling addWaterIntakeRequested" }
                            addWaterIntakeRequested()
                        } catch (e: Exception) {
                            log.e(e) { "Catch exception while collecting label: OnClickWaterIntakeAdd" }
                        }
                    }
                }
            }
        }
    }

    override val todayProgressBarComponent: TodayProgressBarComponent =
        todayProgressBarComponentFactory.create(
            onViewDetailsClick = onViewDetailsClick,
            componentContext = childContext("todayProgressBarComponent", lifecycle)
        )

    override val weekWaterBalanceChartComponent: WeekWaterBalanceChartComponent =
        weekWaterBalanceChartComponentFactory.create(
            childContext(key = "weekWaterBalanceChartComponent", lifecycle)
        )

    override fun onClickWaterIntakeAdd() {
        store.accept(OverviewStore.Intent.OnClickWaterIntakeAdd)
    }

    class Factory(
        private val overviewStoreFactory: OverviewStoreFactory,
        private val todayProgressBarComponentFactory: DefaultTodayProgressBarComponent.Factory,
        private val weekWaterBalanceChartComponentFactory: DefaultWeekWaterBalanceChartComponent.Factory
    ) {
        fun create(
            onAddWaterIntakeClick: () -> Unit,
            onViewDetailsClick: () -> Unit,
            componentContext: ComponentContext
        ): DefaultOverviewComponent =
            DefaultOverviewComponent(
                overviewStoreFactory = overviewStoreFactory,
                todayProgressBarComponentFactory = todayProgressBarComponentFactory,
                weekWaterBalanceChartComponentFactory = weekWaterBalanceChartComponentFactory,
                addWaterIntakeRequested = onAddWaterIntakeClick,
                onViewDetailsClick = onViewDetailsClick,
                componentContext = componentContext
            )
    }
}