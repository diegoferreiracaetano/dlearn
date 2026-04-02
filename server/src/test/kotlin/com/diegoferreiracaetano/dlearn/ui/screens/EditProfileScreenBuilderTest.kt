package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertTrue

class EditProfileScreenBuilderTest {

    private val mapper = mockk<ProfileMapper>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = EditProfileScreenBuilder(mapper, i18n)

    @Test
    fun `given user and status when build is called should return screen with snackbar`() {
        val user = mockk<User>(relaxed = true)
        val screen = builder.build(user, "en", AppStringType.SAVE_CHANGES)

        val container = screen.components.first() as AppContainerComponent
        assertTrue(container.components.any { it is AppSnackbarComponent })
    }
}
