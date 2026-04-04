package com.diegoferreiracaetano.dlearn.data.app

import com.diegoferreiracaetano.dlearn.Platform
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PreferencesRepositoryImplTest {

    private val settings: ObservableSettings = mockk()
    private val platform: Platform = mockk()
    private lateinit var repository: PreferencesRepositoryImpl

    private val storage = mutableMapOf<String, Any>()

    @Before
    fun setup() {
        storage.clear()
        every { platform.updateLocale(any(), any()) } returns Unit
        
        // Mocking settings behavior
        every { settings.getString(any(), any()) } answers {
            storage[firstArg()] as? String ?: secondArg()
        }
        every { settings.getBoolean(any(), any()) } answers {
            storage[firstArg()] as? Boolean ?: secondArg()
        }
        every { settings.putString(any(), any()) } answers {
            storage[firstArg()] = secondArg<String>()
            settings // return settings for chaining if needed
        }
        every { settings.putBoolean(any(), any()) } answers {
            storage[firstArg()] = secondArg<Boolean>()
            settings
        }
        every { settings.clear() } answers {
            storage.clear()
        }

        repository = PreferencesRepositoryImpl(settings, platform)
    }

    @Test
    fun `language should be updated and notify change`() {
        val newLanguage = "en-US"
        repository.language = newLanguage

        assertEquals(newLanguage, repository.language)
        assertEquals(1, repository.onConfigurationChanged.value)
        verify { platform.updateLocale(newLanguage, any()) }
    }

    @Test
    fun `country should be updated and notify change`() {
        val newCountry = "US"
        repository.country = newCountry

        assertEquals(newCountry, repository.country)
        assertEquals(1, repository.onConfigurationChanged.value)
        verify { platform.updateLocale(any(), newCountry) }
    }

    @Test
    fun `notificationsEnabled should be updated and notify change`() {
        val newValue = false
        repository.notificationsEnabled = newValue

        assertEquals(newValue, repository.notificationsEnabled)
        assertEquals(1, repository.onConfigurationChanged.value)
    }

    @Test
    fun `updatePreference should update correct keys`() {
        repository.updatePreference(PreferencesRepositoryImpl.KEY_LANGUAGE, "es-ES")
        assertEquals("es-ES", repository.language)

        repository.updatePreference(PreferencesRepositoryImpl.KEY_COUNTRY, "ES")
        assertEquals("ES", repository.country)

        repository.updatePreference(PreferencesRepositoryImpl.KEY_NOTIFICATIONS, "false")
        assertEquals(false, repository.notificationsEnabled)
    }

    @Test
    fun `clear should clear settings and notify change`() {
        repository.language = "en-US"
        repository.clear()

        assertEquals(PreferencesRepositoryImpl.DEFAULT_LANGUAGE, repository.language)
        assertTrue(repository.onConfigurationChanged.value > 1)
    }
}
