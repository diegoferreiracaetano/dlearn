package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.AppFeedbackComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerifyAccountScreenBuilderTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = VerifyAccountScreenBuilder(i18n)

    @Test
    fun `given a language when build is called should return screen with 3 components`() {
        val screen = builder.build("en")

        assertEquals(3, screen.components.size)
    }

    @Test
    fun `given a language when build is called should contain a section title component`() {
        val screen = builder.build("en")

        assertTrue(screen.components.any { it is AppSectionTitleComponent })
    }

    @Test
    fun `given a language when build is called should contain a text field component`() {
        val screen = builder.build("en")

        assertTrue(screen.components.any { it is AppTextFieldComponent })
    }

    @Test
    fun `given a language when build is called should contain a feedback component`() {
        val screen = builder.build("en")

        assertTrue(screen.components.any { it is AppFeedbackComponent })
    }
}
