package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import org.junit.Rule
import org.junit.Test

class AppSnackbarRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppSnackbarRenderer()

    @Test
    fun given_SnackbarComponent_then_Render_should_displayMessage() {
        val message = "Test Snackbar Message"
        val component = AppSnackbarComponent(message = message)

        composeTestRule.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalSnackbarHostState provides snackbarHostState,
            ) {
                DLearnTheme {
                    SnackbarHost(hostState = snackbarHostState)
                    renderer.Render(
                        component = component,
                        actions = ComponentActions(),
                        modifier = Modifier
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(message).assertExists()
    }
}
