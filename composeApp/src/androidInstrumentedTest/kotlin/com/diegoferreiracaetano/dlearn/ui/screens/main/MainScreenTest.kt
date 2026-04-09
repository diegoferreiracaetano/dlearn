package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
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
    private val currentTab = MutableStateFlow(AppNavigationRoute.HOME)

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

        onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
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
        
        waitForIdle()

        onNodeWithTag(TestTags.Components.RETRY_BUTTON).performClick()

        verify { viewModel.retry() }
    }
}
