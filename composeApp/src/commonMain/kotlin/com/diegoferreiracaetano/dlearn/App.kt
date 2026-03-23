package com.diegoferreiracaetano.dlearn

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.AppNavGraph
import com.diegoferreiracaetano.dlearn.ui.screens.splash.SplashScreen
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import org.koin.compose.koinInject

@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    val preferencesRepository: PreferencesRepository = koinInject()

    // Observa mudanças globais de configuração para forçar a atualização da UI
    val configTick by preferencesRepository.onConfigurationChanged.collectAsState(initial = 0L)

    DLearnTheme {
        CompositionLocalProvider(
            LocalSnackbarHostState provides snackbarHostState
        ) {
            val sessionManager: SessionManager = koinInject()
            LaunchedEffect(Unit) {
                sessionManager.initialize()
            }

            var showLandingScreen by remember { mutableStateOf(true) }

            // Usamos o configTick como chave para reconstruir o grafo de navegação quando as preferências mudarem
          //  key(configTick) {
                if (showLandingScreen) {
                    SplashScreen(
                        onTimeout = { showLandingScreen = false },
                    )
                } else {
                    AppNavGraph(sessionManager = sessionManager)
                }
        //    }
        }
    }
}
