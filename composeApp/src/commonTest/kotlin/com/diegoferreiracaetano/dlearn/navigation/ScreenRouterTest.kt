package com.diegoferreiracaetano.dlearn.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenRouterTest {

    @Test
    fun `given home route when accessed should return correct route string`() {
        assertEquals(AppNavigationRoute.HOME, ScreenRouter.Home.route)
    }

    @Test
    fun `given movie detail when create route with id should return formatted route`() {
        val movieId = "456"
        val expected = "${AppNavigationRoute.MOVIES}/$movieId"
        
        val actual = ScreenRouter.MovieDetail.createRoute(movieId)
        
        assertEquals(expected, actual)
    }

    @Test
    fun `given login route when accessed should return correct route string`() {
        assertEquals(AppNavigationRoute.LOGIN, ScreenRouter.Login.route)
    }
}
