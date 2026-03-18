package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent

class TopBarState(
    val component: AppTopBarComponent? = null,
    val actions: ComponentActions = ComponentActions()
)

class TopBarManager {
    var state by mutableStateOf(TopBarState())
}

val LocalTopBarManager = compositionLocalOf { TopBarManager() }

@Composable
fun ProvideTopBarManager(content: @Composable () -> Unit) {
    val manager = remember { TopBarManager() }
    CompositionLocalProvider(LocalTopBarManager provides manager) {
        content()
    }
}
