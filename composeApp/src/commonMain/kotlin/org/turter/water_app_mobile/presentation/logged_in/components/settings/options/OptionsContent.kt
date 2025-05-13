package org.turter.water_app_mobile.presentation.logged_in.components.settings.options

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight

private data class SettingsOption(val title: String, val action: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsContent(component: OptionsComponent) {

    val options = listOf(
        SettingsOption("Необходимый объем(мл)", component::onClickDailyWaterRequirement),
//        SettingsOption("Необходимый объем(мл)", component::onClickDailyWaterRequirement),
        SettingsOption("Очистить журнал", component::onClickCleanUpLog)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                OptionItem(option)
            }
        }
    }

}

@Composable
private fun OptionItem(item: SettingsOption) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = item.action)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.weight(1f))
        Icon(FeatherIcons.ArrowRight, null)
    }
}