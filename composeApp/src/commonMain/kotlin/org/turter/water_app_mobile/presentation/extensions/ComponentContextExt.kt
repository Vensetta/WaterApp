package org.turter.water_app_mobile.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope(): CoroutineScope =
    CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).also {
        lifecycle.doOnDestroy { it.cancel() }
    }