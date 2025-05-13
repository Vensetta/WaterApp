package org.turter.water_app_mobile.presentation.logged_in.components.home.overview.childs.week_water_balance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.TickPosition
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import org.turter.water_app_mobile.presentation.extensions.toFormatDdMm

private const val BarWidth = 0.8f

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun WeekWaterBalanceChartContent(
    component: WeekWaterBalanceChartComponent,
    modifier: Modifier = Modifier
) {
    val state by component.model.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 70.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val logState = state.weekLogState) {
            is WeekWaterBalanceChartStore.State.WeekLogDataState.Loaded -> {
                val days = logState.weekLog.days

                if (days.isEmpty()) {
                    Text("Данные за неделю отсутствуют")
                    return
                }

                val entries = barChartEntries(days)

                val colors = generateHueColorPalette(entries.size)

                ChartLayout(title = { ChartTitle("Статистика за неделю") }) {
                    XYGraph(
                        xAxisModel = CategoryAxisModel(days),
                        yAxisModel = FloatLinearAxisModel(
                            0f..days.maxOf { it.amountMl }.toFloat(),
                            minimumMajorTickIncrement = 1f,
                            minorTickCount = 0
                        ),
                        xAxisStyle = rememberAxisStyle(
                            tickPosition = TickPosition.Outside,
                            color = Color.LightGray
                        ),
                        xAxisLabels = {
                            AxisLabel(
                                it.date.toFormatDdMm(),
                                Modifier.padding(top = 2.dp)
//                                    .rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                            )
                        },
                        xAxisTitle = { AxisTitle("Дни недели") },
                        yAxisStyle = rememberAxisStyle(tickPosition = TickPosition.Outside),
                        yAxisLabels = {
                            AxisLabel(
                                it.toString(),
                                Modifier.absolutePadding(right = 2.dp)
                            )
                        },
                        verticalMajorGridLineStyle = null
                    ) {
                        VerticalBarPlot(
                            data = entries,
                            bar = { index ->
                                DefaultVerticalBar(
                                    brush = SolidColor(colors[index]),
                                    modifier = Modifier.fillMaxWidth(BarWidth),
                                ) {
                                    HoverSurface { Text(entries[index].y.yMax.toString()) }
                                }
                            },
                        )
                    }
                }
            }

            WeekWaterBalanceChartStore.State.WeekLogDataState.Error -> {
                Text("Ошибка загрузки")
            }
            else -> {
                CircularProgressIndicator()
            }
        }


    }
}

private fun barChartEntries(days: List<WeekLogItem.DayRecord>): List<VerticalBarPlotEntry<WeekLogItem.DayRecord, Float>> {
    return buildList {
        days.forEach { day ->
            add(
                DefaultVerticalBarPlotEntry(
                    day,
                    DefaultVerticalBarPosition(0f, day.amountMl.toFloat())
                )
            )
        }
    }
}

