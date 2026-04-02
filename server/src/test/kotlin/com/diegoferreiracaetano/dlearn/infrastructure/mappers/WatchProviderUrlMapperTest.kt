package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.util.WatchProviderIds
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WatchProviderUrlMapperTest {

    private val mapper = WatchProviderUrlMapper()

    @Test
    fun `given Netflix provider ID when buildUrls is called should return Netflix URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.NETFLIX, "Inception", "tt1375666", "fallback")
        
        assertTrue(result.appUrl?.contains("nflx://") == true)
        assertTrue(result.webUrl?.contains("netflix.com") == true)
        assertEquals("fallback", result.tmdbUrl)
    }

    @Test
    fun `given Disney Plus provider ID when buildUrls is called should return Disney Plus URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.DISNEY_PLUS, "Soul", null, "fallback")
        
        assertTrue(result.appUrl?.contains("disneyplus://") == true)
        assertTrue(result.webUrl?.contains("disneyplus.com") == true)
    }

    @Test
    fun `given Amazon Prime provider ID with IMDB ID when buildUrls is called should return Amazon detail URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.AMAZON_PRIME, "The Boys", "tt1190634", "fallback")
        
        assertTrue(result.appUrl?.contains("primevideo://") == true)
        assertTrue(result.appUrl?.contains("tt1190634") == true)
        assertTrue(result.webUrl?.contains("primevideo.com") == true)
        assertTrue(result.webUrl?.contains("tt1190634") == true)
    }

    @Test
    fun `given Amazon Prime provider ID without IMDB ID when buildUrls is called should return Amazon search URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.AMAZON_PRIME, "Reacher", null, "fallback")
        
        assertTrue(result.appUrl?.contains("search") == true)
        assertTrue(result.webUrl?.contains("search") == true)
    }

    @Test
    fun `given Apple TV provider ID when buildUrls is called should return Apple TV URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.APPLE_TV, "Ted Lasso", null, "fallback")
        
        assertTrue(result.appUrl?.contains("videos://") == true)
        assertTrue(result.webUrl?.contains("tv.apple.com") == true)
    }

    @Test
    fun `given HBO Max provider ID when buildUrls is called should return HBO Max URLs`() {
        val result = mapper.buildUrls(WatchProviderIds.HBO_MAX, "The Last of Us", null, "fallback")
        
        assertTrue(result.appUrl?.contains("hbomax://") == true)
        assertTrue(result.webUrl?.contains("max.com") == true)
    }

    @Test
    fun `given unknown provider ID when buildUrls is called should return null URLs`() {
        val result = mapper.buildUrls(999, "Unknown", null, "fallback")
        
        assertNull(result.appUrl)
        assertNull(result.webUrl)
        assertEquals("fallback", result.tmdbUrl)
    }
}
