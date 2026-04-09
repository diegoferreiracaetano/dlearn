package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import org.junit.Rule
import org.junit.Test

class CarouselRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = CarouselRenderer()

    @Test
    fun given_CarouselComponent_with_items_then_Render_should_show_items() {
        val component = CarouselComponent(
            items = listOf(AppLoadingComponent)
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

        composeTestRule.onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }
}
