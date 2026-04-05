package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRowRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = UserRowRenderer()

    @Test
    fun given_UserRowComponent_when_Rendered_then_should_displayNameAndRoleAndHandleClick() {
        val name = "Diego"
        val role = "Developer"
        val actionUrl = "profile/diego"
        val onActionMock = mockk<(String) -> Unit>(relaxed = true)
        val component = UserRowComponent(name = name, role = role, actionUrl = actionUrl)

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
        composeTestRule.onNodeWithText(role).assertExists().performClick()
        verify { onActionMock(actionUrl) }
    }
}
