package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.today_progress_bar

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight

@Composable
fun TodayProgressBarContent(component: TodayProgressBarComponent, modifier: Modifier = Modifier) {
    val state by component.model.collectAsState()

    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Водный баланс",
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor
                )

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .background(Color.Transparent, ButtonDefaults.shape)
                        .clickable(
                            onClick = component::onViewDetailsClick,
                        )
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "К деталям",
                        style = MaterialTheme.typography.titleMedium,
                        color = contentColor
                    )
                    Spacer(Modifier.padding(8.dp))
                    Icon(FeatherIcons.ArrowRight, null)
                }
            }

            when (val progressState = state.progressBarState) {
                is TodayProgressBarStore.State.ProgressBarState.Loaded -> {
                    val (currentConsumedMl, targetMl) = progressState
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(15.dp),
                        progress = { currentConsumedMl.toFloat() / targetMl }
                    )
                    Text(
                        text = "$currentConsumedMl / $targetMl",
                        color = contentColor
                    )
                }
                TodayProgressBarStore.State.ProgressBarState.Error -> {
                    Text("Error")
                }
                else -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(15.dp)
                    )
                    Text(
                        text = "Загрузка",
                        color = contentColor
                    )
                }
            }


        }
    }
}