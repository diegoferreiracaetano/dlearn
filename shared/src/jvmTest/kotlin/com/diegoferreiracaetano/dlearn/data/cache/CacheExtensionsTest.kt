package com.diegoferreiracaetano.dlearn.data.cache

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CacheExtensionsTest {

    private val cacheManager = mockk<CacheManager>(relaxed = true)

    @Test
    fun `given NETWORK_FIRST when flow succeeds should put in cache and emit`() = runTest {
        val data = "test data"
        val flow = flowOf(data)
        
        val result = flow.toCache(key = "test_key", manager = cacheManager).first()
        
        assertEquals(data, result)
        verify { cacheManager.put("test_key", data, any<KSerializer<String>>()) }
    }

    @Test
    fun `given NETWORK_FIRST when flow fails and cache exists should emit from cache`() = runTest {
        val data = "cached data"
        val flow = flow<String> { throw RuntimeException("Network error") }
        every { cacheManager.get("test_key", any<KSerializer<String>>()) } returns data
        
        val result = flow.toCache(key = "test_key", manager = cacheManager).first()
        
        assertEquals(data, result)
    }

    @Test
    fun `given CACHE_FIRST when cache exists should emit from cache and not call flow`() = runTest {
        val data = "cached data"
        var flowCalled = false
        val flow = flow { 
            flowCalled = true
            emit("network data") 
        }
        every { cacheManager.get("test_key", any<KSerializer<String>>()) } returns data
        
        val result = flow.toCache(key = "test_key", strategy = CacheStrategy.CACHE_FIRST, manager = cacheManager).first()
        
        assertEquals(data, result)
        assertEquals(false, flowCalled)
    }

    @Test
    fun `given CACHE_FIRST when cache is empty should call flow and emit`() = runTest {
        val data = "network data"
        val flow = flowOf(data)
        every { cacheManager.get("test_key", any<KSerializer<String>>()) } returns null
        
        val result = flow.toCache(key = "test_key", strategy = CacheStrategy.CACHE_FIRST, manager = cacheManager).first()
        
        assertEquals(data, result)
        verify { cacheManager.put("test_key", data, any<KSerializer<String>>()) }
    }
}
