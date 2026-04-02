package com.diegoferreiracaetano.dlearn.infrastructure.cache

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class InMemoryCacheManagerTest {

    @Serializable
    data class TestData(val value: String)

    private val serializer = serializer<TestData>()

    @Test
    fun `given a value when put and get are called should return the stored value`() {
        val cache = InMemoryCacheManager(expiration = 5.minutes)

        cache.put("key1", TestData("hello"), serializer)

        val result = cache.get("key1", serializer)
        assertEquals(TestData("hello"), result)
    }

    @Test
    fun `given a non-existent key when get is called should return null`() {
        val cache = InMemoryCacheManager(expiration = 5.minutes)

        val result = cache.get("missing", serializer)

        assertNull(result)
    }

    @Test
    fun `given an expired entry when get is called should return null`() {
        val cache = InMemoryCacheManager(expiration = 1.milliseconds)

        cache.put("key", TestData("data"), serializer)
        Thread.sleep(10)

        val result = cache.get("key", serializer)
        assertNull(result)
    }

    @Test
    fun `given a value when put is called twice should overwrite with new value`() {
        val cache = InMemoryCacheManager(expiration = 5.minutes)

        cache.put("key", TestData("first"), serializer)
        cache.put("key", TestData("second"), serializer)

        val result = cache.get("key", serializer)
        assertEquals(TestData("second"), result)
    }

    @Test
    fun `given multiple keys when put and get are called should return correct values for each key`() {
        val cache = InMemoryCacheManager(expiration = 5.minutes)

        cache.put("k1", TestData("v1"), serializer)
        cache.put("k2", TestData("v2"), serializer)

        assertEquals(TestData("v1"), cache.get("k1", serializer))
        assertEquals(TestData("v2"), cache.get("k2", serializer))
    }
}
