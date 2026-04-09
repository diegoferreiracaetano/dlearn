package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialogTags
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.CleanCacheViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class CleanCacheScreenTest {

    private val viewModel: CleanCacheViewModel = mockk(relaxed = true)

    @Test
    fun given_clean_cache_screen_when_confirm_clicked_should_call_viewModel_confirmClearCache() = runComposeUiTest {
        setContent {
            DLearnTheme {
                CleanCacheScreen(
                    onBackClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(AppDialogTags.CONFIRM_BUTTON).performClick()

        verify { viewModel.confirmClearCache() }
    }

    @Test
    fun given_clean_cache_screen_when_cancel_clicked_should_trigger_onBackClick() = runComposeUiTest {
        val onBackClick: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                CleanCacheScreen(
                    onBackClick = onBackClick,
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(AppDialogTags.DISMISS_BUTTON).performClick()

        verify { onBackClick() }
    }
}
