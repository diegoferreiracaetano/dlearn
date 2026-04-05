package com.diegoferreiracaetano.dlearn.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenRouterTest {

    @Test
    fun given_home_route_when_accessed_should_return_correct_route_string() {
        assertEquals(AppNavigationRoute.HOME, ScreenRouter.Home.route)
    }

    @Test
    fun given_movie_detail_when_create_route_with_id_should_return_formatted_route() {
        val movieId = "456"
        val expected = "${AppNavigationRoute.MOVIES}/$movieId"
        
        val actual = ScreenRouter.MovieDetail.createRoute(movieId)
        
        assertEquals(expected, actual)
    }

    @Test
    fun given_login_route_when_accessed_should_return_correct_route_string() {
        assertEquals(AppNavigationRoute.LOGIN, ScreenRouter.Login.route)
    }
}
