package org.turter.water_app_mobile.presentation.logged_in.components.home.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar.TodayProgressBarContent
import org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance.WeekWaterBalanceChartContent

@Composable
fun OverviewContent(component: OverviewComponent, modifier: Modifier = Modifier) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TodayProgressBarContent(component.todayProgressBarComponent)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = component::onClickWaterIntakeAdd
            ) {
                Icon(FeatherIcons.Plus, null)
            }
        }
    ) { innerPadding ->
        WeekWaterBalanceChartContent(
            component.weekWaterBalanceChartComponent,
            Modifier.padding(innerPadding)
        )
    }

}