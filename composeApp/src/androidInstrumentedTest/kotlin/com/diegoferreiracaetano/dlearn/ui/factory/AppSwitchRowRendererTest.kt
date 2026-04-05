package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class AppSwitchRowRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppSwitchRowRenderer()

    @Test
    fun given_SwitchRowComponent_then_Render_should_displayTitleAndSubtitle() {
        val title = "Enable Notifications"
        val subtitle = "Receive alerts for new content"
        val component = AppSwitchRowComponent(
            title = title,
            subtitle = subtitle,
            preferenceKey = "notifications_enabled",
            isChecked = true
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(subtitle).assertExists()
    }
}
