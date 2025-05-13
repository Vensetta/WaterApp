package org.turter.water_app_mobile.presentation.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SelectableListItem(
    modifier: Modifier = Modifier,
    height: Dp = 80.dp,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
    else MaterialTheme.colorScheme.surface

    val dividerPaddings = PaddingValues(horizontal = if (isSelected) 0.dp else 8.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable(
                onClick = onClick
            )
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(dividerPaddings),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(containerColor)
                .padding(horizontal = 36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
        HorizontalDivider(
            modifier = Modifier.padding(dividerPaddings),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}