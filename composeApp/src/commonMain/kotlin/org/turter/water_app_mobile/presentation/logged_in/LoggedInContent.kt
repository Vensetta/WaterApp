package org.turter.water_app_mobile.presentation.logged_in

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import compose.icons.FeatherIcons
import compose.icons.feathericons.BarChart
import compose.icons.feathericons.Home
import compose.icons.feathericons.Settings
import org.turter.water_app_mobile.presentation.logged_in.components.home.HomeContent
import org.turter.water_app_mobile.presentation.logged_in.components.settings.SettingsContent
import org.turter.water_app_mobile.presentation.logged_in.components.statistic.StatisticContent

@Composable
fun LoggedInContent(component: LoggedInComponent) {
//    var selectedItem by remember { mutableIntStateOf(0) }

    val state by component.model.collectAsState()

    val tabs = listOf(
        NavTabItem("Home", Tab.HOME, FeatherIcons.Home, component::openHome, false),
        NavTabItem("Statistic", Tab.STATISTIC, FeatherIcons.BarChart, component::openStatistic, false),
        NavTabItem("Settings", Tab.SETTINGS, FeatherIcons.Settings, component::openSettings, false)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = tab.tab == state.activeTab,
                            onClick = tab.openScreen,
                            icon = { Icon(tab.icon, null) },
                            label = { Text(tab.title) }
                        )
                    }
                }
            )
        }
    ) { innerPaddings ->
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is LoggedInComponent.Child.Home -> {
                    HomeContent(instance.component, Modifier.padding(innerPaddings))
                }

                is LoggedInComponent.Child.Statistic -> {
                    StatisticContent(instance.component, Modifier.padding(innerPaddings))
                }

                is LoggedInComponent.Child.Settings -> {
                    SettingsContent(instance.component, Modifier.padding(innerPaddings))
                }
            }
        }
    }
}