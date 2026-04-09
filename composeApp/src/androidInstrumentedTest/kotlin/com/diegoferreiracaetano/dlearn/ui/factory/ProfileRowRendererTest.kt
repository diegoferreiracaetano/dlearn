package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class ProfileRowRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = ProfileRowRenderer()

    @Test
    fun given_ProfileRowComponent_then_Render_should_displayNameAndEmailAndHandleEditClick() {
        val name = "Diego"
        val email = "diego@example.com"
        val editActionUrl = "profile/edit"
        val onActionMock = mockk<(String) -> Unit>(relaxed = true)
        val component = ProfileRowComponent(name = name, email = email, editActionUrl = editActionUrl)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onAction = onActionMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(name).assertExists()
        composeTestRule.onNodeWithText(email).assertExists()
    }
}
