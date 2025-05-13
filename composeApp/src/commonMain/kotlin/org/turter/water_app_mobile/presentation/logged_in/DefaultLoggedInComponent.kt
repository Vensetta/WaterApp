package org.turter.water_app_mobile.presentation.logged_in

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.turter.water_app_mobile.presentation.extensions.componentScope
import org.turter.water_app_mobile.presentation.logged_in.components.home.DefaultHomeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.DefaultSettingsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.DefaultStatisticComponent

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLoggedInComponent(
    private val storeFactory: LoggedInStoreFactory,
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val statisticComponentFactory: DefaultStatisticComponent.Factory,
    private val settingsComponentFactory: DefaultSettingsComponent.Factory,
    componentContext: ComponentContext
) : LoggedInComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    private val navigation = StackNavigation<Config>()

    init {
//        scope.launch {
//            store.stateFlow.collect { state ->
//                navigation.bringToFront(
//                    when(state.activeTab) {
//                        Tab.HOME -> Config.Home
//                        Tab.STATISTIC -> Config.Statistic
//                        Tab.SETTINGS -> Config.Settings
//                    }
//                )
//            }
//        }
        
        scope.launch {
            stack.subscribe {
                when(it.active.instance) {
                    is LoggedInComponent.Child.Home -> {
                        store.accept(LoggedInStore.Intent.OnTabClick(Tab.HOME))
                    }
                    is LoggedInComponent.Child.Settings -> {
                        store.accept(LoggedInStore.Intent.OnTabClick(Tab.SETTINGS))
                    }
                    is LoggedInComponent.Child.Statistic -> {
                        store.accept(LoggedInStore.Intent.OnTabClick(Tab.STATISTIC))
                    }
                }
            }
        }
    }

    override val stack: Value<ChildStack<*, LoggedInComponent.Child>> = childStack(
        source = navigation,
        key = "LoggedInBottomNavigation",
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )
    override val model: StateFlow<LoggedInStore.State> = store.stateFlow

    private fun child(config: Config, componentContext: ComponentContext): LoggedInComponent.Child {
        return when(config) {
            Config.Home -> {
                LoggedInComponent.Child.Home(
                    homeComponentFactory.create(
                        onViewDetailsClick = {
                            navigation.bringToFront(Config.Statistic)
                        },
                        componentContext = componentContext
                    )
                )
            }
            Config.Statistic -> {
                LoggedInComponent.Child.Statistic(
                    statisticComponentFactory.create(componentContext)
                )
            }
            Config.Settings -> {
                LoggedInComponent.Child.Settings(
                    settingsComponentFactory.create(componentContext)
                )
            }
        }
    }

    override fun openHome() {
        navigation.bringToFront(Config.Home)
//        store.accept(LoggedInStore.Intent.OnTabClick(Tab.HOME))
    }

    override fun openStatistic() {
        navigation.bringToFront(Config.Statistic)
//        store.accept(LoggedInStore.Intent.OnTabClick(Tab.STATISTIC))
    }

    override fun openSettings() {
        navigation.bringToFront(Config.Settings)
//        store.accept(LoggedInStore.Intent.OnTabClick(Tab.SETTINGS))
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Statistic : Config

        @Serializable
        data object Settings : Config
    }

    class Factory(
        private val storeFactory: LoggedInStoreFactory,
        private val homeComponentFactory: DefaultHomeComponent.Factory,
        private val statisticComponentFactory: DefaultStatisticComponent.Factory,
        private val settingsComponentFactory: DefaultSettingsComponent.Factory
    ) {
        fun create(componentContext: ComponentContext): LoggedInComponent =
            DefaultLoggedInComponent(
                storeFactory = storeFactory,
                homeComponentFactory = homeComponentFactory,
                statisticComponentFactory = statisticComponentFactory,
                settingsComponentFactory = settingsComponentFactory,
                componentContext = componentContext
            )
    }
}