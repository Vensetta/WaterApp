package org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultCleanUpLogComponent(
    private val storeFactory: CleanUpLogStoreFactory,
    private val onCleanedUp: () -> Unit,
    private val onBack: () -> Unit,
    componentContext: ComponentContext
) : CleanUpLogComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    CleanUpLogStore.Label.OnCleanedUp -> onCleanedUp()
                    CleanUpLogStore.Label.OnClickBack -> onBack()
                }
            }
        }
    }

    override val model: StateFlow<CleanUpLogStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(CleanUpLogStore.Intent.OnClickBack)
    }

    override fun onClickConfirm() {
        store.accept(CleanUpLogStore.Intent.OnClickConfirm)
    }

    class Factory(
        private val storeFactory: CleanUpLogStoreFactory
    ) {
        fun create(
            onBack: () -> Unit,
            onCleanedUp: () -> Unit,
            componentContext: ComponentContext
        ): CleanUpLogComponent =
            DefaultCleanUpLogComponent(
                storeFactory = storeFactory,
                onBack = onBack,
                onCleanedUp = onCleanedUp,
                componentContext = componentContext
            )
    }
}