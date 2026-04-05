package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomNavigationRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = BottomNavigationRenderer()

    @Test
    fun given_BottomNavigationComponent_then_Render_should_show_tabs() {
        val component = BottomNavigationComponent(
            items = listOf(
                BottomNavItem(label = "Home", actionUrl = "home"),
                BottomNavItem(label = "Search", actionUrl = "search")
            ),
            selectedActionUrl = "home"
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

        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }
}
