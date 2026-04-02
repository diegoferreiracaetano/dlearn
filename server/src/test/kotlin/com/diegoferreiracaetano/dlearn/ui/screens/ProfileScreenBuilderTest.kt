package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class ProfileScreenBuilderTest {

    private val mapper = mockk<ProfileMapper>(relaxed = true)
    private val builder = ProfileScreenBuilder(mapper)

    private val user = User(id = "1", name = "Diego", email = "diego@test.com")

    @Test
    fun `given a user without premium when build is called should return screen without premium banner`() {
        every { mapper.toPremiumBanner(any(), any()) } returns null

        val screen = builder.build(user, "en", null)

        // header + accountSection + generalSection + moreSection + footer = 5
        assertEquals(5, screen.components.size)
    }

    @Test
    fun `given a premium user when build is called should return screen with premium banner`() {
        every { mapper.toPremiumBanner(any(), any()) } returns mockk<PremiumBannerComponent>()

        val screen = builder.build(user, "en", null)

        // header + premiumBanner + accountSection + generalSection + moreSection + footer = 6
        assertEquals(6, screen.components.size)
    }

    @Test
    fun `given a user when buildContent is called should return components list`() {
        every { mapper.toPremiumBanner(any(), any()) } returns null

        val components = builder.buildContent(user, "en", "BR")

        assertEquals(5, components.size)
    }

    @Test
    fun `given a user when build is called should return screen with content from buildContent`() {
        every { mapper.toPremiumBanner(any(), any()) } returns null

        val screen = builder.build(user, "en", "BR")

        assertEquals(builder.buildContent(user, "en", "BR").size, screen.components.size)
    }
}
