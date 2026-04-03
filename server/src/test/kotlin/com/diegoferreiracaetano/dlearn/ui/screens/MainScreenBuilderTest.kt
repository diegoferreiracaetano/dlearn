package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainScreenBuilderTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = MainScreenBuilder(i18n)

    @Test
    fun `given no user when build is called should return screen with container and navigation components`() {
        val screen = builder.build("en", user = null)

        assertEquals(1, screen.components.size)
        val container = screen.components.first() as AppContainerComponent
        assertTrue(container.topBar is AppTopBarListComponent)
        assertTrue(container.bottomBar is BottomNavigationComponent)
    }

    @Test
    fun `given a user when build is called should use user name as top bar title`() {
        val user = User(id = "1", name = "Diego", email = "diego@test.com", imageUrl = "http://img.png")
        val screen = builder.build("en", user = user)

        val container = screen.components.first() as AppContainerComponent
        val topBarList = container.topBar as AppTopBarListComponent
        val firstItem = topBarList.items.first()

        assertEquals("Diego", firstItem.topBar.title)
    }

    @Test
    fun `given a user with subtitle when build is called should include subtitle in top bar`() {
        val user = User(id = "1", name = "Diego", email = "diego@test.com")
        every { i18n.getString(any(), any()) } returns "Welcome back"

        val screen = builder.build("en", user = user)

        val container = screen.components.first() as AppContainerComponent
        val topBarList = container.topBar as AppTopBarListComponent
        val firstItem = topBarList.items.first()

        assertEquals("Welcome back", firstItem.topBar.subtitle)
    }

    @Test
    fun `given no user when build is called should not have subtitle in top bar`() {
        val screen = builder.build("en", user = null)

        val container = screen.components.first() as AppContainerComponent
        val topBarList = container.topBar as AppTopBarListComponent
        val firstItem = topBarList.items.first()

        assertNull(firstItem.topBar.subtitle)
    }

    @Test
    fun `given any user when build is called bottom nav should have 4 items`() {
        val screen = builder.build("en", user = null)

        val container = screen.components.first() as AppContainerComponent
        val bottomNav = container.bottomBar as BottomNavigationComponent

        assertEquals(4, bottomNav.items.size)
    }

    @Test
    fun `given any user when build is called top bar should have 4 tab items`() {
        val screen = builder.build("en", user = null)

        val container = screen.components.first() as AppContainerComponent
        val topBarList = container.topBar as AppTopBarListComponent

        assertEquals(4, topBarList.items.size)
    }

    @Test
    fun `given a user with null imageUrl when build is called should use avatar placeholder as image url`() {
        val user = User(id = "1", name = "Diego", email = "diego@test.com", imageUrl = null)

        val screen = builder.build("en", user = user)

        val container = screen.components.first() as AppContainerComponent
        val topBarList = container.topBar as AppTopBarListComponent
        val firstItem = topBarList.items.first()

        assertEquals(AppConstants.AVATAR_PLACEHOLDER, firstItem.topBar.imageUrl)
    }
}
