package org.turter.water_app_mobile.presentation.logged_in.components.home.add_water_intake

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Check
import compose.icons.feathericons.PlusCircle
import compose.icons.feathericons.Trash2
import compose.icons.feathericons.X
import org.turter.water_app_mobile.presentation.common.compose.SelectableListItem
import org.turter.water_app_mobile.presentation.logged_in.components.modals.confirm_delete_water_volume.ConfirmDeleteWaterVolumeModalContent
import org.turter.water_app_mobile.presentation.logged_in.components.modals.create_water_volume.CreateWaterVolumeModalContent

private val listItemHeight = 80.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaterIntakeContent(component: AddWaterIntakeComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            when (val selectedState = state.selectedVolumeState) {
                AddWaterIntakeStore.State.SelectedVolumeState.None ->
                    DefaultTopAppBar(onBack = component::onClickBack)

                is AddWaterIntakeStore.State.SelectedVolumeState.Present ->
                    SelectedVolumeTopAppBar(
                        volumeMl = selectedState.volume.volumeMl,
                        onUnselect = component::onUnselectVolume
                    )
            }
        },
        floatingActionButton = {
            if (state.volumeIsSelected()) {
                FloatingActionButton(
                    onClick = component::onClickAcceptAdding,
                    modifier = Modifier.size(width = 56.dp, height = 56.dp),
                    shape = FloatingActionButtonDefaults.shape
                ) {
                    Icon(
                        imageVector = FeatherIcons.Check,
                        contentDescription = null
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val volumeListState = state.volumeListState) {
                is AddWaterIntakeStore.State.VolumeListState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item("addNewVolumeItem") {
                            AddNewVolumeOptionListItem(
                                onAddVolume = component.createWaterVolumeModalComponent::open
                            )
                        }
                        items(
                            items = volumeListState.volumes,
                            key = { it.id }
                        ) { volumeItem ->
                            VolumeListItem(
                                volumeItem = volumeItem,
                                isSelected = state.selectedVolumeState.isVolumeSelected(volumeItem.id),
                                onSelect = {
                                    component.onSelectVolume(volumeItem)
                                },
                                onDelete = {
                                    component.confirmDeleteWaterVolumeModalComponent.open(
                                        volumeItem
                                    )
                                }
                            )
                        }
                    }
                }

                is AddWaterIntakeStore.State.VolumeListState.Error -> {
                    Text("Ошибка загрузки")
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

    CreateWaterVolumeModalContent(component.createWaterVolumeModalComponent)

    ConfirmDeleteWaterVolumeModalContent(component.confirmDeleteWaterVolumeModalComponent)
}

@Composable
private fun AddNewVolumeOptionListItem(onAddVolume: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(listItemHeight)
            .clickable(
                onClick = onAddVolume
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = FeatherIcons.PlusCircle,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Добавить объем",
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun VolumeListItem(
    volumeItem: VolumeItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    SelectableListItem(
        isSelected = isSelected,
        onClick = onSelect
    ) {
        Text(
            text = "${volumeItem.volumeMl} мл",
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onDelete
        ) {
            Icon(FeatherIcons.Trash2, null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Выберите объем") },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(FeatherIcons.ArrowLeft, null)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectedVolumeTopAppBar(volumeMl: Int, onUnselect: () -> Unit) {
    TopAppBar(
        title = { Text("Выбрано: $volumeMl мл") },
        navigationIcon = {
            IconButton(
                onClick = onUnselect
            ) {
                Icon(FeatherIcons.X, null)
            }
        }
    )
}

private fun AddWaterIntakeStore.State.volumeIsSelected(): Boolean =
    this.selectedVolumeState is AddWaterIntakeStore.State.SelectedVolumeState.Present

private fun AddWaterIntakeStore.State.SelectedVolumeState.isVolumeSelected(volumeId: Int): Boolean =
    when (val state = this) {
        is AddWaterIntakeStore.State.SelectedVolumeState.Present -> state.volume.id == volumeId
        AddWaterIntakeStore.State.SelectedVolumeState.None -> false
    }