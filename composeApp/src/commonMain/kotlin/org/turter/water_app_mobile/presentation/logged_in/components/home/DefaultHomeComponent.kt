package org.turter.water_app_mobile.presentation.logged_in.components.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake.DefaultAddWaterIntakeComponent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.DefaultOverviewComponent

class DefaultHomeComponent(
    private val addWaterIntakeComponentFactory: DefaultAddWaterIntakeComponent.Factory,
    private val overviewComponentFactory: DefaultOverviewComponent.Factory,
    private val onViewDetailsClick: () -> Unit,
    componentContext: ComponentContext
) : HomeComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, HomeComponent.Child>> = childStack(
        source = navigation,
        key = "HomeDefaultNavigation",
        serializer = Config.serializer(),
        initialConfiguration = Config.Overview,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): HomeComponent.Child =
        when (config) {
            Config.AddWaterIntake -> {
                HomeComponent.Child.AddWaterIntake(
                    addWaterIntakeComponentFactory.create(
                        onClickBack = {
                            navigation.pop()
                        },
                        onWaterIntakeAdded = {
                            navigation.pop()
                        },
                        componentContext = componentContext
                    )
                )
            }

            Config.Overview -> {
                HomeComponent.Child.Overview(
                    overviewComponentFactory.create(
                        onAddWaterIntakeClick = {
                            navigation.pushNew(Config.AddWaterIntake)
                        },
                        onViewDetailsClick = {
                            onViewDetailsClick()
                        },
                        componentContext = componentContext
                    )
                )
            }
        }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Overview : Config

        @Serializable
        data object AddWaterIntake : Config
    }

    class Factory(
        private val addWaterIntakeComponentFactory: DefaultAddWaterIntakeComponent.Factory,
        private val overviewComponentFactory: DefaultOverviewComponent.Factory,
    ) {
        fun create(
            onViewDetailsClick: () -> Unit,
            componentContext: ComponentContext
        ): HomeComponent =
            DefaultHomeComponent(
                addWaterIntakeComponentFactory = addWaterIntakeComponentFactory,
                overviewComponentFactory = overviewComponentFactory,
                onViewDetailsClick = onViewDetailsClick,
                componentContext = componentContext
            )
    }
}