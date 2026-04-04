package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PremiumBannerComponentTest {

    @Test
    fun `PremiumBannerComponent holds values`() {
        val component = PremiumBannerComponent(
            title = "Premium",
            description = "Get premium now",
            icon = AppIconType.STAR
        )
        assertEquals("Premium", component.title)
        assertEquals("Get premium now", component.description)
        assertEquals(AppIconType.STAR, component.icon)
    }

    @Test
    fun `PremiumBannerComponent defaults`() {
        val component = PremiumBannerComponent("Title", "Desc")
        assertNull(component.icon)
    }
}
