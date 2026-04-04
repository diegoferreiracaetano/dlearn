package com.diegoferreiracaetano.dlearn.domain.home

import kotlin.test.Test
import kotlin.test.assertEquals

class HomeCategoryTest {

    @Test
    fun `HomeCategory holds values`() {
        val category = HomeCategory(1, "Action")
        assertEquals(1, category.id)
        assertEquals("Action", category.name)
    }

    @Test
    fun `HomeFilterType enum constants exist`() {
        assertEquals("ALL", HomeFilterType.ALL.name)
        assertEquals("MOVIES", HomeFilterType.MOVIES.name)
        assertEquals("SERIES", HomeFilterType.SERIES.name)
    }
}
