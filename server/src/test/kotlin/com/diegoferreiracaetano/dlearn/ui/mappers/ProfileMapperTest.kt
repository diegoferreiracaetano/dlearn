package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.LocaleConstants
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.Feature
import com.diegoferreiracaetano.dlearn.infrastructure.services.FeatureToggleService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProfileMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val featureToggleService = mockk<FeatureToggleService>(relaxed = true)
    private val mapper = ProfileMapper(i18n, featureToggleService)

    private val user = User(
        id = "1",
        name = "Diego",
        email = "diego@test.com",
        imageUrl = "url",
        isPremium = true
    )

    @Test
    fun `given a user when toHeader is called should map it correctly to profile header`() {
        val result = mapper.toHeader(user)
        assertEquals("Diego", result.name)
        assertEquals("diego@test.com", result.email)
    }

    @Test
    fun `given a user when toEditHeader is called should map it correctly to edit header`() {
        val result = mapper.toEditHeader(user)
        assertEquals("Diego", result.name)
    }

    @Test
    fun `given a user when toEditFields is called should return a list of mapped text fields`() {
        val result = mapper.toEditFields(user)
        assertEquals(3, result.size)
    }

    @Test
    fun `given a premium user when toPremiumBanner is called should return the premium banner component`() {
        every { i18n.getString(AppStringType.PREMIUM_MEMBER, "pt") } returns "Premium"
        val result = mapper.toPremiumBanner(user, "pt")
        assertNotNull(result)
        assertEquals("Premium", result.title)
    }

    @Test
    fun `given a non-premium user when toPremiumBanner is called should return null`() {
        val result = mapper.toPremiumBanner(user.copy(isPremium = false), "pt")
        assertNull(result)
    }

    @Test
    fun `given the member section feature is enabled when toAccountSection is called should include the member item`() {
        every { featureToggleService.isEnabled(Feature.MEMBER_SECTION) } returns true
        val result = mapper.toAccountSection("pt")
        assertEquals(2, result.items.size)
    }

    @Test
    fun `given a specific language and country when toGeneralSection is called should map correctly the display names`() {
        every { i18n.getString(AppStringType.LANGUAGE_PT_BR, LocaleConstants.LANG_PT_BR) } returns "Português"
        every { i18n.getString(AppStringType.COUNTRY_BR, LocaleConstants.LANG_PT_BR) } returns "Brasil"

        val result = mapper.toGeneralSection(LocaleConstants.LANG_PT_BR, "BR")

        assertEquals("Português", result.items.find { it.id == "language" }?.value)
        assertEquals("Brasil", result.items.find { it.id == "country" }?.value)
    }
}
