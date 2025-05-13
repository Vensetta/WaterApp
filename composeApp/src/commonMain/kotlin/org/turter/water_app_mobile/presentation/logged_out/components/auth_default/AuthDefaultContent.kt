package org.turter.water_app_mobile.presentation.logged_out.components.auth_default

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight

@Composable
fun AuthDefaultContent(component: AuthDefaultComponent) {

    val state by component.model.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Water App",
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = component::onClickLogIn
            ) {
                Text("Login")
            }

            if (state.isFastAuthAvailable) {
                TextButton(
                    onClick = component::onClickToAuthByCode
                ) {
                    Text("Auth by code")
                    Icon(FeatherIcons.ArrowRight, null)
                }
            }
        }
    }
}