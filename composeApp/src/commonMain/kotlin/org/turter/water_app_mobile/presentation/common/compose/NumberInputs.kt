package org.turter.water_app_mobile.presentation.common.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import io.ktor.websocket.Frame.Text
import androidx.compose.ui.text.input.ImeAction

@Composable
fun IntNaturalInput(
    modifier: Modifier = Modifier,
    value: Int,
    label: String = "Кол-во",
    onValueChange: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier,
        value = if(value <= 0) "" else value.toString(),
        onValueChange = {
            onValueChange(
                when (val res = it.toIntOrNull()) {
                    null -> value
                    else -> if (res > 0) {
                        res
                    } else value
                }
            )
        },
        label = { Text(label) },
        keyboardActions = KeyboardActions(
            onDone = {
                println(keyboardController)
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}