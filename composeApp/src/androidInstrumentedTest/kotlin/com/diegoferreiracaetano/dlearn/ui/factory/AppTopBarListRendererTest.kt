package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarItem
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class AppTopBarListRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppTopBarListRenderer()

    @Test
    fun given_AppTopBarListComponent_then_Render_should_show_top_bar() {
        val component = AppTopBarListComponent(
            items = listOf(
                AppTopBarItem(
                    actionUrl = "url1",
                    topBar = AppTopBarComponent(title = "Title 1")
                )
            ),
            selectedActionUrl = "url1"
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

        composeTestRule.onNodeWithText("Title 1").assertIsDisplayed()
    }
}
