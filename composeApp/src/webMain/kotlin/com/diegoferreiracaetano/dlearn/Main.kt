package com.diegoferreiracaetano.dlearn

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.diegoferreiracaetano.dlearn.di.initKoin
import com.diegoferreiracaetano.dlearn.di.platformAuthModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(
        platformModules = listOf(platformAuthModule)
    )

    ComposeViewport {
        App()
    }
}
