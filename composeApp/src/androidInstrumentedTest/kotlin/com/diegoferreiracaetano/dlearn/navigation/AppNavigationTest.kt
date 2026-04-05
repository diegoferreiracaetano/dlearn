package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class AppNavigationTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private val eventDispatcher = mockk<GlobalEventDispatcher>(relaxed = true)
    private val loginViewModel = mockk<LoginViewModel>(relaxed = true)

    private fun setupKoin() {
        stopKoin()
        startKoin {
            modules(module {
                single { sessionManager }
                single { eventDispatcher }
                single { loginViewModel }
            })
        }
    }

    @Test
    fun given_not_logged_in_when_start_then_destination_should_be_Welcome() {
        setupKoin()
        every { sessionManager.isLoggedIn } returns MutableStateFlow(false)
        every { eventDispatcher.events } returns MutableSharedFlow()
        every { loginViewModel.state } returns MutableStateFlow(LoginUIState.Idle)

        composeTestRule.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                AppNavGraph(
                    sessionManager = sessionManager,
                    eventDispatcher = eventDispatcher,
                    navController = rememberNavController()
                )
            }
        }

        // Verifica se algum texto da WelcomeScreen está presente
        composeTestRule.onNodeWithText("DLearn", ignoreCase = true).assertExists()
    }

    @Test
    fun given_WelcomeScreen_when_click_login_then_should_navigate_to_login() {
        setupKoin()
        every { sessionManager.isLoggedIn } returns MutableStateFlow(false)
        every { eventDispatcher.events } returns MutableSharedFlow()
        every { loginViewModel.state } returns MutableStateFlow(LoginUIState.Idle)

        composeTestRule.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                AppNavGraph(
                    sessionManager = sessionManager,
                    eventDispatcher = eventDispatcher,
                    navController = rememberNavController()
                )
            }
        }

        // Tenta clicar no botão de Login na WelcomeScreen
        composeTestRule.onNodeWithText("Login", ignoreCase = true).performClick()

        // Verifica se a LoginScreen foi carregada
        composeTestRule.onNodeWithText("Esqueceu a senha?", ignoreCase = true).assertExists()
    }
}
