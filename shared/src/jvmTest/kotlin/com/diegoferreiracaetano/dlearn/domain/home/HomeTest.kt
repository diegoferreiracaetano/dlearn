package com.diegoferreiracaetano.dlearn.domain.home

import kotlin.test.Test
import kotlin.test.assertEquals

class HomeTest {

    @Test
    fun `HomeFilterType values are correct`() {
        assertEquals("ALL", HomeFilterType.ALL.name)
        assertEquals("MOVIES", HomeFilterType.MOVIES.name)
        assertEquals("SERIES", HomeFilterType.SERIES.name)
    }

    @Test
    fun `HomeCategory holds id and name`() {
        val category = HomeCategory(1, "Name")
        assertEquals(1, category.id)
        assertEquals("Name", category.name)
    }
}
