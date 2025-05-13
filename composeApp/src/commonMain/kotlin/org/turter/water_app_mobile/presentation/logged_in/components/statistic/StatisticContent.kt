package org.turter.water_app_mobile.presentation.logged_in.components.statistic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.turter.water_app_mobile.presentation.common.compose.SelectableListItem
import org.turter.water_app_mobile.presentation.extensions.toFormatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticContent(component: StatisticComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("За сегодня") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when(val intakesState = state.waterIntakesState) {
                is StatisticStore.State.WaterIntakesState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = intakesState.list,
                            key = { it.id }
                        ) { waterIntake ->
                            SelectableListItem {
                                Spacer(Modifier.weight(1f))
                                Text("${waterIntake.dateTime.toFormatTime()} - ${waterIntake.amountMl} мл")
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                is StatisticStore.State.WaterIntakesState.Error -> {
                    Text("Ошибка загрузки")
                }
                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}