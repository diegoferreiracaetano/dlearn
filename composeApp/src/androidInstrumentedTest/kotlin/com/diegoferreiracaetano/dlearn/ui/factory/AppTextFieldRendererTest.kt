package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextFieldRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppTextFieldRenderer()

    @Test
    fun given_TextFieldComponent_when_InputEntered_then_should_callAction() {
        val onActionMock = mockk<(String) -> Unit>(relaxed = true)
        val key = "search_key"
        val component = AppTextFieldComponent(
            value = "",
            placeholder = AppStringType.SEARCH_PLACEHOLDER,
            key = key
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onAction = onActionMock),
                    modifier = Modifier
                )
            }
        }

        val input = "searching..."
        // Assumindo que o placeholder vazio ou mapeado resulta em um nó encontrável
        composeTestRule.onNodeWithText("").performTextInput(input)
        verify { onActionMock("$key:$input") }
    }
}
