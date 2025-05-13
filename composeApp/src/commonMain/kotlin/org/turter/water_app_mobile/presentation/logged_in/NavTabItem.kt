package org.turter.water_app_mobile.presentation.logged_in

import androidx.compose.ui.graphics.vector.ImageVector

data class NavTabItem(
    val title: String,
    val tab: Tab,
    val icon: ImageVector,
    val openScreen: () -> Unit,
    val isSelected: Boolean
)
