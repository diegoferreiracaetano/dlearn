package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMainContentComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module


@OptIn(ExperimentalTestApi::class)
class AppMainContentRendererTest{

    @Before
    fun setup() {
        if (GlobalContext.getOrNull() != null) stopKoin()
        startKoin {
            allowOverride(true)
            modules(module {
                single<AppViewModel> { viewModel }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private val renderer = AppMainContentRenderer()
    private val viewModel: AppViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)

    @Test
    fun given_AppMainContentComponent_then_Render_should_show_app_screen() = runComposeUiTest {

        every { viewModel.uiState } returns uiState

        val watchlistEmptyMock =
            Screen(
                components =
                    listOf(
                        AppEmptyStateComponent(
                            title = "WATCHLIST_EMPTY_TITLE",
                            description = "WATCHLIST_EMPTY_DESCRIPTION",
                            image = AppImageType.WATCHLIST,
                        ),
                    ),
            )

        uiState.value = UIState.Success(watchlistEmptyMock)

        val component = AppMainContentComponent()

        setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        onNodeWithTag(TestTags.Components.EMPTY_STATE).assertExists()
    }
}
