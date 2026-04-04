package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class AppMovieDetailHeaderComponentTest {

    @Test
    fun `AppMovieDetailHeaderComponent and WatchProviderComponent hold values`() {
        val provider = WatchProviderComponent(
            name = "Netflix",
            iconUrl = "icon",
            priceInfo = "Free",
            appUrl = "app",
            webUrl = "web",
            tmdbUrl = "tmdb"
        )
        val component = AppMovieDetailHeaderComponent(
            title = "Title",
            imageUrl = "image",
            year = "2024",
            duration = "120m",
            genre = "Action",
            rating = 8.5,
            trailerId = "trailer",
            downloadActionUrl = "download",
            shareActionUrl = "share",
            favoriteActionUrl = "favorite",
            watchlistActionUrl = "watchlist",
            isFavorite = true,
            isInWatchlist = true,
            providers = listOf(provider)
        )
        
        assertEquals("Title", component.title)
        assertEquals("image", component.imageUrl)
        assertEquals("2024", component.year)
        assertEquals("120m", component.duration)
        assertEquals("Action", component.genre)
        assertEquals(8.5, component.rating)
        assertEquals("trailer", component.trailerId)
        assertEquals("download", component.downloadActionUrl)
        assertEquals("share", component.shareActionUrl)
        assertEquals("favorite", component.favoriteActionUrl)
        assertEquals("watchlist", component.watchlistActionUrl)
        assertEquals(true, component.isFavorite)
        assertEquals(true, component.isInWatchlist)
        assertEquals(listOf(provider), component.providers)
        
        assertEquals("Netflix", provider.name)
        assertEquals("icon", provider.iconUrl)
        assertEquals("Free", provider.priceInfo)
        assertEquals("app", provider.appUrl)
        assertEquals("web", provider.webUrl)
        assertEquals("tmdb", provider.tmdbUrl)
    }

    @Test
    fun `AppMovieDetailHeaderComponent defaults`() {
        val component = AppMovieDetailHeaderComponent(title = "Title", imageUrl = "image")
        assertNull(component.year)
        assertNull(component.duration)
        assertNull(component.genre)
        assertNull(component.rating)
        assertNull(component.trailerId)
        assertNull(component.downloadActionUrl)
        assertNull(component.shareActionUrl)
        assertNull(component.favoriteActionUrl)
        assertNull(component.watchlistActionUrl)
        assertFalse(component.isFavorite)
        assertFalse(component.isInWatchlist)
        assertEquals(emptyList(), component.providers)
    }
    
    @Test
    fun `WatchProviderComponent defaults`() {
        val provider = WatchProviderComponent("Netflix", "icon", "Free")
        assertNull(provider.appUrl)
        assertNull(provider.webUrl)
        assertNull(provider.tmdbUrl)
    }
}
