package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultTodayProgressBarComponent(
    private val todayProgressBarStoreFactory: TodayProgressBarStoreFactory,
    private val viewDetailsRequested: () -> Unit,
    componentContext: ComponentContext
) : TodayProgressBarComponent, ComponentContext by componentContext {
    private val log = Logger.withTag("DefaultTodayProgressBarComponent")

    private val store = instanceKeeper.getStore { todayProgressBarStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    TodayProgressBarStore.Label.OnClickViewDetails -> {
                        viewDetailsRequested()
                    }
                }
            }
        }
    }

    override val model: StateFlow<TodayProgressBarStore.State> = store.stateFlow

    override fun onViewDetailsClick() {
        store.accept(TodayProgressBarStore.Intent.OnClickViewDetails)
    }

    class Factory(
        private val todayProgressBarStoreFactory: TodayProgressBarStoreFactory
    ) {
        fun create(
            onViewDetailsClick: () -> Unit,
            componentContext: ComponentContext
        ): DefaultTodayProgressBarComponent =
            DefaultTodayProgressBarComponent(
                todayProgressBarStoreFactory = todayProgressBarStoreFactory,
                viewDetailsRequested = onViewDetailsClick,
                componentContext = componentContext
            )
    }
}