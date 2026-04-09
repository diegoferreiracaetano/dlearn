package com.diegoferreiracaetano.dlearn.data.cache

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeCacheManager : CacheManager {
    private val cache = mutableMapOf<String, Any?>()
    var shouldThrowOnPut = false

    override fun <T> put(key: String, value: T, serializer: KSerializer<T>) {
        if (shouldThrowOnPut) error("Cache error")
        cache[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, serializer: KSerializer<T>): T? {
        return cache[key] as? T
    }
}

class CacheExtensionsTest {

    private val cacheManager = FakeCacheManager()

    @Test
    fun `given NETWORK_FIRST when flow succeeds should emit and cache`() = runTest {
        val data = "test data"
        val flow = flowOf(data)

        val result = flow.toCache(key = "test_key", manager = cacheManager).first()

        assertEquals(data, result)
        assertEquals(data, cacheManager.get("test_key", String.serializer()))
    }

    @Test
    fun `given NETWORK_FIRST when flow fails and cache has data should emit cache`() = runTest {
        val cachedData = "cached data"
        cacheManager.put("test_key", cachedData, String.serializer())
        val flow = flow<String> { error("Network error") }

        val result = flow.toCache(key = "test_key", manager = cacheManager).first()

        assertEquals(cachedData, result)
    }

    @Test
    fun `given NETWORK_FIRST when flow fails and cache is empty should throw`() = runTest {
        val exception = RuntimeException("Network error")
        val flow = flow<String> { throw exception }

        val result = assertFailsWith<RuntimeException> {
            flow.toCache(key = "test_key", manager = cacheManager).first()
        }
        assertEquals(exception.message, result.message)
    }

    @Test
    fun `given CACHE_FIRST when cache has data should emit cache and not collect flow`() = runTest {
        val cachedData = "cached data"
        cacheManager.put("test_key", cachedData, String.serializer())
        var flowCollected = false
        val flow = flow {
            flowCollected = true
            emit("network data")
        }

        val result = flow.toCache(
            key = "test_key",
            strategy = CacheStrategy.CACHE_FIRST,
            manager = cacheManager,
        ).first()

        assertEquals(cachedData, result)
        assertEquals(false, flowCollected)
    }

    @Test
    fun `given CACHE_FIRST when cache is empty should collect flow and cache`() = runTest {
        val networkData = "network data"
        val flow = flowOf(networkData)

        val result = flow.toCache(
            key = "test_key",
            strategy = CacheStrategy.CACHE_FIRST,
            manager = cacheManager,
        ).first()

        assertEquals(networkData, result)
        assertEquals(networkData, cacheManager.get("test_key", String.serializer()))
    }

    @Test
    fun `given CACHE_FIRST when cache is empty and flow fails should throw`() = runTest {
        val exception = RuntimeException("Network error")
        val flow = flow<String> { throw exception }

        val result = assertFailsWith<RuntimeException> {
            flow.toCache(
                key = "test_key",
                strategy = CacheStrategy.CACHE_FIRST,
                manager = cacheManager,
            ).first()
        }
        assertEquals(exception.message, result.message)
    }

    @Test
    fun `given flow succeeds but cache put fails should still emit data`() = runTest {
        val data = "test data"
        val flow = flowOf(data)
        cacheManager.shouldThrowOnPut = true

        val result = flow.toCache(key = "test_key", manager = cacheManager).first()

        assertEquals(data, result)
    }
}
