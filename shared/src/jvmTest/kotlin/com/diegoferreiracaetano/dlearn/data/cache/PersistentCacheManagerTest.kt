package com.diegoferreiracaetano.dlearn.data.cache

import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PersistentCacheManagerTest {

    private val keyValueStorage = mockk<KeyValueStorage>(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }
    private val cacheManager = PersistentCacheManager(keyValueStorage, json)

    @Serializable
    data class TestData(val name: String)

    @Test
    fun `when put is called should encode and store value`() {
        val data = TestData("test")
        val key = "key"
        
        cacheManager.put(key, data, TestData.serializer())

        verify(exactly = 1) { keyValueStorage.put(key, json.encodeToString(TestData.serializer(), data)) }
    }

    @Test
    fun `when put fails should not throw exception`() {
        val data = TestData("test")
        val key = "key"
        every { keyValueStorage.put(key, any<String>()) } throws RuntimeException()
        
        cacheManager.put(key, data, TestData.serializer())
    }

    @Test
    fun `when get is called and key exists should decode and return value`() {
        val data = TestData("test")
        val key = "key"
        val serialized = json.encodeToString(TestData.serializer(), data)
        every { keyValueStorage.get(key, "") } returns serialized

        val result = cacheManager.get(key, TestData.serializer())

        assertEquals(data, result)
    }

    @Test
    fun `when get is called and key does not exist should return null`() {
        val key = "key"
        every { keyValueStorage.get(key, "") } returns ""

        val result = cacheManager.get(key, TestData.serializer())

        assertNull(result)
    }

    @Test
    fun `when get is called and decoding fails should return null`() {
        val key = "key"
        every { keyValueStorage.get(key, "") } returns "invalid json"

        val result = cacheManager.get(key, TestData.serializer())

        assertNull(result)
    }
}
