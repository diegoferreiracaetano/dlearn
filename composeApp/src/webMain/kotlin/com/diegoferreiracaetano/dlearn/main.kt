package com.diegoferreiracaetano.dlearn

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.diegoferreiracaetano.dlearn.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    initKoin()

    ComposeViewport {
        App()
    }
}