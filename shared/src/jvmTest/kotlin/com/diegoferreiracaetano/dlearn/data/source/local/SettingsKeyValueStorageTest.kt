package com.diegoferreiracaetano.dlearn.data.source.local

import com.russhwolf.settings.ObservableSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun `when put long should store it`() {
        storage.put("key", 42L)
        verify { settings.putLong("key", 42L) }
    }

    @Test
    fun `when get long should return it`() {
        every { settings.getLong("key", 0L) } returns 42L
        assertEquals(42L, storage.get("key", 0L))
    }

    @Test
    fun `when put float should store it`() {
        storage.put("key", 42f)
        verify { settings.putFloat("key", 42f) }
    }

    @Test
    fun `when get float should return it`() {
        every { settings.getFloat("key", 0f) } returns 42f
        assertEquals(42f, storage.get("key", 0f))
    }

    @Test
    fun `when put double should store it`() {
        storage.put("key", 42.0)
        verify { settings.putDouble("key", 42.0) }
    }

    @Test
    fun `when get double should return it`() {
        every { settings.getDouble("key", 0.0) } returns 42.0
        assertEquals(42.0, storage.get("key", 0.0))
    }

    @Test
    fun `when get unsupported type should throw exception`() {
        assertFailsWith<IllegalArgumentException> {
            storage.get("key", Any())
        }
    }

    @Test
    fun `when put unsupported type should throw exception`() {
        assertFailsWith<IllegalArgumentException> {
            storage.put("key", Any())
        }
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

    @Test
    fun `when getFlow string should return flow`() = runTest {
        assertEquals("", storage.getFlow("key", "").first())
    }

    @Test
    fun `when getFlow boolean should return flow`() = runTest {
        assertEquals(false, storage.getFlow("key", false).first())
    }

    @Test
    fun `when getFlow int should return flow`() = runTest {
        assertEquals(0, storage.getFlow("key", 0).first())
    }

    @Test
    fun `when getFlow long should return flow`() = runTest {
        assertEquals(0L, storage.getFlow("key", 0L).first())
    }

    @Test
    fun `when getFlow float should return flow`() = runTest {
        assertEquals(0f, storage.getFlow("key", 0f).first())
    }

    @Test
    fun `when getFlow double should return flow`() = runTest {
        assertEquals(0.0, storage.getFlow("key", 0.0).first())
    }

    @Test
    fun `when getFlow unsupported type should throw exception`() {
        assertFailsWith<IllegalArgumentException> {
            storage.getFlow("key", Any())
        }
    }
}
