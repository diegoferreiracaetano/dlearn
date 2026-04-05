package com.diegoferreiracaetano.dlearn.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class AppScreenRenderTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<AppViewModel>(relaxed = true)
    private val uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            modules(module {
                single { viewModel }
            })
        }
        every { viewModel.uiState } returns uiState
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun given_LoadingState_then_Render_should_showLoadingIndicator() {
        uiState.value = UIState.Loading

        composeTestRule.setContent {
            DLearnTheme {
                AppScreen(path = "test")
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun given_ErrorState_then_RetryClick_should_callViewModelRetry() {
        uiState.value = UIState.Error(Exception("Network Error"))

        composeTestRule.setContent {
            DLearnTheme {
                AppScreen(path = "test")
            }
        }

        composeTestRule.onNodeWithText("Tentar novamente").performClick()
        verify { viewModel.retry() }
    }

    @Test
    fun given_SuccessStateWithSectionTitle_then_Render_should_displayTitle() {
        val mockScreen = Screen(
            components = listOf(
                AppSectionTitleComponent(title = "Section Title")
            )
        )
        uiState.value = UIState.Success(mockScreen)

        composeTestRule.setContent {
            DLearnTheme {
                AppScreen(path = "test")
            }
        }

        composeTestRule.onNodeWithText("Section Title").assertExists()
    }
}
