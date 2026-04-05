package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    private val viewModel: MainViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    private val currentTab = MutableStateFlow("home")

    @Test
    fun given_loading_state_when_rendered_should_show_loading_indicator() = runComposeUiTest {
        every { viewModel.uiState } returns uiState
        every { viewModel.currentTab } returns currentTab

        setContent {
            DLearnTheme {
                MainScreen(
                    onMovieClick = {},
                    onItemClick = {},
                    onTabSelected = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun given_success_state_when_tab_clicked_should_call_viewModel_onTabSelected() = runComposeUiTest {
        uiState.value = UIState.Success(Screen(components = emptyList()))
        every { viewModel.uiState } returns uiState
        every { viewModel.currentTab } returns currentTab

        setContent {
            DLearnTheme {
                MainScreen(
                    onMovieClick = {},
                    onItemClick = {},
                    onTabSelected = {},
                    viewModel = viewModel
                )
            }
        }

        // Assumindo que o item de navegação "Perfil" tem essa tag ou texto
        onNodeWithTag("tab_profile").performClick()

        verify { viewModel.onTabSelected(any()) }
    }

    @Test
    fun given_error_state_when_retry_clicked_should_call_viewModel_retry() = runComposeUiTest {
        uiState.value = UIState.Error(Exception("Error"))
        every { viewModel.uiState } returns uiState
        every { viewModel.currentTab } returns currentTab

        setContent {
            DLearnTheme {
                MainScreen(
                    onMovieClick = {},
                    onItemClick = {},
                    onTabSelected = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Tentar novamente").performClick()

        verify { viewModel.retry() }
    }
}
