package com.diegoferreiracaetano.dlearn.domain.video

import kotlin.test.Test
import kotlin.test.assertEquals

class VideoCategoryTest {

    @Test
    fun `VideoCategory holds id and title`() {
        val category = VideoCategory("1", "Action")
        assertEquals("1", category.id)
        assertEquals("Action", category.title)
    }
}
