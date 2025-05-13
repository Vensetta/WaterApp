package org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.turter.water_app_mobile.presentation.common.compose.FullScreenLoader
import org.turter.water_app_mobile.presentation.common.compose.IntNaturalInput
import org.turter.water_app_mobile.presentation.logged_in.components.settings.common.SettingsOptionDescription
import org.turter.water_app_mobile.presentation.logged_in.components.settings.common.SettingsOptionTopAppBar

@Composable
fun DailyWaterRequirementContent(component: DailyWaterRequirementComponent) {

    val state by component.model.collectAsState()

    Scaffold(
        topBar = {
            SettingsOptionTopAppBar(
                title = "Изменение объема",
                onBack = component::onClickBack,
                onConfirm = component::onClickSave
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val amountState = state.amountState) {
                is DailyWaterRequirementStore.State.AmountState.Mutable -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingsOptionDescription(
                            description = "Настройте объем жидкости, выбранный Вами в качестве цели на день"
                        ) {
                            IntNaturalInput(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                value = amountState.amountMl,
                                onValueChange = component::setAmountMl
                            )
                        }
                    }
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (state.isSaving) {
        FullScreenLoader()
    }
}