package org.turter.water_app_mobile.presentation.logged_in.components.settings.cleanup_log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import org.turter.water_app_mobile.presentation.logged_in.components.settings.daily_water_requirement.DailyWaterRequirementStore

@Composable
fun CleanUpLogContent(component: CleanUpLogComponent) {

    val state by component.model.collectAsState()

    Scaffold(
        topBar = {
            SettingsOptionTopAppBar(
                title = "Очистка журнала",
                onBack = component::onClickBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingsOptionDescription(
                    description = "При подтверждении очиститься весь журнал приема воды"
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = component::onClickConfirm
                    ) {
                        Text("Подтведить")
                    }
                }
            }
        }
    }

    if (state.isProcessing) {
        FullScreenLoader()
    }
}