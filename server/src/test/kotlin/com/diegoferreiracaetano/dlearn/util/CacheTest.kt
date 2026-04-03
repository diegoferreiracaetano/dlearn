package com.diegoferreiracaetano.dlearn.util

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CacheTest {

    @Test
    fun `given a value when put and get are called should return the stored value`() {
        val cache = Cache<String>(ttlInMillis = 60_000)

        cache.put("key", "value")

        assertEquals("value", cache.get("key"))
    }

    @Test
    fun `given a non-existent key when get is called should return null`() {
        val cache = Cache<String>(ttlInMillis = 60_000)

        assertNull(cache.get("missing"))
    }

    @Test
    fun `given an expired entry when get is called should return null`() {
        val cache = Cache<String>(ttlInMillis = 1)

        cache.put("key", "value")
        Thread.sleep(10)

        assertNull(cache.get("key"))
    }

    @Test
    fun `given a value when put is called twice should overwrite with the new value`() {
        val cache = Cache<String>(ttlInMillis = 60_000)

        cache.put("key", "first")
        cache.put("key", "second")

        assertEquals("second", cache.get("key"))
    }

    @Test
    fun `given multiple keys when put is called should return each value independently`() {
        val cache = Cache<Int>(ttlInMillis = 60_000)

        cache.put("a", 1)
        cache.put("b", 2)

        assertEquals(1, cache.get("a"))
        assertEquals(2, cache.get("b"))
    }
}
