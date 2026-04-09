package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalSnackbarHostState =
    staticCompositionLocalOf<SnackbarHostState> {
        error("No SnackbarHostState provided")
    }

val LocalContentMaxHeight =
    staticCompositionLocalOf {
        200.dp
    }

fun Modifier.maybeHeight(height: Dp): Modifier =
    if (height > 0.dp) this.height(height) else this.height(200.dp)
