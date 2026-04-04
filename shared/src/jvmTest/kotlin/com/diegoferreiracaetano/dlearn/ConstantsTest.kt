package com.diegoferreiracaetano.dlearn

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantsTest {

    @Test
    fun `Constants are consistent`() {
        assertEquals("123456", Constants.DEFAULT_MOCK_OTP)
        assertEquals("000000", Constants.DEBUG_OTP)
        assertEquals(428, Constants.HTTP_STATUS_PRECONDITION_REQUIRED)
    }

    @Test
    fun `AppConstants are consistent`() {
        assertEquals("DLearn", AppConstants.APP_NAME)
        assertEquals("1.0.0", AppConstants.APP_VERSION)
    }

    @Test
    fun `UIConstants are consistent`() {
        assertEquals("L", UIConstants.DEFAULT_CONTENT_RATING)
        assertEquals("type", UIConstants.TYPE_DISCRIMINATOR)
    }

    @Test
    fun `PreferenceConstants are consistent`() {
        assertEquals("pref_notifications", PreferenceConstants.PREF_NOTIFICATIONS)
        assertEquals("pref_language", PreferenceConstants.PREF_LANGUAGE)
        assertEquals("pref_country", PreferenceConstants.PREF_COUNTRY)
    }

    @Test
    fun `LocaleConstants are consistent`() {
        assertEquals("pt-BR", LocaleConstants.LANG_PT_BR)
        assertEquals("BR", LocaleConstants.COUNTRY_BR)
    }

    @Test
    fun `ProfileConstants are consistent`() {
        assertEquals("member", ProfileConstants.ID_MEMBER)
        assertEquals("password", ProfileConstants.ID_PASSWORD)
    }

    @Test
    fun `TokenConstants are consistent`() {
        assertEquals("user_id", TokenConstants.CLAIM_USER_ID)
        assertEquals("Bearer ", TokenConstants.BEARER_PREFIX)
    }

    @Test
    fun `ApiEndpoints are consistent`() {
        assertEquals("v1/home", ApiEndpoints.V1_HOME)
        assertEquals("/v1/app", ApiEndpoints.V1_APP)
    }

    @Test
    fun `MetadataKeys are consistent`() {
        assertEquals("external_id", MetadataKeys.EXTERNAL_ID)
        assertEquals("auth_type", MetadataKeys.AUTH_TYPE)
    }

    @Test
    fun `SocialAuthConstants are consistent`() {
        assertEquals("email", SocialAuthConstants.CLAIM_EMAIL)
        assertEquals(".", SocialAuthConstants.JWT_SEPARATOR)
    }

    @Test
    fun `TmdbConstants are consistent`() {
        assertEquals("https://image.tmdb.org/t/p/", TmdbConstants.IMAGE_BASE_URL)
        assertEquals("w500", TmdbConstants.IMAGE_W500)
    }

    @Test
    fun `FrameworkConstants are consistent`() {
        assertEquals("swagger", FrameworkConstants.SWAGGER_PATH)
        assertEquals("documentation.yaml", FrameworkConstants.DOCUMENTATION_FILE)
    }
}
