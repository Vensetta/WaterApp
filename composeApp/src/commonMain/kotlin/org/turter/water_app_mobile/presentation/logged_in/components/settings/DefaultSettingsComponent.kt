package org.turter.water_app_mobile.presentation.logged_in.components.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log.DefaultCleanUpLogComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DefaultDailyWaterRequirementComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.options.DefaultOptionsComponent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule.DefaultUserScheduleComponent

class DefaultSettingsComponent(
    private val optionsComponentFactory: DefaultOptionsComponent.Factory,
    private val dailyWaterRequirementComponentFactory: DefaultDailyWaterRequirementComponent.Factory,
    private val userScheduleComponentFactory: DefaultUserScheduleComponent.Factory,
    private val cleanUpLogComponentFactory: DefaultCleanUpLogComponent.Factory,
    componentContext: ComponentContext
) : SettingsComponent, ComponentContext by componentContext {

    val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, SettingsComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        key = "SettingsDefaultNavigation",
        initialConfiguration = Config.Options,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): SettingsComponent.Child {
        return when (config) {
            Config.Options -> {
                SettingsComponent.Child.Options(
                    optionsComponentFactory.create(
                        onDailyWaterRequirement = {
                            navigation.pushNew(Config.DailyWaterRequirement)
                        },
                        onUserSchedule = {
                            navigation.pushNew(Config.UserSchedule)
                        },
                        onCleanUpLog = {
                            navigation.pushNew(Config.CleanUpLog)
                        },
                        componentContext = componentContext
                    )
                )
            }

            Config.DailyWaterRequirement -> {
                SettingsComponent.Child.DailyWaterRequirement(
                    dailyWaterRequirementComponentFactory.create(
                        onBack = {
                            navigation.pop()
                        },
                        onSaved = {
                            navigation.pop()
                        },
                        componentContext = componentContext
                    )
                )
            }

            Config.UserSchedule -> {
                SettingsComponent.Child.UserSchedule(
                    userScheduleComponentFactory.create(
                        onBack = {
                            navigation.pop()
                        },
                        onSaved = {
                            navigation.pop()
                        },
                        componentContext = componentContext
                    )
                )
            }

            Config.CleanUpLog -> {
                SettingsComponent.Child.CleanUpLog(
                    cleanUpLogComponentFactory.create(
                        onBack = {
                            navigation.pop()
                        },
                        onCleanedUp = {
                            navigation.pop()
                        },
                        componentContext = componentContext
                    )
                )
            }
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Options : Config

        @Serializable
        data object DailyWaterRequirement : Config

        @Serializable
        data object UserSchedule : Config

        @Serializable
        data object CleanUpLog : Config
    }

    class Factory(
        private val optionsComponentFactory: DefaultOptionsComponent.Factory,
        private val dailyWaterRequirementComponentFactory: DefaultDailyWaterRequirementComponent.Factory,
        private val userScheduleComponentFactory: DefaultUserScheduleComponent.Factory,
        private val cleanUpLogComponentFactory: DefaultCleanUpLogComponent.Factory
    ) {
        fun create(
            componentContext: ComponentContext
        ): SettingsComponent =
            DefaultSettingsComponent(
                optionsComponentFactory = optionsComponentFactory,
                dailyWaterRequirementComponentFactory = dailyWaterRequirementComponentFactory,
                userScheduleComponentFactory = userScheduleComponentFactory,
                cleanUpLogComponentFactory = cleanUpLogComponentFactory,
                componentContext = componentContext
            )
    }
}