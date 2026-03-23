package com.diegoferreiracaetano.dlearn

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.AppNavGraph
import com.diegoferreiracaetano.dlearn.ui.screens.splash.SplashScreen
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    val preferencesRepository: PreferencesRepository = koinInject()
    
    // Observa mudanças nas configurações para forçar recomposição do App
    val configChanged by preferencesRepository.onConfigurationChanged.collectAsState(Unit)
    val currentLanguage = preferencesRepository.language

    // Aplica o idioma no nível do Compose (isso afeta Res.string)
    // No Compose Multiplatform 1.6.0+, os recursos reagem ao Locale.
    // Para forçar, podemos usar CompositionLocalProvider se houver um LocalLocale.
    
    DLearnTheme {
        CompositionLocalProvider(
            LocalSnackbarHostState provides snackbarHostState
        ) {
            val sessionManager: SessionManager = koinInject()
            LaunchedEffect(Unit) {
                sessionManager.initialize()
            }

            var showLandingScreen by remember { mutableStateOf(true) }

            // O uso de currentLanguage aqui garante que o bloco abaixo recomponha quando o idioma mudar
            key(currentLanguage) {
                if (showLandingScreen) {
                    SplashScreen(
                        onTimeout = { showLandingScreen = false },
                    )
                } else {
                    AppNavGraph(sessionManager = sessionManager)
                }
            }
        }
    }
}

@Composable
fun <T> key(v1: T, block: @Composable () -> Unit) {
    androidx.compose.runtime.key(v1) {
        block()
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
