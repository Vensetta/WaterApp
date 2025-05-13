package org.turter.water_app_mobile.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.turter.water_app_mobile.presentation.logged_in.LoggedInContent
import org.turter.water_app_mobile.presentation.logged_out.LoggedOutContent
import org.turter.water_app_mobile.theme.AppTheme

@Composable
fun RootContent(component: RootComponent) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Children(
                stack = component.stack
            ) {
                when(val instance = it.instance) {
                    RootComponent.Child.Initial -> {
                        //TODO implements welcome/loading screens later
                    }
                    RootComponent.Child.Loading -> {
                        //TODO implements welcome/loading screens later
                    }
                    is RootComponent.Child.LoggedIn -> {
                        LoggedInContent(instance.component)
                    }
                    is RootComponent.Child.LoggedOut -> {
                        LoggedOutContent(instance.component)
                    }
                }
            }
        }
    }
}