package org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.launch
import org.turter.water_app_mobile.presentation.extensions.componentScope

class DefaultAuthByCodeComponent(
    private val authByCodeStoreFactory: AuthByCodeStoreFactory,
    private val onAuthDefaultClick: () -> Unit,
    componentContext: ComponentContext
) : AuthByCodeComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { authByCodeStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    AuthByCodeStore.Label.OnClickToDefaultAuth -> {
                        onAuthDefaultClick()
                    }
                }
            }
        }
    }

    override fun onDigitClick(digit: Char) {
        store.accept(AuthByCodeStore.Intent.OnClickDigit(digit))
    }

    override fun onBackspaceClick() {
        store.accept(AuthByCodeStore.Intent.OnClickDelete)
    }

    override fun onToAuthDefaultClick() {
        store.accept(AuthByCodeStore.Intent.OnClickToDefaultAuth)
    }

    class Factory(
        private val authByCodeStoreFactory: AuthByCodeStoreFactory
    ) {
        fun create(
            onAuthDefaultClick: () -> Unit,
            componentContext: ComponentContext
        ): DefaultAuthByCodeComponent =
            DefaultAuthByCodeComponent(
                authByCodeStoreFactory = authByCodeStoreFactory,
                onAuthDefaultClick = onAuthDefaultClick,
                componentContext = componentContext
            )
    }
}