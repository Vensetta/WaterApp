package org.turter.water_app_mobile.presentation.logged_out

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.turter.water_app_mobile.presentation.extensions.componentScope
import org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code.DefaultAuthByCodeComponent
import org.turter.water_app_mobile.presentation.logged_out.components.auth_default.DefaultAuthDefaultComponent
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutComponent.Child.*

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLoggedOutComponent(
    private val storeFactory: LoggedOutStoreFactory,
    private val authDefaultComponentFactory: DefaultAuthDefaultComponent.Factory,
    private val authByCodeComponentFactory: DefaultAuthByCodeComponent.Factory,
    componentContext: ComponentContext
) : LoggedOutComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    private val navigation = StackNavigation<Config>()

    init {
        scope.launch {
            store.stateFlow.collect { state ->
                when(val initialAuthTypeState = state.authTypeState) {
                    is LoggedOutStore.State.UserInitialAuthTypeState.Loaded -> {
                        navigation.replaceAll(
                            when(initialAuthTypeState.type) {
                                UserInitialAuthType.DEFAULT -> Config.AuthDefault
                                UserInitialAuthType.BY_CODE -> Config.AuthByCode
                            }
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    override val stack: Value<ChildStack<*, LoggedOutComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        key = "LoggedOutDefaultNavigation",
        initialConfiguration = Config.Initial,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): LoggedOutComponent.Child {
        return when (config) {
            Config.Initial -> {
                Initial
            }

            Config.AuthByCode -> {
                AuthByCode(
                    authByCodeComponentFactory.create(
                        onAuthDefaultClick = { navigation.replaceCurrent(Config.AuthDefault) },
                        componentContext = componentContext
                    )
                )
            }

            Config.AuthDefault -> {
                AuthDefault(
                    authDefaultComponentFactory.create(
                        onAuthByCodeClick = { navigation.replaceCurrent(Config.AuthByCode) },
                        componentContext = componentContext
                    )
                )
            }
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Initial : Config

        @Serializable
        data object AuthDefault : Config

        @Serializable
        data object AuthByCode : Config
    }

    class Factory(
        private val storeFactory: LoggedOutStoreFactory,
        private val authDefaultComponentFactory: DefaultAuthDefaultComponent.Factory,
        private val authByCodeComponentFactory: DefaultAuthByCodeComponent.Factory
    ) {
        fun create(componentContext: ComponentContext): DefaultLoggedOutComponent =
            DefaultLoggedOutComponent(
                storeFactory = storeFactory,
                authDefaultComponentFactory = authDefaultComponentFactory,
                authByCodeComponentFactory = authByCodeComponentFactory,
                componentContext = componentContext
            )
    }
}