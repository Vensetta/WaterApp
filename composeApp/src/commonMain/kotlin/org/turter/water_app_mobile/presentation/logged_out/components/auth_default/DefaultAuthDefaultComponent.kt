package org.turter.water_app_mobile.presentation.logged_out.components.auth_default

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultAuthDefaultComponent(
    private val authDefaultStoreFactory: AuthDefaultStoreFactory,
    private val onAuthByCodeClick: () -> Unit,
    componentContext: ComponentContext
) : AuthDefaultComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { authDefaultStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    AuthDefaultStore.Label.ClickToAuthByCode -> {
                        onAuthByCodeClick()
                    }
                }
            }
        }
    }

    override val model: StateFlow<AuthDefaultStore.State> = store.stateFlow

    override fun onClickLogIn() {
        store.accept(AuthDefaultStore.Intent.ClickLogIn)
    }

    override fun onClickToAuthByCode() {
        store.accept(AuthDefaultStore.Intent.ClickToAuthByCode)
    }

    class Factory(
        private val authDefaultStoreFactory: AuthDefaultStoreFactory
    ) {
        fun create(
            onAuthByCodeClick: () -> Unit,
            componentContext: ComponentContext
        ): DefaultAuthDefaultComponent =
            DefaultAuthDefaultComponent(
                authDefaultStoreFactory = authDefaultStoreFactory,
                onAuthByCodeClick = onAuthByCodeClick,
                componentContext = componentContext
            )
    }
}