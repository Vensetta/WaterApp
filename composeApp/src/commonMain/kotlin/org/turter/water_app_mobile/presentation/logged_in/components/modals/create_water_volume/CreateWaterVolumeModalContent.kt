package org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.PlusCircle
import org.turter.water_app_mobile.presentation.common.compose.IntNaturalInput

@Composable
fun CreateWaterVolumeModalContent(component: CreateWaterVolumeModalComponent) {
    val state by component.model.collectAsState()

    if (state.isOpen) {
        AlertDialog(
            onDismissRequest = component::close,
            title = { Text("Новый объем") },
            icon = {
                Icon(
                    imageVector = FeatherIcons.PlusCircle,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Add icon"
                )
            },
            text = {
                IntNaturalInput(
                    value = state.volumeMl,
                    label = "Объем",
                    onValueChange = component::setVolumeMl
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            confirmButton = {
                TextButton(
                    enabled = state.volumeMl > 0,
                    onClick = component::confirm
                ) {
                    Text("Сохранить".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = component::close) {
                    Text("Отмена".uppercase())
                }
            }
        )
    }
}