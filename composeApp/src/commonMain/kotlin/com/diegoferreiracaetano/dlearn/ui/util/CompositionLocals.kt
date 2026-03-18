package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

val LocalContentMaxHeight = staticCompositionLocalOf<Dp> {
    0.dp
}

val LocalTopBarManager = compositionLocalOf { TopBarManager() }