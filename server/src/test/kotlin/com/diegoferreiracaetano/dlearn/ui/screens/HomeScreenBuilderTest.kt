package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeScreenBuilderTest {

    private val mapper = mockk<HomeMapper>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = HomeScreenBuilder(mapper, i18n)

    @Test
    fun `given home domain data when build is called should return screen with all expected SDUI components`() {
        val data = HomeDomainData(
            banner = mockk(),
            top10 = listOf(mockk()),
            popular = listOf(mockk()),
            categories = mapOf("Action" to listOf(mockk()))
        )

        every { mapper.toBannerMain(any()) } returns mockk<FullScreenBannerComponent>()
        every { mapper.toCarousel(any(), any(), any(), any()) } returns mockk<MovieCarouselComponent>()
        every { mapper.toBannerCarousel(any(), any(), any()) } returns mockk<BannerCarouselComponent>()

        val screen = builder.build(data, "en", HomeFilterType.ALL)

        assertTrue(screen.components.any { it is ChipGroupComponent })
        assertTrue(screen.components.any { it is FullScreenBannerComponent })
        assertTrue(screen.components.any { it is MovieCarouselComponent })
        assertTrue(screen.components.any { it is BannerCarouselComponent })
        assertEquals(5, screen.components.size) // Chips + Banner + Top10 + Popular + Category
    }

    @Test
    fun `given home data with no banner top10 or popular when build is called should return only chip group`() {
        val data = HomeDomainData(banner = null, top10 = emptyList(), popular = emptyList(), categories = emptyMap())

        val screen = builder.build(data, "en", HomeFilterType.ALL)

        assertTrue(screen.components.none { it is FullScreenBannerComponent })
        assertEquals(1, screen.components.size)
    }

    @Test
    fun `given home data with category having empty videos when build is called should skip that category`() {
        val data = HomeDomainData(
            banner = null,
            top10 = emptyList(),
            popular = emptyList(),
            categories = mapOf("Empty" to emptyList())
        )

        val screen = builder.build(data, "en")

        assertEquals(1, screen.components.size) // Only chips
    }

    @Test
    fun `given SERIES filter type when build is called should mark SERIES chip as selected in chip group`() {
        val data = HomeDomainData(banner = null, top10 = emptyList(), popular = emptyList(), categories = emptyMap())

        val screen = builder.build(data, "en", HomeFilterType.SERIES)

        val chipGroup = screen.components.first() as ChipGroupComponent
        assertTrue(chipGroup.items.find { it.id == HomeFilterType.SERIES.name }?.isSelected == true)
    }
}
