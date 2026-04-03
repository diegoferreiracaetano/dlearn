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
import kotlin.test.assertTrue

class ProfileMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val featureToggleService = mockk<FeatureToggleService>(relaxed = true)
    private val mapper = ProfileMapper(i18n, featureToggleService)

    private val user = User(
        id = "1",
        name = "Diego",
        email = "diego@test.com",
        imageUrl = "url",
        isPremium = true,
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
    fun `given the member section feature is disabled when toAccountSection is called should only include password item`() {
        every { featureToggleService.isEnabled(Feature.MEMBER_SECTION) } returns false
        val result = mapper.toAccountSection("pt")
        assertEquals(1, result.items.size)
    }

    @Test
    fun `given a specific language and country when toGeneralSection is called should map correctly the display names`() {
        every { i18n.getString(AppStringType.LANGUAGE_PT_BR, LocaleConstants.LANG_PT_BR) } returns "Português"
        every { i18n.getString(AppStringType.COUNTRY_BR, LocaleConstants.LANG_PT_BR) } returns "Brasil"

        val result = mapper.toGeneralSection(LocaleConstants.LANG_PT_BR, "BR")

        assertEquals("Português", result.items.find { it.id == "language" }?.value)
        assertEquals("Brasil", result.items.find { it.id == "country" }?.value)
    }

    @Test
    fun `given an unknown language when toGeneralSection is called should use raw lang value`() {
        val result = mapper.toGeneralSection("xx", null)

        assertEquals("xx", result.items.find { it.id == "language" }?.value)
    }

    @Test
    fun `given a null country when toGeneralSection is called should return empty country value`() {
        val result = mapper.toGeneralSection("en", null)

        assertEquals("", result.items.find { it.id == "country" }?.value)
    }

    @Test
    fun `given a language when toMoreSection is called should return section with 3 items`() {
        val result = mapper.toMoreSection("en")
        assertEquals(3, result.items.size)
    }

    @Test
    fun `given a language when toSaveButton is called should return footer with save action`() {
        val result = mapper.toSaveButton("en")
        assertNotNull(result)
    }

    @Test
    fun `given a language when toFooter is called should return footer with logout label`() {
        every { i18n.getString(AppStringType.LOGOUT, "en") } returns "Logout"
        val result = mapper.toFooter("en")
        assertEquals("Logout", result.label)
    }

    @Test
    fun `given an en-US language when toGeneralSection is called should map language display name`() {
        every { i18n.getString(AppStringType.LANGUAGE_EN_US, LocaleConstants.LANG_EN_US) } returns "English"
        every { i18n.getString(AppStringType.COUNTRY_US, LocaleConstants.LANG_EN_US) } returns "USA"

        val result = mapper.toGeneralSection(LocaleConstants.LANG_EN_US, LocaleConstants.COUNTRY_US)

        assertEquals("English", result.items.find { it.id == "language" }?.value)
    }

    @Test
    fun `given toGeneralSection when called should return section with 4 items`() {
        val result = mapper.toGeneralSection("en", "US")
        assertEquals(4, result.items.size)
    }

    @Test
    fun `given an es-ES language when toGeneralSection is called should map language and country display names for Spanish`() {
        every { i18n.getString(AppStringType.LANGUAGE_ES_ES, LocaleConstants.LANG_ES_ES) } returns "Español"
        every { i18n.getString(AppStringType.COUNTRY_ES, LocaleConstants.LANG_ES_ES) } returns "España"

        val result = mapper.toGeneralSection(LocaleConstants.LANG_ES_ES, LocaleConstants.COUNTRY_ES)

        assertEquals("Español", result.items.find { it.id == "language" }?.value)
        assertEquals("España", result.items.find { it.id == "country" }?.value)
    }
}
