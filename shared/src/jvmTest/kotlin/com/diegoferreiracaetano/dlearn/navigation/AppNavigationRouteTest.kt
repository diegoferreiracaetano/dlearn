package com.diegoferreiracaetano.dlearn.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class AppNavigationRouteTest {

    @Test
    fun `AppNavigationRoute values are correct`() {
        assertEquals("home", AppNavigationRoute.HOME)
        assertEquals("profile", AppNavigationRoute.PROFILE)
        assertEquals("movies", AppNavigationRoute.MOVIES)
        assertEquals("app", AppNavigationRoute.APP_PREFIX)
    }

    @Test
    fun `SDUI_APP_ROUTE is correctly formatted`() {
        val expected = "app?path={path}&params={params}&ref={ref}"
        assertEquals(expected, AppNavigationRoute.SDUI_APP_ROUTE)
    }

    @Test
    fun `MOVIE_DETAIL_ROUTE is correctly formatted`() {
        assertEquals("movies/{id}", AppNavigationRoute.MOVIE_DETAIL_ROUTE)
    }
}
