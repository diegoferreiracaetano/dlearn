package com.diegoferreiracaetano.dlearn.data.source.local

import com.russhwolf.settings.ObservableSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SettingsKeyValueStorageTest {

    private val settings = mockk<ObservableSettings>(relaxed = true)
    private lateinit var storage: SettingsKeyValueStorage

    @Before
    fun setup() {
        storage = SettingsKeyValueStorage(settings)
    }

    @Test
    fun `when put string should store it`() {
        storage.put("key", "value")
        verify { settings.putString("key", "value") }
    }

    @Test
    fun `when get string should return it`() {
        every { settings.getString("key", "") } returns "value"
        assertEquals("value", storage.get("key", ""))
    }

    @Test
    fun `when put boolean should store it`() {
        storage.put("key", true)
        verify { settings.putBoolean("key", true) }
    }

    @Test
    fun `when get boolean should return it`() {
        every { settings.getBoolean("key", false) } returns true
        assertEquals(true, storage.get("key", false))
    }

    @Test
    fun `when put int should store it`() {
        storage.put("key", 42)
        verify { settings.putInt("key", 42) }
    }

    @Test
    fun `when get int should return it`() {
        every { settings.getInt("key", 0) } returns 42
        assertEquals(42, storage.get("key", 0))
    }

    @Test
    fun `when remove should delete key`() {
        storage.remove("key")
        verify { settings.remove("key") }
    }

    @Test
    fun `when clear should delete all keys`() {
        storage.clear()
        verify { settings.clear() }
    }
}
