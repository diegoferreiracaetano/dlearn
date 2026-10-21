package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.PAGE_CAROUSEL_NEXT_BUTTON_TAG
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
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
            modules(
                module {
                    single { sessionManager }
                    single { eventDispatcher }
                    single { loginViewModel }
                }
            )
        }
    }

    private fun startApp() {
        setupKoin()
        every { sessionManager.isLoggedIn } returns MutableStateFlow(false)
        every { eventDispatcher.events } returns MutableSharedFlow()
        every { loginViewModel.state } returns MutableStateFlow(LoginUIState.Idle)
        every { loginViewModel.effects } returns emptyFlow()

        composeTestRule.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalSnackbarHostState provides snackbarHostState,
            ) {
                AppNavGraph(
                    sessionManager = sessionManager,
                    eventDispatcher = eventDispatcher,
                    navController = rememberNavController(),
                )
            }
        }
    }

    private fun finishOnboarding() {
        repeat(ONBOARDING_PAGES) {
            composeTestRule.onNodeWithTag(PAGE_CAROUSEL_NEXT_BUTTON_TAG).performClick()
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun given_not_logged_in_when_start_then_destination_should_be_Onboarding() {
        startApp()

        composeTestRule.onNodeWithTag(PAGE_CAROUSEL_NEXT_BUTTON_TAG).assertExists()
    }

    @Test
    fun given_onboarding_when_finished_then_should_navigate_to_Welcome() {
        startApp()

        finishOnboarding()

        composeTestRule.onNodeWithTag(TestTags.Screens.WELCOME_SCREEN).assertExists()
    }

    @Test
    fun given_WelcomeScreen_when_click_login_then_should_navigate_to_login() {
        startApp()
        finishOnboarding()

        composeTestRule.onNodeWithTag(TestTags.Components.LOGIN_BUTTON).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TestTags.Screens.LOGIN_SCREEN).assertExists()
    }

    private companion object {
        const val ONBOARDING_PAGES = 3
    }
}
