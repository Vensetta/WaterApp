package org.turter.water_app_mobile.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.turter.water_app_mobile.domain.entity.AuthState
import org.turter.water_app_mobile.presentation.extensions.componentScope
import org.turter.water_app_mobile.presentation.logged_in.DefaultLoggedInComponent
import org.turter.water_app_mobile.presentation.logged_out.DefaultLoggedOutComponent
import org.turter.water_app_mobile.presentation.root.RootComponent.Child.*

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultRootComponent(
    private val storeFactory: RootStoreFactory,
    private val loggedInComponentFactory: DefaultLoggedInComponent.Factory,
    private val loggedOutComponentFactory: DefaultLoggedOutComponent.Factory,
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        key = "RootDefaultNavigation",
        initialConfiguration = Config.Initial,
        handleBackButton = true,
        childFactory = ::child
    )

    init {
        scope.launch {
            store.stateFlow.collect { state ->
                when(state.authState) {
                    is AuthState.Authenticated -> {
                        navigation.replaceAll(Config.LoggedIn)
                    }
                    AuthState.Initial -> {
                        navigation.replaceAll(Config.Initial)
                    }
                    AuthState.Loading -> {
                        navigation.replaceAll(Config.Loading)
                    }
                    AuthState.NotAuthenticated -> {
                        navigation.replaceAll(Config.LoggedOut)
                    }
                }
            }
        }
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when(config) {
            Config.Initial -> {
                Initial
            }
            Config.Loading -> {
                Loading
            }
            Config.LoggedIn -> {
                LoggedIn(
                    loggedInComponentFactory.create(componentContext)
                )
            }
            Config.LoggedOut -> {
                LoggedOut(
                    loggedOutComponentFactory.create(componentContext)
                )
            }
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Initial : Config

        @Serializable
        data object Loading : Config

        @Serializable
        data object LoggedIn : Config

        @Serializable
        data object LoggedOut : Config
    }

    class Factory(
        private val storeFactory: RootStoreFactory,
        private val loggedInComponentFactory: DefaultLoggedInComponent.Factory,
        private val loggedOutComponentFactory: DefaultLoggedOutComponent.Factory
    ) {
        fun create(componentContext: ComponentContext): RootComponent =
            DefaultRootComponent(
                storeFactory = storeFactory,
                loggedInComponentFactory = loggedInComponentFactory,
                loggedOutComponentFactory = loggedOutComponentFactory,
                componentContext = componentContext
            )
    }
}