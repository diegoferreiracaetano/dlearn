package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserListScreenBuilderTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = UserListScreenBuilder(i18n)

    @Test
    fun `given a list of users when build is called should return screen with top bar and list of user rows`() {
        val users = listOf(
            User(id = "1", name = "User 1", email = "u1@test.com", isPremium = true),
            User(id = "2", name = "User 2", email = "u2@test.com", isPremium = false)
        )

        val screen = builder.build(users, "en")

        assertEquals(2, screen.components.size)
        assertTrue(screen.components[0] is AppTopBarComponent)
        val list = screen.components[1] as AppListComponent
        assertEquals(2, list.components.size)
        assertTrue(list.components.all { it is UserRowComponent })
    }
}
