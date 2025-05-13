package org.turter.water_app_mobile.presentation.logged_in.components.settings.user_schedule

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.turter.water_app_mobile.presentation.extensions.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultUserScheduleComponent(
    private val storeFactory: UserScheduleStoreFactory,
    private val onBack: () -> Unit,
    private val onSaved: () -> Unit,
    componentContext: ComponentContext
) : UserScheduleComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    UserScheduleStore.Label.OnClickBack -> onBack()
                    UserScheduleStore.Label.ScheduleSaved -> onSaved()
                }
            }
        }
    }

    override val model: StateFlow<UserScheduleStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(UserScheduleStore.Intent.OnClickBack)
    }

    override fun onClickSave() {
        store.accept(UserScheduleStore.Intent.OnClickSave)
    }

    override fun setWakeUpTime(time: LocalTime) {
        store.accept(UserScheduleStore.Intent.SetWakeUpTime(time))
    }

    override fun setBedTime(time: LocalTime) {
        store.accept(UserScheduleStore.Intent.SetBedTime(time))
    }

    class Factory(
        private val storeFactory: UserScheduleStoreFactory,
    ) {
        fun create(
            onBack: () -> Unit,
            onSaved: () -> Unit,
            componentContext: ComponentContext
        ): UserScheduleComponent =
            DefaultUserScheduleComponent(
                storeFactory = storeFactory,
                onBack = onBack,
                onSaved = onSaved,
                componentContext = componentContext
            )
    }
}