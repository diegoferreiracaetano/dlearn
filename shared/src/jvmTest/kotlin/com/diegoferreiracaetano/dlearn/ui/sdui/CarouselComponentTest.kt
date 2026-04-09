package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CarouselComponentTest {

    @Test
    fun `CarouselComponent holds values`() {
        val item = AppLoadingComponent
        val component = CarouselComponent(
            title = "Carousel",
            items = listOf<Component>(item)
        )

        assertEquals("Carousel", component.title)
        assertEquals(listOf<Component>(item), component.items)
    }

    @Test
    fun `CarouselComponent defaults`() {
        val component = CarouselComponent(items = emptyList())
        assertNull(component.title)
    }
}
