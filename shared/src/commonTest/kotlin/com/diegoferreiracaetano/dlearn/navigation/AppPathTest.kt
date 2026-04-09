package com.diegoferreiracaetano.dlearn.navigation

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppPathTest {

    @Test
    fun `given an AppRequest when build is called should return formatted path string`() {
        val request = AppRequest(
            path = "home",
            params = mapOf("type" to "movies", "id" to "123")
        )
        val result = AppPath.build(request)
        assertEquals("home?type=movies&id=123", result)
    }

    @Test
    fun `given an AppRequest without params when build is called should return only the path`() {
        val request = AppRequest(path = "/profile/")
        val result = AppPath.build(request)
        assertEquals("profile", result)
    }

    @Test
    fun `given a full path string when parse is called should return correct AppRequest`() {
        val fullPath = "search?q=batman&page=1"
        val result = AppPath.parse(fullPath)

        assertEquals("search", result.path)
        assertEquals("batman", result.params?.get("q"))
        assertEquals("1", result.params?.get("page"))
    }

    @Test
    fun `given a path with trailing slashes when parse is called should return trimmed path`() {
        val fullPath = "///details/123///"
        val result = AppPath.parse(fullPath)
        assertEquals("details/123", result.path)
    }

    @Test
    fun `given a path and extra params when parse is called should combine them`() {
        val fullPath = "movie/detail?id=550"
        val extraParams = mapOf("from" to "home")
        val result = AppPath.parse(fullPath, params = extraParams)

        assertEquals("movie/detail", result.path)
        assertEquals("550", result.params?.get("id"))
        assertEquals("home", result.params?.get("from"))
    }

    @Test
    fun `given an empty or null path when parse is called should return empty path request`() {
        val resultNull = AppPath.parse(null)
        val resultEmpty = AppPath.parse("")

        assertEquals("", resultNull.path)
        assertEquals("", resultEmpty.path)
    }

    @Test
    fun `given a path using invoke operator when called should return formatted string`() {
        val result = AppPath(path = "home", params = mapOf("filter" to "all"))
        assertEquals("home?filter=all", result)
    }

    @Test
    fun `given a query string with empty key or value when build is called should filter them`() {
        val request = AppRequest(path = "test", params = mapOf("" to "val", "key" to ""))
        val result = AppPath.build(request)
        assertEquals("test", result)
    }

    @Test
    fun `given a query with malformed pairs when parse is called should ignore them`() {
        val fullPath = "test?key=val&malformed"
        val result = AppPath.parse(fullPath)
        assertEquals("test", result.path)
        assertEquals("val", result.params?.get("key"))
        assertEquals(1, result.params?.size)
    }
}
