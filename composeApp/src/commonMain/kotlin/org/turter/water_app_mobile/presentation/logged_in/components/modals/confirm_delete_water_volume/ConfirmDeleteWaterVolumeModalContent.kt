package org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import compose.icons.FeatherIcons
import compose.icons.feathericons.Delete
import compose.icons.feathericons.Trash2

@Composable
fun ConfirmDeleteWaterVolumeModalContent(component: ConfirmDeleteWaterVolumeModalComponent) {
    val state by component.model.collectAsState()

    if (state.isOpened) {
        AlertDialog(
            onDismissRequest = component::close,
            icon = {
                Icon(
                    imageVector = FeatherIcons.Trash2,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Delete icon"
                )
            },
            title = { Text("Удалить ${state.target?.volumeMl} мл?") },
            confirmButton = {
                TextButton(
                    onClick = component::confirmDelete
                ) {
                    Text("Подтвердить".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = component::close) {
                    Text("Закрыть".uppercase())
                }
            }
        )
    }
}