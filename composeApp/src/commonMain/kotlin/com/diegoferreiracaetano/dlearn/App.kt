package com.diegoferreiracaetano.dlearn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.AppNavGraph
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.view.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun App() {
    DLearnTheme {
        val sessionManager: SessionManager = koinInject()
        LaunchedEffect(Unit) {
            sessionManager.initialize()
        }

        var showLandingScreen by remember { mutableStateOf(true) }

        if (showLandingScreen) {
            SplashScreen(
                onTimeout = { showLandingScreen = false },
            )
        } else {
            AppNavGraph(sessionManager = sessionManager)
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
