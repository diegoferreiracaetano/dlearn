package com.diegoferreiracaetano.dlearn.ui.screens.splash

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SplashScreenTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_splash_screen_when_timeout_reached_should_trigger_onTimeout() = runComposeUiTest {
        val onTimeout: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                SplashScreen(onTimeout = onTimeout)
            }
        }

        mainClock.advanceTimeBy(3000)

        verify { onTimeout() }
    }
}
