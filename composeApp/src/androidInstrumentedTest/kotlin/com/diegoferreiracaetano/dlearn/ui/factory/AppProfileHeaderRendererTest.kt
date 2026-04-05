package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppProfileHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppProfileHeaderRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppProfileHeaderRenderer()

    @Test
    fun given_ProfileHeaderComponent_then_Render_should_displayNameAndEmail() {
        val name = "Diego Caetano"
        val email = "diego@example.com"
        val component = AppProfileHeaderComponent(name = name, email = email)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(name).assertExists()
        composeTestRule.onNodeWithText(email).assertExists()
    }
}
