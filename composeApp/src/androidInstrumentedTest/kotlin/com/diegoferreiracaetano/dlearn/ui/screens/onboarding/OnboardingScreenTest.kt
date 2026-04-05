package com.diegoferreiracaetano.dlearn.ui.screens.onboarding

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OnboardingScreenTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_last_onboarding_page_when_finish_button_clicked_should_trigger_onFinish() = runComposeUiTest {
        val onFinish: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                OnboardingScreen(onFinish = onFinish)
            }
        }

        // Navegar até a última página (são 3 páginas)
        onNodeWithText("Próximo").performClick()
        onNodeWithText("Próximo").performClick()
        
        // Na última página o texto do botão muda para "Começar" ou similar
        onNodeWithText("Começar").performClick()

        verify { onFinish() }
    }
}
